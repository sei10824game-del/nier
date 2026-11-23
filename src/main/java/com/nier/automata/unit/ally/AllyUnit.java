package com.nier.automata.unit.ally;

import com.nier.automata.core.Faction;
import com.nier.automata.core.NierUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Shared base of the android-faction roster: melee arcs, server-simulated projectiles (no extra entity
 * types needed), beams, dashes, healing and particle helpers. Everything here runs server side only.
 */
public abstract class AllyUnit extends NierUnit {
    private final List<Shot> shots = new ArrayList<>();

    protected AllyUnit(EntityType<? extends NierUnit> type, Level level) { super(type, level); }

    @Override public Faction faction() { return Faction.ANDROID; }

    /** Number of loadout variants picked at spawn (stored as form). 1 = none. */
    protected int variants() { return 1; }

    @Override public SpawnGroupData finalizeSpawn(ServerLevelAccessor l, DifficultyInstance d, MobSpawnType r, @Nullable SpawnGroupData g, @Nullable net.minecraft.nbt.CompoundTag tag) {
        if (variants() > 1) setForm(random.nextInt(variants()));
        return super.finalizeSpawn(l, d, r, g, tag);
    }

    // ---------- geometry ----------
    public Vec3 fwd() { float y = yBodyRot * Mth.DEG_TO_RAD; return new Vec3(-Mth.sin(y), 0, Mth.cos(y)); }
    public Vec3 right() { Vec3 f = fwd(); return new Vec3(-f.z, 0, f.x); }
    /** World point relative to the unit: forward, up, right (blocks). */
    public Vec3 at(double f, double up, double r) { return position().add(fwd().scale(f)).add(right().scale(r)).add(0, up, 0); }
    public Vec3 podPos() { return at(-0.35, getBbHeight() + 0.25, -0.65); }
    public Vec3 aimAt(@Nullable LivingEntity t, Vec3 from) {
        return t == null ? fwd() : t.position().add(0, t.getBbHeight() * 0.55, 0).subtract(from).normalize();
    }

    // ---------- targeting ----------
    public boolean foe(LivingEntity e) {
        return e != this && e.isAlive() && !isAlliedTo(e) && (e == getTarget() || isEnemy(e) || e.getLastHurtMob() == this);
    }
    public List<LivingEntity> foesIn(AABB box) { return level().getEntitiesOfClass(LivingEntity.class, box, this::foe); }

    // ---------- melee ----------
    /** Damage every foe within range in a frontal cone (half angle deg). Returns hit count. */
    public int arc(double range, double halfAngle, float dmg, double kb, double lift) {
        Vec3 f = fwd(); int n = 0; double cos = Math.cos(Math.toRadians(halfAngle));
        for (LivingEntity e : foesIn(getBoundingBox().inflate(range))) {
            Vec3 d = e.position().subtract(position()); double len = Math.sqrt(d.x * d.x + d.z * d.z);
            if (len > range + e.getBbWidth() / 2) continue;
            if (len > 0.6 && (d.x * f.x + d.z * f.z) / len < cos) continue;
            if (e.hurt(damageSources().mobAttack(this), dmg)) { n++; push(e, d, kb, lift); }
        }
        if (level() instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.SWEEP_ATTACK, at(range * 0.6, 1.0, 0).x, getY() + 1.0, at(range * 0.6, 1.0, 0).z, 1, 0, 0, 0, 0);
        return n;
    }
    /** Damage all foes in a sphere. */
    public int burst(Vec3 c, double r, float dmg, double kb, double lift) {
        int n = 0;
        for (LivingEntity e : foesIn(new AABB(c, c).inflate(r)))
            if (e.position().distanceTo(c) <= r + e.getBbWidth() && e.hurt(damageSources().mobAttack(this), dmg)) { n++; push(e, e.position().subtract(c), kb, lift); }
        return n;
    }
    private static void push(LivingEntity e, Vec3 d, double kb, double lift) {
        Vec3 h = new Vec3(d.x, 0, d.z); if (h.lengthSqr() < 1e-4) h = new Vec3(0, 0, 1);
        h = h.normalize().scale(kb);
        e.setDeltaMovement(e.getDeltaMovement().add(h.x, lift, h.z)); e.hurtMarked = true;
    }

    // ---------- movement ----------
    public void lunge(double speed, double up) { Vec3 f = fwd().scale(speed); setDeltaMovement(f.x, up, f.z); hurtMarked = true; }
    public void lungeTo(@Nullable LivingEntity t, double speed, double up) {
        if (t == null) { lunge(speed, up); return; }
        Vec3 d = t.position().subtract(position()); d = new Vec3(d.x, 0, d.z);
        double len = d.length(); if (len < 1.2) return;
        d = d.normalize().scale(Math.min(speed, len * 0.35)); setDeltaMovement(d.x, up, d.z); hurtMarked = true;
    }
    /** Back-step evade (the YoRHa dodge). */
    public void evade(double speed) { Vec3 f = fwd().scale(-speed); Vec3 r = right().scale((random.nextBoolean() ? 1 : -1) * speed * 0.5);
        setDeltaMovement(f.x + r.x, 0.25, f.z + r.z); hurtMarked = true; fx(ParticleTypes.CLOUD, position().add(0, 0.2, 0), 6, 0.3, 0.02); }

