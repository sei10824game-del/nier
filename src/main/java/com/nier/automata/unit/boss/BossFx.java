package com.nier.automata.unit.boss;

import com.nier.automata.core.NierUnit;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/** Server-side attack helpers used by boss Skill ticks (hit volumes, beams, pillars, orbs, particles). */
public final class BossFx {
    private BossFx() {}

    public static DustParticleOptions dust(int rgb, float size) {
        return new DustParticleOptions(new Vector3f((rgb >> 16 & 255) / 255f, (rgb >> 8 & 255) / 255f, (rgb & 255) / 255f), size);
    }
    public static float atk(NierUnit u) { return (float) u.getAttributeValue(Attributes.ATTACK_DAMAGE); }
    public static boolean foe(NierUnit u, Entity e) {
        return e instanceof LivingEntity l && l != u && l.isAlive() && !u.isAlliedTo(l) && (u.isEnemy(l) || l == u.getTarget());
    }
    /** Horizontal facing of the unit's body. */
    public static Vec3 fwd(NierUnit u) {
        float y = u.getYRot() * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(y), 0, Mth.cos(y));
    }
    public static Vec3 toward(NierUnit u, LivingEntity t) {
        if (t == null) return fwd(u);
        Vec3 d = t.position().subtract(u.position()); d = new Vec3(d.x, 0, d.z);
        return d.lengthSqr() < 1e-4 ? fwd(u) : d.normalize();
    }
    public static Vec3 at(NierUnit u, double forward, double up) { return u.position().add(fwd(u).scale(forward)).add(0, up, 0); }

    /** Seconds of fire applied by hits while > 0 (set via {@link #burning}). */
    private static int burn;
    /** Runs r with every hit inside it setting targets on fire for secs seconds. */
    public static void burning(int secs, Runnable r) { burn = secs; try { r.run(); } finally { burn = 0; } }

    private static void hit(NierUnit u, LivingEntity e, float dmg, double knock, double lift) {
        if (e.hurt(u.damageSources().mobAttack(u), dmg)) {
            if (burn > 0) e.setSecondsOnFire(burn);
            Vec3 d = e.position().subtract(u.position());
            if (knock > 0) e.knockback(knock, -d.x, -d.z);
            if (lift > 0) { e.setDeltaMovement(e.getDeltaMovement().add(0, lift, 0)); e.hurtMarked = true; }
        }
    }

    /** Melee cone in front: reach (blocks), half-angle (deg), damage multiplier of ATTACK_DAMAGE. */
    public static int arc(NierUnit u, double reach, double halfDeg, float mult, double knock) {
        Vec3 f = fwd(u); int n = 0; double cos = Math.cos(Math.toRadians(halfDeg));
        for (LivingEntity e : u.level().getEntitiesOfClass(LivingEntity.class, u.getBoundingBox().inflate(reach, 2, reach), e -> foe(u, e))) {
            Vec3 d = e.position().subtract(u.position()); d = new Vec3(d.x, 0, d.z);
            double len = d.length() - e.getBbWidth() / 2 - u.getBbWidth() / 2;
            if (len > reach || (d.lengthSqr() > 0.01 && d.normalize().dot(f) < cos)) continue;
            hit(u, e, atk(u) * mult, knock, 0); n++;
        }
        return n;
    }

    /** Sphere AoE at c. */
    public static int blast(NierUnit u, Vec3 c, double r, float mult, double knock, boolean lift) {
        int n = 0;
        for (LivingEntity e : u.level().getEntitiesOfClass(LivingEntity.class, new AABB(c, c).inflate(r), e -> foe(u, e))) {
            if (e.position().distanceTo(c) > r + e.getBbWidth() / 2) continue;
            hit(u, e, atk(u) * mult, knock, lift ? 0.6 : 0); n++;
        }
        if (u.level() instanceof ServerLevel s) {
            s.sendParticles(ParticleTypes.EXPLOSION, c.x, c.y + 0.3, c.z, Math.max(1, (int) r), r / 3, 0.2, r / 3, 0);
            ring(u, c, r, ParticleTypes.CLOUD, (int) (r * 10));
        }
        return n;
    }

    /** Line hit from a point along dir (normalised inside). */
    public static int beam(NierUnit u, Vec3 from, Vec3 dir, double len, double width, float mult, ParticleOptions p) {
        dir = dir.normalize(); int n = 0;
        AABB box = new AABB(from, from.add(dir.scale(len))).inflate(width);
        for (LivingEntity e : u.level().getEntitiesOfClass(LivingEntity.class, box, e -> foe(u, e))) {
            Vec3 rel = e.getBoundingBox().getCenter().subtract(from);
            double proj = rel.dot(dir);
            if (proj < 0 || proj > len || rel.subtract(dir.scale(proj)).length() > width + e.getBbWidth() / 2) continue;
            hit(u, e, atk(u) * mult, 0.3, 0); n++;
        }
        if (u.level() instanceof ServerLevel s)
            for (double d = 0; d < len; d += 0.5) { Vec3 q = from.add(dir.scale(d)); s.sendParticles(p, q.x, q.y, q.z, 1, width * .2, width * .2, width * .2, 0); }
        return n;
    }

    /** Energy pillar / spike erupting at pos: hits and launches upward. */
    public static void pillar(NierUnit u, Vec3 pos, double r, float mult, ParticleOptions p) {
        for (LivingEntity e : u.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(r, 3, r), e -> foe(u, e)))
            if (Math.hypot(e.getX() - pos.x, e.getZ() - pos.z) <= r + e.getBbWidth() / 2) hit(u, e, atk(u) * mult, 0, 0.8);
        if (u.level() instanceof ServerLevel s) {
            for (double y = 0; y < 4; y += 0.25) s.sendParticles(p, pos.x, pos.y + y, pos.z, 2, r * .3, 0.05, r * .3, 0);
            s.sendParticles(ParticleTypes.CRIT, pos.x, pos.y + .2, pos.z, 8, r * .5, 0.1, r * .5, 0.2);
        }
    }
    /** Ground warning marker for delayed hits. */
    public static void warn(NierUnit u, Vec3 pos, double r, int rgb) { ring(u, pos.add(0, 0.1, 0), r, dust(rgb, 1.2f), (int) (r * 8) + 6); }

    public static void ring(NierUnit u, Vec3 c, double r, ParticleOptions p, int n) {
        if (!(u.level() instanceof ServerLevel s)) return;
        for (int i = 0; i < n; i++) { double a = i * Math.PI * 2 / n; s.sendParticles(p, c.x + Math.cos(a) * r, c.y, c.z + Math.sin(a) * r, 1, 0, 0, 0, 0); }
    }
    public static void puff(NierUnit u, Vec3 c, ParticleOptions p, int n, double spread) {
        if (u.level() instanceof ServerLevel s) s.sendParticles(p, c.x, c.y, c.z, n, spread, spread, spread, 0.02);
    }

    public static BossOrb orb(NierUnit u, Vec3 from, Vec3 vel, float mult, int rgb, float size, int kind, int life) {
        return BossOrb.shoot(u, from, vel, atk(u) * mult, rgb, size, kind, life);
    }
    /** Radial ring of orbs in the horizontal plane. */
    public static void orbRing(NierUnit u, Vec3 from, int n, double speed, double phase, float mult, int rgb) {
        for (int i = 0; i < n; i++) { double a = phase + i * Math.PI * 2 / n;
            orb(u, from, new Vec3(Math.cos(a) * speed, 0, Math.sin(a) * speed), mult, rgb, 0.35f, BossOrb.STRAIGHT, 80); }
    }
    /** Fan of orbs aimed at target. */
    public static void orbFan(NierUnit u, Vec3 from, LivingEntity t, int n, double spreadDeg, double speed, float mult, int rgb) {
        Vec3 aim = t == null ? fwd(u) : t.getEyePosition().subtract(from).normalize();
        for (int i = 0; i < n; i++) {
            double a = Math.toRadians(n == 1 ? 0 : -spreadDeg / 2 + spreadDeg * i / (n - 1));
            Vec3 v = new Vec3(aim.x * Math.cos(a) - aim.z * Math.sin(a), aim.y, aim.x * Math.sin(a) + aim.z * Math.cos(a));
            orb(u, from, v.normalize().scale(speed), mult, rgb, 0.35f, BossOrb.STRAIGHT, 80);
        }
    }

    public static void lunge(NierUnit u, Vec3 dir, double speed, double up) {
        u.setDeltaMovement(dir.x * speed, up == 0 ? u.getDeltaMovement().y : up, dir.z * speed); u.hurtMarked = true;
    }
    /** Teleport to a point at distance d from target at relative angle (deg, 0 = in front of target toward self, 180 = behind). */
    public static void blink(NierUnit u, LivingEntity t, double d, double angleDeg, int rgb) {
        if (t == null) return;
        puff(u, u.position().add(0, 1, 0), dust(rgb, 1.5f), 30, 0.5);
        Vec3 dir = toward(u, t).reverse();
        double a = Math.toRadians(angleDeg);
        Vec3 off = new Vec3(dir.x * Math.cos(a) - dir.z * Math.sin(a), 0, dir.x * Math.sin(a) + dir.z * Math.cos(a)).scale(d);
        Vec3 p = t.position().add(off);
        u.teleportTo(p.x, t.getY(), p.z);
        u.lookAt(net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.EYES, t.getEyePosition());
        u.setYBodyRot(u.getYRot()); u.setYHeadRot(u.getYRot());
        puff(u, u.position().add(0, 1, 0), dust(rgb, 1.5f), 30, 0.5);
        sound(u, SoundEvents.ENDERMAN_TELEPORT, 1f, 1.4f);
    }
    public static void face(NierUnit u, LivingEntity t) {
        if (t == null) return;
        Vec3 d = toward(u, t);
        float y = (float) (Mth.atan2(d.z, d.x) * Mth.RAD_TO_DEG) - 90f;
        u.setYRot(y); u.setYBodyRot(y); u.setYHeadRot(y);
    }
    public static void sound(NierUnit u, SoundEvent s, float vol, float pitch) { u.playSound(s, vol, pitch); }
}
