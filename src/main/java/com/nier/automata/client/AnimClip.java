package com.nier.automata.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;

/**
 * Keyframed pose clip. Values are ABSOLUTE offsets from the bone's rest pose (rotation in radians,
 * position in model pixels), blended over whatever idle/walk animation produced, so a clip fully
 * overrides the bones it touches. Build with: AnimClip.of(20).bone("rightArm").rot(0,..).rot(5,..)
 */
public final class AnimClip {
    public enum Ease { LINEAR, IN, OUT, INOUT, SNAP }
    private record Key(float t, float[] v, Ease ease) {}
    private final Map<String, List<Key>> tracks = new HashMap<>(); // key = bone + ":r" / ":p" / ":s"
    private final Map<String, Boolean> touched = new HashMap<>();
    public final int length;
    public int blend = 3;

    private AnimClip(int length) { this.length = length; }
    public static AnimClip of(int length) { return new AnimClip(length); }
    public AnimClip blend(int b) { this.blend = b; return this; }
    public Bone bone(String name) { touched.put(name, true); return new Bone(name); }

    public final class Bone {
        private final String n;
        Bone(String n) { this.n = n; }
        public Bone bone(String other) { return AnimClip.this.bone(other); }
        public AnimClip done() { return AnimClip.this; }
        public Bone rot(float t, float x, float y, float z) { return rot(t, x, y, z, Ease.INOUT); }
        public Bone rot(float t, float x, float y, float z, Ease e) { add(n + ":r", t, new float[]{x, y, z}, e); return this; }
        public Bone pos(float t, float x, float y, float z) { return pos(t, x, y, z, Ease.INOUT); }
        public Bone pos(float t, float x, float y, float z, Ease e) { add(n + ":p", t, new float[]{x, y, z}, e); return this; }
        public Bone scale(float t, float x, float y, float z) { add(n + ":s", t, new float[]{x, y, z}, Ease.INOUT); return this; }
    }
    private void add(String k, float t, float[] v, Ease e) {
        List<Key> l = tracks.computeIfAbsent(k, x -> new ArrayList<>());
        l.add(new Key(t, v, e)); l.sort((a, b) -> Float.compare(a.t, b.t));
    }

    private static float ease(float x, Ease e) {
        return switch (e) {
            case LINEAR -> x; case IN -> x * x; case OUT -> 1 - (1 - x) * (1 - x);
            case INOUT -> x * x * (3 - 2 * x); case SNAP -> x < 1f ? 0f : 1f;
        };
    }
    private static float[] sample(List<Key> l, float t) {
        if (t <= l.get(0).t) return l.get(0).v;
        for (int i = 1; i < l.size(); i++) {
            Key b = l.get(i);
            if (t <= b.t) {
                Key a = l.get(i - 1);
                float k = ease((t - a.t) / Math.max(1e-4f, b.t - a.t), b.ease);
                float[] r = new float[3];
                for (int j = 0; j < 3; j++) r[j] = a.v[j] + (b.v[j] - a.v[j]) * k;
                return r;
            }
        }
        return l.get(l.size() - 1).v;
    }

    /** t = ticks since skill start (with partial tick). */
    public void apply(Map<String, ModelPart> bones, Map<String, float[]> rest, float t) {
        if (t < 0 || t > length) return;
        float w = Math.min(1f, Math.min(t / blend, (length - t) / blend));
        w = Math.max(0f, w); w = w * w * (3 - 2 * w);
        for (String name : touched.keySet()) {
            ModelPart p = bones.get(name); float[] r = rest.get(name);
            if (p == null) continue;
            List<Key> rt = tracks.get(name + ":r");
            if (rt != null) { float[] v = sample(rt, t);
                p.xRot += (r[3] + v[0] - p.xRot) * w; p.yRot += (r[4] + v[1] - p.yRot) * w; p.zRot += (r[5] + v[2] - p.zRot) * w; }
            List<Key> pt = tracks.get(name + ":p");
            if (pt != null) { float[] v = sample(pt, t);
                p.x += (r[0] + v[0] - p.x) * w; p.y += (r[1] + v[1] - p.y) * w; p.z += (r[2] + v[2] - p.z) * w; }
            List<Key> st = tracks.get(name + ":s");
            if (st != null) { float[] v = sample(st, t);
                p.xScale += (v[0] - p.xScale) * w; p.yScale += (v[1] - p.yScale) * w; p.zScale += (v[2] - p.zScale) * w; }
        }
    }
}
