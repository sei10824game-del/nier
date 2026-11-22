package com.nier.automata.unit.boss;

import com.nier.automata.NierAutomata;
import com.nier.automata.core.NierUnit;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

/**
 * Shared energy projectile of the machine bosses (NieR "bullet hell" orbs). Rendered purely as coloured
 * particles. Kinds: STRAIGHT, HOMING (missiles / core spheres), WELL (slow black-hole sphere that pulls), FALL (gravity).
 */
public class BossOrb extends Projectile {
    public static final int STRAIGHT = 0, HOMING = 1, WELL = 2, FALL = 3;
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BossOrb.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(BossOrb.class, EntityDataSerializers.FLOAT);
    public static RegistryObject<EntityType<BossOrb>> TYPE;

    static void register(DeferredRegister<EntityType<?>> reg) {
        if (TYPE != null) return;
        TYPE = reg.register("boss_orb", () -> EntityType.Builder.<BossOrb>of(BossOrb::new, MobCategory.MISC)
                .sized(0.5f, 0.5f).clientTrackingRange(8).updateInterval(1).setShouldReceiveVelocityUpdates(true)
                .build(NierAutomata.MODID + ":boss_orb"));
    }

    private int kind, life = 100;
    private float damage;
    private LivingEntity target;

    public BossOrb(EntityType<? extends BossOrb> t, Level l) { super(t, l); setNoGravity(true); }

    public static BossOrb shoot(NierUnit owner, Vec3 from, Vec3 vel, float damage, int color, float size, int kind, int life) {
        BossOrb o = new BossOrb(TYPE.get(), owner.level());
        o.setOwner(owner); o.setPos(from); o.setDeltaMovement(vel);
        o.damage = damage; o.kind = kind; o.life = life; o.target = owner.getTarget();
        o.entityData.set(COLOR, color); o.entityData.set(SIZE, size);
        owner.level().addFreshEntity(o);
        return o;
    }

    @Override protected void defineSynchedData() { entityData.define(COLOR, 0xff3060); entityData.define(SIZE, 0.4f); }

    @Override public void tick() {
        super.tick();
        Vec3 v = getDeltaMovement();
        if (level().isClientSide) {
            int c = entityData.get(COLOR); float s = entityData.get(SIZE);
            DustParticleOptions d = new DustParticleOptions(new Vector3f((c >> 16 & 255) / 255f, (c >> 8 & 255) / 255f, (c & 255) / 255f), 1.5f + s * 2);
            int n = 2 + (int) (s * 6);
            for (int i = 0; i < n; i++)
                level().addParticle(d, getX() + (random.nextDouble() - .5) * s, getY() + (random.nextDouble() - .5) * s, getZ() + (random.nextDouble() - .5) * s, 0, 0, 0);
            if (kind == WELL || random.nextInt(4) == 0) level().addParticle(kind == WELL ? ParticleTypes.REVERSE_PORTAL : ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);
            setPos(getX() + v.x, getY() + v.y, getZ() + v.z);
            return;
        }
        if (--life <= 0 || !(getOwner() instanceof NierUnit owner)) { discard(); return; }
        if (kind == HOMING && target != null && target.isAlive() && tickCount > 6) {
            Vec3 want = target.getEyePosition().subtract(position()).normalize().scale(v.length());
            v = v.add(want.subtract(v).scale(0.08));
        } else if (kind == FALL) v = v.add(0, -0.04, 0);
        setDeltaMovement(v);
        Vec3 next = position().add(v);
        double r = entityData.get(SIZE) + 0.3;
        if (kind == WELL) {
            for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(7), e -> BossFx.foe(owner, e))) {
                Vec3 pull = position().subtract(e.position()).normalize().scale(0.09);
                e.setDeltaMovement(e.getDeltaMovement().add(pull)); e.hurtMarked = true;
                if (tickCount % 10 == 0 && e.distanceToSqr(this) < 4) e.hurt(damageSources().indirectMagic(this, owner), damage * 0.5f);
            }
            if (life == 1) BossFx.blast(owner, position(), 3.5, damage, 1.0, false);
        } else {
            for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(r), e -> BossFx.foe(owner, e))) {
                e.hurt(damageSources().indirectMagic(this, owner), damage);
                discard(); return;
            }
            if (!level().getBlockState(net.minecraft.core.BlockPos.containing(next)).getCollisionShape(level(), net.minecraft.core.BlockPos.containing(next)).isEmpty()) {
                if (entityData.get(SIZE) >= 0.6f) BossFx.blast(owner, position(), entityData.get(SIZE) * 2.5, damage * 0.6f, 0.4, false);
                discard(); return;
            }
        }
        setPos(next.x, next.y, next.z);
    }

    @Override protected void addAdditionalSaveData(CompoundTag t) {}
    @Override protected void readAdditionalSaveData(CompoundTag t) { discard(); }
}