    // ---------- spins (rotate the entity itself so the client never has to unwind a multi-turn clip) ----------
    protected float spinBase;
    public void face(float deg) { setYRot(deg); yBodyRot = deg; yHeadRot = deg; yBodyRotO = deg; }
    /** Call every tick of a spinning skill: t &lt; from records the heading, afterwards turns degPerTick. */
    public void spin(int t, int from, float degPerTick) { if (t <= from) spinBase = yBodyRot; else face(spinBase + (t - from) * degPerTick); }

    // ---------- fx ----------
    public void fx(ParticleOptions p, Vec3 c, int n, double spread, double speed) {
        if (level() instanceof ServerLevel sl) sl.sendParticles(p, c.x, c.y, c.z, n, spread, spread, spread, speed);
    }
    public void sound(SoundEvent s, float vol, float pitch) { level().playSound(null, getX(), getY(), getZ(), s, getSoundSource(), vol, pitch); }

    // ---------- healing ----------
    public void healAllies(double r, float amount) {
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(r), e -> e == this || isAlliedTo(e))) {
            e.heal(amount); fx(ParticleTypes.HAPPY_VILLAGER, e.position().add(0, e.getBbHeight() * 0.6, 0), 6, 0.4, 0);
        }
    }

    // ---------- beams ----------
    /** Instant line attack from -> from+dir*len; hits each foe once. */
    public int beam(Vec3 from, Vec3 dir, double len, float dmg, ParticleOptions p) {
        dir = dir.normalize(); Vec3 to = from.add(dir.scale(len)); int n = 0;
        for (LivingEntity e : foesIn(new AABB(from, to).inflate(1)))
            if (e.getBoundingBox().inflate(0.4).clip(from, to).isPresent() || e.getBoundingBox().inflate(0.4).contains(from)) {
                e.invulnerableTime = 0; if (e.hurt(damageSources().indirectMagic(this, this), dmg)) n++;
            }
        for (double s = 0; s < len; s += 0.5) fx(p, from.add(dir.scale(s)), 1, 0.03, 0);
        return n;
    }

    // ---------- projectiles (simulated) ----------
    public static final class Shot {
        Vec3 pos, vel; float dmg = 4; int life = 40; ParticleOptions trail = ParticleTypes.CRIT;
        double homing, gravity, boom, size = 0.35; boolean resetIframes = true; @Nullable LivingEntity target;
        Shot(Vec3 pos, Vec3 vel) { this.pos = pos; this.vel = vel; }
        public Shot dmg(float d) { dmg = d; return this; }
        public Shot life(int l) { life = l; return this; }
        public Shot trail(ParticleOptions p) { trail = p; return this; }
        public Shot homing(double h, @Nullable LivingEntity t) { homing = h; target = t; return this; }
        public Shot gravity(double g) { gravity = g; return this; }
        public Shot boom(double r) { boom = r; return this; }
        public Shot size(double s) { size = s; return this; }
    }
    public Shot shoot(Vec3 from, Vec3 dir, double speed) { Shot s = new Shot(from, dir.normalize().scale(speed)); shots.add(s); return s; }

    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide || shots.isEmpty()) return;
        for (Iterator<Shot> it = shots.iterator(); it.hasNext(); ) {
            Shot s = it.next();
            if (s.homing > 0 && s.target != null && s.target.isAlive()) {
                Vec3 want = s.target.position().add(0, s.target.getBbHeight() * 0.5, 0).subtract(s.pos).normalize().scale(s.vel.length());
                s.vel = s.vel.add(want.subtract(s.vel).scale(s.homing));
            }
            s.vel = s.vel.add(0, -s.gravity, 0);
            Vec3 next = s.pos.add(s.vel);
            boolean hit = false;
            for (LivingEntity e : foesIn(new AABB(s.pos, next).inflate(s.size))) {
                if (e.getBoundingBox().inflate(s.size).clip(s.pos, next).isPresent() || e.getBoundingBox().inflate(s.size).contains(next)) {
                    if (s.resetIframes) e.invulnerableTime = 0;
                    e.hurt(damageSources().mobAttack(this), s.dmg); hit = true; break;
                }
            }
            if (!hit && !level().getBlockState(net.minecraft.core.BlockPos.containing(next)).getCollisionShape(level(), net.minecraft.core.BlockPos.containing(next)).isEmpty()) hit = true;
            fx(s.trail, next, 1, 0.02, 0);
            s.pos = next;
            if (hit || --s.life <= 0) {
                if (s.boom > 0) { burst(s.pos, s.boom, s.dmg, 0.6, 0.3); fx(ParticleTypes.EXPLOSION, s.pos, 1, 0, 0);
                    level().playSound(null, s.pos.x, s.pos.y, s.pos.z, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 0.7f, 1.3f); }
                it.remove();
            }
        }
    }
}
