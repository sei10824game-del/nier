package com.nier.automata.unit.ally;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Client-only pose/sequence helpers for {@link AnimClip}. A Seq is a list of full-body key poses; every bone
 * any pose mentions is keyed at every key (missing = rest), so poses blend as whole-body stances.
 * Weapon bone values assume a melee weapon rest of about -1.3 rad (W = +1.2 brings the blade in line with the arm).
 * Sign convention: s = +1 swing from the unit's right to its left, -1 the reverse.
 */
public final class Clips {
    private Clips() {}
    static final float W = 1.2f;
    private static final float[] Z = {0, 0, 0}, ONE = {1, 1, 1};

    public static final class Pose {
        final Map<String, float[]> r = new HashMap<>(), p = new HashMap<>(), s = new HashMap<>();
        public Pose r(String b, float x, float y, float z) { r.put(b, new float[]{x, y, z}); return this; }
        public Pose p(String b, float x, float y, float z) { p.put(b, new float[]{x, y, z}); return this; }
        /** Scale (1 = rest). */
        public Pose scale(String b, float x, float y, float z) { s.put(b, new float[]{x, y, z}); return this; }
        /** Copy with extra overrides. */
        public Pose with(Pose o) { Pose n = new Pose(); n.r.putAll(r); n.p.putAll(p); n.s.putAll(s); n.r.putAll(o.r); n.p.putAll(o.p); n.s.putAll(o.s); return n; }
    }
    public static Pose P() { return new Pose(); }
    public static final Pose REST = new Pose();

    private record K(float t, Pose pose, Ease e) {}
    public static final class Seq {
        private final int d; private final List<K> keys = new ArrayList<>();
        Seq(int d) { this.d = d; }
        public Seq key(float t, Pose p) { return key(t, p, Ease.INOUT); }
        public Seq key(float t, Pose p, Ease e) { keys.add(new K(Math.min(t, d), p, e)); return this; }
        public AnimClip build() {
            if (keys.stream().noneMatch(k -> k.t <= 0)) keys.add(0, new K(0, REST, Ease.INOUT));
            if (keys.stream().noneMatch(k -> k.t >= d)) keys.add(new K(d, REST, Ease.INOUT));
            Set<String> rb = new LinkedHashSet<>(), pb = new LinkedHashSet<>(), sb = new LinkedHashSet<>();
            for (K k : keys) { rb.addAll(k.pose.r.keySet()); pb.addAll(k.pose.p.keySet()); sb.addAll(k.pose.s.keySet()); }
            AnimClip c = AnimClip.of(d);
            for (String b : rb) { AnimClip.Bone bone = c.bone(b);
                for (K k : keys) { float[] v = k.pose.r.getOrDefault(b, Z); bone.rot(k.t, v[0], v[1], v[2], k.e); } }
            for (String b : pb) { AnimClip.Bone bone = c.bone(b);
                for (K k : keys) { float[] v = k.pose.p.getOrDefault(b, Z); bone.pos(k.t, v[0], v[1], v[2], k.e); } }
            for (String b : sb) { AnimClip.Bone bone = c.bone(b);
                for (K k : keys) { float[] v = k.pose.s.getOrDefault(b, ONE); bone.scale(k.t, v[0], v[1], v[2]); } }
            return c;
        }
    }
    public static Seq seq(int d) { return new Seq(d); }

