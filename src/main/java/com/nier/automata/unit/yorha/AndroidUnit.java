package com.nier.automata.unit.yorha;

import com.nier.automata.core.Faction;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Shared combat kit of the android protagonists: blade arcs, AoE, lunges, support-drone fire, evade counter. */
public abstract class AndroidUnit extends NierUnit {
    /** Invulnerability ticks (evade / self-destruct). */
    public int iframes;

    protected AndroidUnit(EntityType<? extends AndroidUnit> type, Level level) { super(type, level); }

    @Override public Faction faction() { return Faction.ANDROID; }
    /** Outgoing damage multiplier (forms raise it). */
    public float damageMult() { return 1f; }

    @Override public boolean hurt(DamageSource s, float a) {
        if (iframes > 0 && !s.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.hurt(s, a);
    }
    @Override public void aiStep() { super.aiStep(); if (iframes > 0) iframes--; }
    @Override public boolean causeFallDamage(float d, float m, DamageSource s) { return false; }

    // ---------- helpers used by skill ticks (server side) ----------
    public ServerLevel sl() { return (ServerLevel) level(); }
    public Vec3 fwd() { return Vec3.directionFromRotation(0, getYRot()); }
    public boolean foe(LivingEntity e) { return e != this && e.isAlive() && (e == getTarget() || isEnemy(e)); }
    public void strike(LivingEntity e, double dmg) { e.hurt(damageSources().mobAttack(this), (float) dmg * damageMult()); }

    /** Melee arc in front: range (blocks), half angle (deg). Returns hits. */
    public int arc(double range, double halfDeg, double dmg, double knock) {
        Vec3 f = fwd(); double cos = Math.cos(Math.toRadians(halfDeg)); int n = 0;
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(range), this::foe)) {
            Vec3 d = e.position().subtract(position()); Vec3 h = new Vec3(d.x, 0, d.z);
            if (h.length() > range + e.getBbWidth() / 2) continue;
            if (h.lengthSqr() > 0.25 && h.normalize().dot(f) < cos) continue;
            strike(e, dmg); if (knock > 0) e.knockback(knock, -f.x, -f.z); n++;
        }
        Vec3 p = position().add(f.scale(1.3)).add(0, 1.1, 0);
        sl().sendParticles(ParticleTypes.SWEEP_ATTACK, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        sound(n > 0 ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f + random.nextFloat() * 0.3f);
        return n;
    }

    /** Radial hit around c; up = vertical launch. */
    public int aoe(Vec3 c, double r, double dmg, double up) {
        int n = 0;
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, new AABB(c, c).inflate(r, 2, r), this::foe)) {
            if (e.position().distanceTo(c) > r + 1) continue;
            strike(e, dmg);
            Vec3 d = e.position().subtract(c);
            if (d.lengthSqr() > 0.01) e.knockback(0.6, -d.x, -d.z);
            if (up > 0) e.setDeltaMovement(e.getDeltaMovement().add(0, up, 0));
            n++;
        }
        return n;
    }

    public void lunge(double speed, double up) {
        Vec3 f = fwd(); setDeltaMovement(f.x * speed, up != 0 ? up : getDeltaMovement().y, f.z * speed); hasImpulse = true;
    }

    /** World position of the floating support pod (right shoulder, behind). */
    public Vec3 podPos() {
        Vec3 f = fwd(); Vec3 right = new Vec3(-f.z, 0, f.x);
        return position().add(right.scale(0.65)).add(f.scale(-0.1)).add(0, 2.0, 0);
    }

    /** One pod round: hitscan with tracer and spread. */
    public void podShot(@Nullable LivingEntity t, float dmg) {
        if (t == null) return;
        Vec3 from = podPos(), to = t.getEyePosition().add(random.nextGaussian() * 0.15, random.nextGaussian() * 0.15, random.nextGaussian() * 0.15);
        tracer(from, to, ParticleTypes.ELECTRIC_SPARK, 6);
        if (hasLineOfSight(t)) { strike(t, dmg); t.invulnerableTime = 0; }
        sound(SoundEvents.CROSSBOW_SHOOT, 0.5f, 1.8f);
    }

    /** Beam from 'from' along 'dir' hitting every foe the ray crosses. */
    public void beam(Vec3 from, Vec3 dir, double len, float dmg) {
        Vec3 to = from.add(dir.normalize().scale(len));
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, new AABB(from, to).inflate(1), this::foe))
            if (e.getBoundingBox().inflate(0.4).clip(from, to).isPresent()) { strike(e, dmg); e.invulnerableTime = 0; }
        tracer(from, to, ParticleTypes.END_ROD, 3);
    }

    public void tracer(Vec3 a, Vec3 b, ParticleOptions p, int perBlock) {
        int n = (int) Math.max(2, a.distanceTo(b) * perBlock);
        for (int i = 0; i <= n; i++) { Vec3 q = a.lerp(b, i / (double) n); sl().sendParticles(p, q.x, q.y, q.z, 1, 0, 0, 0, 0); }
    }
    public void ring(Vec3 c, double r, ParticleOptions p) {
        for (int i = 0; i < 24; i++) { double a = i * Math.PI / 12;
            sl().sendParticles(p, c.x + Math.cos(a) * r, c.y + 0.1, c.z + Math.sin(a) * r, 1, 0, 0.05, 0, 0.02); }
    }
    public void sound(SoundEvent s, float vol, float pitch) { level().playSound(null, getX(), getY(), getZ(), s, getSoundSource(), vol, pitch); }

    /** Evade + counter (used when just hit): backstep with i-frames, then dash counter-slash at t=14. */
    public static Skill evade(int id) {
        return new Skill(id, "evade_counter", 22, 60, 0, 5, (u, t, k) -> {
            AndroidUnit a = (AndroidUnit) u;
            if (k == 0) { a.iframes = 10; a.lunge(-0.9, 0.3); a.sound(SoundEvents.ENDER_DRAGON_FLAP, 0.5f, 1.8f);
                a.sl().sendParticles(ParticleTypes.CLOUD, a.getX(), a.getY() + 1, a.getZ(), 8, 0.3, 0.5, 0.3, 0.02); }
            if (k == 11) a.lunge(1.4, 0.1);
            if (k == 14) a.arc(3.5, 70, 9, 0.8);
        }).weight(4).when(u -> u.hurtTime > 0);
    }

    /** Pod machine-gun burst: rounds every 2 ticks from t=6 to t=34 (40 ticks). */
    public static Skill podGun(int id, float dmg) {
        return new Skill(id, "pod_gun", 40, 60, 3, 22, (u, t, k) -> {
            if (k >= 6 && k <= 34 && k % 2 == 0) ((AndroidUnit) u).podShot(t, dmg);
        }).weight(2);
    }
}
