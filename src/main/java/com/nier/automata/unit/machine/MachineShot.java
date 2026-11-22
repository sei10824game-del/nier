package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import javax.annotation.Nullable;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

/**
 * Every machine projectile: purple energy orbs (can be shot/slashed down), orange orbs (indestructible),
 * homing missiles, ballistic shells/bombs and proximity mines. Rendered purely with particles.
 */
public class MachineShot extends Projectile {
    public enum Kind { ORB, HARD_ORB, MISSILE, SHELL, MINE }
    private static final EntityDataAccessor<Integer> KIND = SynchedEntityData.defineId(MachineShot.class, EntityDataSerializers.INT);
    private static final DustParticleOptions PURPLE = new DustParticleOptions(new Vector3f(0.75f, 0.25f, 1f), 1.6f);
    private static final DustParticleOptions ORANGE = new DustParticleOptions(new Vector3f(1f, 0.55f, 0.1f), 1.6f);
    private static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1f, 0.15f, 0.1f), 1.0f);
    public static RegistryObject<EntityType<MachineShot>> TYPE;

    private float damage = 4f;
    private int life;
    @Nullable private LivingEntity homing;

    public MachineShot(EntityType<? extends MachineShot> t, Level l) { super(t, l); }

    static void register(DeferredRegister<EntityType<?>> reg) {
        if (TYPE != null) return;
        TYPE = reg.register("machine_shot", () -> EntityType.Builder.<MachineShot>of(MachineShot::new, MobCategory.MISC)
                .sized(0.4f, 0.4f).clientTrackingRange(6).updateInterval(1).build("nierautomata:machine_shot"));
    }

    public static MachineShot fire(MachineUnit owner, Vec3 from, Vec3 vel, Kind kind, float damage, @Nullable LivingEntity homing) {
        MachineShot s = new MachineShot(TYPE.get(), owner.level());
        s.setOwner(owner); s.setPos(from); s.setDeltaMovement(vel);
        s.entityData.set(KIND, kind.ordinal()); s.damage = damage; s.homing = homing;
        owner.level().addFreshEntity(s);
        return s;
    }

    public Kind kind() { return Kind.values()[entityData.get(KIND)]; }
    @Override protected void defineSynchedData() { entityData.define(KIND, 0); }
    @Override public boolean isNoGravity() { return true; }
    @Override public boolean isPickable() { return kind() == Kind.ORB; }

    @Override public boolean hurt(DamageSource src, float amt) {
        if (kind() != Kind.ORB || isInvulnerableTo(src)) return false;
        if (!level().isClientSide) { poof(); discard(); }   // purple orbs can be cut down
        return true;
    }

    @Override protected boolean canHitEntity(Entity e) {
        if (!super.canHitEntity(e) || !(e instanceof LivingEntity)) return false;
        return !(getOwner() instanceof NierUnit u) || !u.isAlliedTo(e);
    }

    @Override public void tick() {
        super.tick();
        Kind k = kind();
        Vec3 v = getDeltaMovement();
        if (k == Kind.SHELL || k == Kind.MINE) v = v.add(0, -0.045, 0);
        if (k == Kind.MISSILE && homing != null && homing.isAlive() && life > 6 && life < 70) {
            Vec3 want = homing.position().add(0, homing.getBbHeight() * 0.5, 0).subtract(position()).normalize().scale(Math.max(0.6, v.length()));
            v = v.add(want.subtract(v).scale(0.12));
        }
        if (k == Kind.MINE && onGround()) v = Vec3.ZERO;
        setDeltaMovement(v);
        if (!level().isClientSide) {
            HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hit.getType() != HitResult.Type.MISS && !(k == Kind.MINE && hit.getType() == HitResult.Type.BLOCK)) onHit(hit);
            if (k == Kind.MINE && life > 25 && !level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1.3), this::canHitEntity).isEmpty()) blast(1.6f);
            if (life > (k == Kind.MINE ? 400 : 160)) { if (k == Kind.MINE) blast(1.6f); else discard(); }
        }
        if (k == Kind.MINE) move(net.minecraft.world.entity.MoverType.SELF, v); else setPos(getX() + v.x, getY() + v.y, getZ() + v.z);
        if (level().isClientSide) trail(k);
        life++;
    }

    private void trail(Kind k) {
        switch (k) {
            case ORB -> level().addParticle(PURPLE, getX(), getY(), getZ(), 0, 0, 0);
            case HARD_ORB -> level().addParticle(ORANGE, getX(), getY(), getZ(), 0, 0, 0);
            case MISSILE -> { level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0); level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0); }
            case SHELL -> level().addParticle(ORANGE, getX(), getY(), getZ(), 0, 0, 0);
            case MINE -> { if (life % 10 < 5) level().addParticle(RED, getX(), getY() + 0.3, getZ(), 0, 0, 0); }
        }
    }

    @Override protected void onHitEntity(EntityHitResult r) {
        Kind k = kind();
        if (k == Kind.MISSILE || k == Kind.SHELL || k == Kind.MINE) { blast(k == Kind.SHELL ? 2.2f : 1.4f); return; }
        Entity owner = getOwner();
        r.getEntity().hurt(damageSources().mobProjectile(this, owner instanceof LivingEntity l ? l : null), damage);
        poof(); discard();
    }
    @Override protected void onHitBlock(BlockHitResult r) {
        Kind k = kind();
        if (k == Kind.MINE) return;
        if (k == Kind.MISSILE || k == Kind.SHELL) blast(k == Kind.SHELL ? 2.2f : 1.4f); else { poof(); discard(); }
    }

    /** Non-griefing explosion + explicit damage to non-allies. */
    private void blast(float radius) {
        Entity owner = getOwner();
        level().explode(this, getX(), getY(), getZ(), radius * 0.6f, Level.ExplosionInteraction.NONE);
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius), this::canHitEntity)) {
            e.invulnerableTime = 0;
            e.hurt(damageSources().explosion(this, owner), damage);
        }
        discard();
    }
    private void poof() {
        if (level() instanceof net.minecraft.server.level.ServerLevel sl)
            sl.sendParticles(kind() == Kind.ORB ? PURPLE : ORANGE, getX(), getY(), getZ(), 8, 0.2, 0.2, 0.2, 0.05);
    }
}
