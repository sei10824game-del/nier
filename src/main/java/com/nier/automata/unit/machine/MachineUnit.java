package com.nier.automata.unit.machine;

import com.nier.automata.core.Faction;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/** Shared base + combat helpers for all ordinary machine lifeforms. Distances are in blocks and pre-scaled by the caller. */
public abstract class MachineUnit extends NierUnit {
    public static final DustParticleOptions LASER_FX = new DustParticleOptions(new Vector3f(1f, 0.2f, 0.15f), 1.4f);
    public static final DustParticleOptions CHARGE_FX = new DustParticleOptions(new Vector3f(1f, 0.6f, 0.2f), 1.0f);
    public final MachineVariant v;
    /** Per-cast state, reset when a skill starts. */
    protected final Set<Integer> hit = new HashSet<>();
    protected Vec3 aim = Vec3.ZERO;
    protected float lockYaw;
    protected boolean landed;
    protected int landTick;

    protected MachineUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) {
        super(t, l); this.v = v; xpReward = (int) (v.hp() / 4);
    }

    /** Ko-Shi / Ro-Shi: the whole body is charged - nearby foes are shocked continuously. */
    @Override public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && v.electric() && tickCount % 8 == 0) {
            double r = 2.2 * v.scale() * 0.8;
            com.nier.automata.core.Fx.burst(level(), position().add(0, getBbHeight() * 0.5, 0), v.skin() == MachineVariant.Skin.KOSHI ? com.nier.automata.core.Fx.HACK : 0xb060ff, 3);
            for (var e : level().getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, getBoundingBox().inflate(r, 1, r), this::foe)) strike(e, dmg(0.35f), 0.15);
        }
    }

    @Override public Faction faction() { return v.faction(); }
    @Override public void startSkill(Skill s) {
        hit.clear(); landed = false; lockYaw = getYRot();
        aim = getTarget() != null ? getTarget().position() : position().add(fwd().scale(4));
        super.startSkill(s);
    }

    // ---------- geometry ----------
    public Vec3 fwd() { return Vec3.directionFromRotation(0, getYRot()); }
    public Vec3 fwd(float yaw) { return Vec3.directionFromRotation(0, yaw); }
    /** Point relative to the unit: forward, up, right (blocks). */
    public Vec3 at(double f, double up, double right) {
        Vec3 fw = fwd(); Vec3 r = new Vec3(-fw.z, 0, fw.x);
        return position().add(fw.scale(f)).add(r.scale(right)).add(0, up, 0);
    }
    protected float dmg(double mult) { return (float) (getAttributeValue(Attributes.ATTACK_DAMAGE) * mult); }
    protected float yawTo(Vec3 p) { return (float) (Mth.atan2(p.z - getZ(), p.x - getX()) * Mth.RAD_TO_DEG) - 90f; }
    protected void face(float yaw) { setYRot(yaw); yBodyRot = yaw; yHeadRot = yaw; }

    // ---------- targeting ----------
    public boolean foe(LivingEntity e) {
        if (e == this || !e.isAlive() || isAlliedTo(e)) return false;
        if (e instanceof Player p && (p.isCreative() || p.isSpectator())) return false;
        return isEnemy(e) || e == getTarget() || e == getLastHurtByMob();
    }
    protected void strike(LivingEntity e, float amount, double knock) {
        e.invulnerableTime = 0;
        if (e.hurt(damageSources().mobAttack(this), amount) && knock > 0) {
            Vec3 d = e.position().subtract(position()).multiply(1, 0, 1).normalize();
            e.knockback(knock, -d.x, -d.z);
        }
        setLastHurtMob(e);
    }
    private Iterable<LivingEntity> near(Vec3 c, double r) {
        return level().getEntitiesOfClass(LivingEntity.class, new AABB(c, c).inflate(r, r + 1, r), this::foe);
    }
    /** Melee arc in front. once=true: each victim only once per cast. */
    protected int swing(double reach, double arcDeg, float amount, double knock, boolean once) {
        int n = 0; Vec3 fw = fwd();
        for (LivingEntity e : near(position(), reach + 1)) {
            Vec3 d = e.position().subtract(position()).multiply(1, 0, 1);
            double dist = d.length() - e.getBbWidth() / 2;
            if (dist > reach || Math.abs(e.getY() - getY()) > getBbHeight() + 1) continue;
            if (dist > 0.4 && Math.toDegrees(Math.acos(Mth.clamp(d.normalize().dot(fw), -1, 1))) > arcDeg / 2) continue;
            if (once && !hit.add(e.getId())) continue;
            strike(e, amount, knock); n++;
        }
        com.nier.automata.core.Fx.slash(level(), position().add(fw.scale(0.3)).add(0, getBbHeight() * 0.6, 0), getYRot(), (random.nextInt(3) - 1) * 40.0, Math.min(arcDeg, 160), Math.min(reach, 4) + 0.3, com.nier.automata.core.Fx.MACHINE_HOT, random.nextBoolean() ? 1 : -1);
        return n;
    }
    /** Radial blast. */
    protected void area(Vec3 c, double r, float amount, double knock, boolean once) {
        for (LivingEntity e : near(c, r)) {
            if (e.position().distanceTo(c) - e.getBbWidth() / 2 > r) continue;
            if (once && !hit.add(e.getId())) continue;
            strike(e, amount, knock);
        }
    }
    /** Expanding ground shockwave: call every tick with the current radius; hits grounded victims inside the band once. */
    protected void ring(Vec3 c, double radius, float amount) {
        com.nier.automata.core.Fx.shock(level(), c, radius, com.nier.automata.core.Fx.MACHINE_HOT);
        for (LivingEntity e : near(c, radius + 1)) {
            double d = e.position().multiply(1, 0, 1).distanceTo(c.multiply(1, 0, 1));
            if (Math.abs(d - radius) > 1.0 || Math.abs(e.getY() - c.y) > 1.2 || !hit.add(e.getId())) continue;
            strike(e, amount, 0.8); e.push(0, 0.35, 0);
        }
        if (level() instanceof ServerLevel sl) for (int i = 0; i < 20; i++) {
            double a = i * Math.PI / 10;
            sl.sendParticles(ParticleTypes.CLOUD, c.x + Math.cos(a) * radius, c.y + 0.1, c.z + Math.sin(a) * radius, 1, 0, 0.05, 0, 0.01);
        }
    }
    /** Continuous beam (damage every 4 ticks of t). Returns end point. */
    protected Vec3 beam(Vec3 from, Vec3 dir, double len, float amount, int t) {
        Vec3 to = from.add(dir.normalize().scale(len));
        var clip = level().clip(new net.minecraft.world.level.ClipContext(from, to, net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE, this));
        to = clip.getLocation();
        if (t % 4 == 0) for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, new AABB(from, to).inflate(1), this::foe))
            if (e.getBoundingBox().inflate(0.4).clip(from, to).isPresent()) strike(e, amount, 0.1);
        if (level() instanceof ServerLevel sl) {
            com.nier.automata.core.Fx.beam(level(), from, to, com.nier.automata.core.Fx.MACHINE, 0.18);
        }
        return to;
    }
    protected MachineShot shoot(Vec3 from, Vec3 at, double speed, MachineShot.Kind k, float amount) {
        return MachineShot.fire(this, from, at.subtract(from).normalize().scale(speed), k, amount, getTarget());
    }
    /** Launch a ballistic shell / mine so it lands on p after about `ticks` ticks. */
    protected MachineShot lob(Vec3 from, Vec3 p, int ticks, MachineShot.Kind k, float amount) {
        Vec3 d = p.subtract(from);
        Vec3 vel = new Vec3(d.x / ticks, d.y / ticks + 0.045 * ticks / 2, d.z / ticks);
        return MachineShot.fire(this, from, vel, k, amount, null);
    }
    protected Vec3 targetPos(@Nullable LivingEntity t) { return t == null ? aim : t.position().add(0, t.getBbHeight() * 0.5, 0); }

    // ---------- movement ----------
    protected void dash(double speed) { Vec3 f = fwd().scale(speed); setDeltaMovement(f.x, getDeltaMovement().y, f.z); }
    /** Jump that lands near p (vy chosen so it lands after ~`ticks`). */
    protected void leap(Vec3 p, int ticks, double vy) {
        Vec3 d = p.subtract(position()).multiply(1, 0, 1);
        double dist = Math.max(0, d.length() - 1.2 * v.scale());
        Vec3 h = d.lengthSqr() < 1e-4 ? Vec3.ZERO : d.normalize().scale(dist / ticks);
        setDeltaMovement(h.x, vy, h.z); hasImpulse = true;
    }

    // ---------- fx ----------
    protected void fx(ParticleOptions p, Vec3 c, int n, double spread, double speed) {
        if (level() instanceof ServerLevel sl) sl.sendParticles(p, c.x, c.y, c.z, n, spread, spread, spread, speed);
    }
    protected void dust(Vec3 c, int n, double spread) {
        fx(new BlockParticleOption(ParticleTypes.BLOCK, level().getBlockState(net.minecraft.core.BlockPos.containing(c.x, c.y - 0.5, c.z))), c, n, spread, 0.15);
        fx(ParticleTypes.POOF, c, n / 3, spread, 0.05);
    }
    protected void sound(net.minecraft.sounds.SoundEvent s, float vol, float pitch) { playSound(s, vol, pitch); }

    /** Shield carriers block frontal hits (80% reduction). Machines never take fall damage. */
    @Override public boolean hurt(net.minecraft.world.damagesource.DamageSource src, float amt) {
        if (v.weapon() == MachineVariant.Weapon.SHIELD && src.getSourcePosition() != null
                && src.getSourcePosition().subtract(position()).multiply(1, 0, 1).normalize().dot(Vec3.directionFromRotation(0, yBodyRot)) > 0.4) {
            playSound(net.minecraft.sounds.SoundEvents.SHIELD_BLOCK, 1f, 1f); amt *= 0.2f;
        }
        return super.hurt(src, amt);
    }
    @Override public boolean causeFallDamage(float d, float m, net.minecraft.world.damagesource.DamageSource s) { return false; }
    @Override public boolean isPushable() { return v.scale() < 2.5f && super.isPushable(); }
}