    // ================= stances =================
    public static Pose guard() {
        return P().r("rightArm", -0.5f, 0.2f, 0.2f).r("leftArm", -0.3f, -0.2f, -0.2f).r("weapon", 0.2f, 0, 0)
                .r("torso", 0.1f, 0.2f, 0).p("body", 0, 1, 0).r("rightLeg", 0.25f, 0, 0.05f).r("leftLeg", -0.3f, 0, -0.05f);
    }
    public static Pose slashWind(float s) {
        return P().r("torso", 0.05f, 0.75f * s, 0.05f * s).r("head", 0, -0.45f * s, 0)
                .r("rightArm", -1.9f, 0.9f * s, 0.5f).r("weapon", W * 0.6f, 0, 0).r("leftArm", -0.7f, 0.3f * s, -0.6f)
                .p("body", 0, 1.6f, 1.2f).r("body", 0, 0.2f * s, 0).r("rightLeg", 0.35f, 0, 0.05f).r("leftLeg", -0.35f, 0, -0.05f);
    }
    public static Pose slashHit(float s) {
        return P().r("torso", 0.2f, -0.85f * s, -0.08f * s).r("head", 0, 0.5f * s, 0)
                .r("rightArm", -1.45f, -1.05f * s, 0.25f).r("weapon", W, 0, 0).r("leftArm", -0.2f, 0, -1.0f)
                .p("body", 0, 1.1f, -2.6f).r("body", 0, -0.25f * s, 0).r("rightLeg", -0.65f, 0, 0).r("leftLeg", 0.55f, 0, 0);
    }
    public static Pose slashFollow(float s) {
        return P().r("torso", 0.15f, -1.0f * s, -0.1f * s).r("head", 0, 0.6f * s, 0)
                .r("rightArm", -1.0f, -1.4f * s, 0.15f).r("weapon", W * 0.8f, 0, 0).r("leftArm", 0.1f, 0, -0.8f)
                .p("body", 0, 1.3f, -2.8f).r("body", 0, -0.3f * s, 0).r("rightLeg", -0.55f, 0, 0).r("leftLeg", 0.45f, 0, 0);
    }
    public static Pose overUp() {
        return P().r("torso", -0.35f, 0, 0).r("head", -0.3f, 0, 0).r("rightArm", -3.0f, 0, 0.15f).r("leftArm", -2.9f, 0, -0.15f)
                .r("weapon", W * 0.9f, 0, 0).p("body", 0, -2.5f, 1.0f).r("rightLeg", 0.3f, 0, 0).r("leftLeg", -0.5f, 0, 0);
    }
    public static Pose overDown() {
        return P().r("torso", 0.75f, 0, 0).r("head", 0.25f, 0, 0).r("rightArm", -0.9f, 0, 0.1f).r("leftArm", -0.9f, 0, -0.1f)
                .r("weapon", W, 0, 0).p("body", 0, 3.5f, -3.0f).r("rightLeg", -0.9f, 0, 0).r("leftLeg", 0.7f, 0, 0);
    }
    public static Pose thrustWind() {
        return P().r("torso", 0.1f, 0.6f, 0).r("head", 0, -0.5f, 0).r("rightArm", -1.3f, 0.6f, 0.3f).p("rightArm", 0, 0, 3)
                .r("leftArm", -1.2f, -0.4f, -0.2f).r("weapon", W, 0, 0).p("body", 0, 2.2f, 2.0f)
                .r("rightLeg", 0.5f, 0, 0).r("leftLeg", -0.6f, 0, 0);
    }
    public static Pose thrustHit() {
        return P().r("torso", 0.35f, -0.2f, 0).r("head", -0.2f, 0.15f, 0).r("rightArm", -1.6f, -0.1f, 0.05f).p("rightArm", 0, 0, -3)
                .r("leftArm", -1.5f, 0.2f, 0).r("weapon", W, 0, 0).p("body", 0, 2.6f, -4.5f)
                .r("rightLeg", -1.0f, 0, 0).r("leftLeg", 0.8f, 0, 0);
    }
    public static Pose aim() {
        return P().r("torso", 0, 0.35f, 0).r("head", 0, -0.3f, 0).r("rightArm", -1.55f, 0.15f, 0).r("leftArm", -1.45f, 0.75f, 0)
                .r("weapon", 1.3f, 0, 0).p("body", 0, 0.8f, 0).r("rightLeg", 0.3f, 0, 0.05f).r("leftLeg", -0.25f, 0, -0.05f);
    }
    public static Pose recoil() {
        return aim().with(P().r("rightArm", -1.75f, 0.15f, 0).r("leftArm", -1.6f, 0.75f, 0).r("torso", -0.08f, 0.38f, 0).p("rightArm", 0, 0, 1).p("body", 0, 0.8f, 0.6f));
    }
    /** Commanding gesture: pod pushed forward, right hand points. */
    public static Pose podAim() {
        return P().r("rightArm", -1.5f, -0.25f, 0).r("leftArm", 0.2f, 0, -0.3f).r("torso", 0, -0.2f, 0).r("head", 0, 0.15f, 0)
                .p("pod", 2, 3, -6).r("pod", 0, 0, 0);
    }
    public static Pose podKick() { return podAim().with(P().p("pod", 2, 2.5f, -4.5f).r("pod", -0.25f, 0, 0)); }
    public static Pose castRaise() {
        return P().r("rightArm", -2.6f, 0.4f, 0.5f).r("leftArm", -2.6f, -0.4f, -0.5f).r("torso", -0.3f, 0, 0).r("head", -0.4f, 0, 0)
                .p("body", 0, -1.5f, 1).r("rightLeg", 0.15f, 0, 0.1f).r("leftLeg", -0.15f, 0, -0.1f);
    }
    public static Pose castRelease() {
        return P().r("rightArm", -1.6f, -0.3f, 0.1f).r("leftArm", -1.6f, 0.3f, -0.1f).r("torso", 0.25f, 0, 0).r("head", 0.1f, 0, 0)
                .p("body", 0, 1.0f, -1.5f).r("rightLeg", -0.4f, 0, 0).r("leftLeg", 0.35f, 0, 0);
    }
    public static Pose crouch() {
        return P().r("torso", 0.5f, 0, 0).r("head", -0.4f, 0, 0).r("rightArm", 0.4f, 0, 0.4f).r("leftArm", 0.4f, 0, -0.4f)
                .p("body", 0, 4, 1).r("rightLeg", -1.1f, 0, 0).r("leftLeg", -1.1f, 0, 0).p("rightLeg", 0, -2, -1).p("leftLeg", 0, -2, -1);
    }
    public static Pose dashLow() {
        return P().r("torso", 0.9f, 0, 0).r("head", -0.7f, 0, 0).r("rightArm", 1.2f, 0, 0.4f).r("leftArm", 1.2f, 0, -0.4f)
                .r("weapon", -0.2f, 0, 0).p("body", 0, 3, -2).r("rightLeg", -1.0f, 0, 0).r("leftLeg", 0.9f, 0, 0);
    }
    public static Pose backstep() {
        return P().r("torso", -0.35f, 0, 0.1f).r("head", 0.2f, 0, 0).r("rightArm", 0.5f, 0, 0.7f).r("leftArm", 0.5f, 0, -0.7f)
                .p("body", 0, -1, 3).r("rightLeg", 0.8f, 0, 0).r("leftLeg", -0.2f, 0, 0);
    }

    // ================= ready-made clips =================
    public static AnimClip slash(int d, int hit, float s) {
        return seq(d).key(hit - 5, slashWind(s)).key(hit, slashHit(s), Ease.OUT).key(hit + 3, slashFollow(s)).build();
    }
    /** Chained alternating slashes; the last hit is an overhead finisher when finisher=true. */
    public static AnimClip combo(int d, boolean finisher, int... hits) {
        Seq q = seq(d); float s = 1;
        for (int i = 0; i < hits.length; i++) {
            int h = hits[i], prev = i == 0 ? 0 : hits[i - 1] + 2;
            boolean last = i == hits.length - 1;
            if (last && finisher) { q.key(Math.max(prev, h - 5), overUp()).key(h, overDown(), Ease.IN).key(h + 4, overDown()); }
            else { q.key(Math.max(prev + 1, h - 4), slashWind(s)).key(h, slashHit(s), Ease.OUT).key(h + 2, slashFollow(s)); }
            s = -s;
        }
        return q.build();
    }
    public static AnimClip overhead(int d, int hit) {
        return seq(d).key(hit - 7, crouch()).key(hit - 3, overUp()).key(hit, overDown(), Ease.IN).key(hit + 5, overDown()).build();
    }
    public static AnimClip thrust(int d, int hit) {
        return seq(d).key(hit - 6, thrustWind()).key(hit - 4, thrustWind()).key(hit, thrustHit(), Ease.OUT).key(hit + 4, thrustHit()).build();
    }
    /** Rapid thrust flurry (spear multi-hit). */
    public static AnimClip flurry(int d, int first, int last, int every) {
        Seq q = seq(d).key(first - 4, thrustWind());
        for (int t = first; t <= last; t += every) { q.key(t, thrustHit(), Ease.OUT); if (t + every / 2f < last) q.key(t + every / 2f, thrustWind().with(P().p("body", 0, 2.4f, -1))); }
        return q.key(last + 3, thrustHit()).build();
    }
    public static AnimClip shoot(int d, int first, int last, int every) {
        Seq q = seq(d).key(Math.max(1, first - 4), aim());
        for (int t = first; t <= last; t += every) { q.key(t, recoil(), Ease.OUT); q.key(Math.min(t + every * 0.6f, d - 1), aim()); }
        return q.key(last + 3, aim()).build();
    }
    public static AnimClip podShoot(int d, int first, int last, int every) {
        Seq q = seq(d).key(Math.max(1, first - 4), podAim());
        for (int t = first; t <= last; t += every) { q.key(t, podKick(), Ease.OUT); q.key(Math.min(t + every * 0.6f, d - 1), podAim()); }
        return q.key(last + 2, podAim()).build();
    }
    public static AnimClip cast(int d, int release) {
        return seq(d).key(release - 8, castRaise()).key(release - 2, castRaise().with(P().p("body", 0, -2.2f, 1.5f)))
                .key(release, castRelease(), Ease.OUT).key(release + 5, castRelease()).build();
    }
    /** Spin attack pose track; pair with AllyUnit.spin on the server for the actual rotation. */
    public static AnimClip spin(int d, int from, int to, int turns) {
        Pose arms = P().r("rightArm", -1.5f, 0, 1.2f).r("leftArm", -1.5f, 0, -1.2f).r("weapon", W, 0, 0).p("body", 0, 1.5f, 0)
                .r("rightLeg", 0.2f, 0, 0.2f).r("leftLeg", -0.2f, 0, -0.2f);
        Seq q = seq(d).key(from - 4, slashWind(1).with(P().p("body", 0, 2.5f, 0)));
        int n = turns * 4;
        // the entity itself turns (AllyUnit.spin); the clip leans into the rotation and whips the arms
        for (int i = 0; i <= n; i++) q.key(from + (to - from) * i / (float) n, arms.with(P().r("torso", 0.15f, i % 2 == 0 ? 0.25f : -0.1f, i % 2 == 0 ? 0.15f : 0)), Ease.LINEAR);
        return q.key(to + 3, slashFollow(1)).build();
    }
    public static AnimClip dashStrike(int d, int hit) {
        return seq(d).key(hit - 6, dashLow()).key(hit - 2, dashLow().with(P().p("body", 0, 2.5f, -6))).key(hit, slashHit(1), Ease.OUT)
                .key(hit + 4, slashFollow(1)).build();
    }
    public static AnimClip launcher(int d, int hit) {
        Pose low = crouch().with(P().r("rightArm", 0.6f, 0.3f, 0.3f).r("weapon", W, 0, 0));
        Pose high = P().r("torso", -0.4f, -0.3f, 0).r("head", -0.4f, 0, 0).r("rightArm", -2.9f, -0.2f, 0.2f).r("weapon", W, 0, 0)
                .r("leftArm", 0.4f, 0, -0.6f).p("body", 0, -3, -1).r("rightLeg", -0.3f, 0, 0).r("leftLeg", 0.6f, 0, 0);
        return seq(d).key(hit - 5, low).key(hit, high, Ease.OUT).key(hit + 5, high).build();
    }
    public static AnimClip evade(int d) {
        return seq(d).key(2, backstep(), Ease.OUT).key(d - 3, backstep()).build();
    }
    public static AnimClip heal(int d, int pulse) {
        Pose pray = P().r("rightArm", -1.2f, -0.6f, 0).r("leftArm", -1.2f, 0.6f, 0).r("head", 0.4f, 0, 0).r("torso", 0.15f, 0, 0).p("body", 0, 1.5f, 0);
        return seq(d).key(pulse - 6, pray).key(pulse, castRaise(), Ease.OUT).key(pulse + 6, castRaise()).build();
    }
}
