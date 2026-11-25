package com.nier.automata.unit.boss.hegel;

import com.nier.automata.core.Skill;
import com.nier.automata.unit.boss.BossFx;
import com.nier.automata.unit.boss.BossOrb;
import com.nier.automata.unit.boss.BossUnit;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

/**
 * Hegel - eight interlocked spherical Goliath units kept afloat by enormous energy output (first Goliath A2 faces).
 * Form 0 (linked): sweeping core laser, laser lattice, radial segment lasers. At 50% HP it separates (form 1):
 * the spherical units slam into the foe and burst outward.
 */
public class HegelUnit extends BossUnit {
    public static final int SWEEP_LASER = 1, LATTICE = 2, RING_LASERS = 3, SEPARATE = 4, SPHERE_SLAM = 5, SPHERE_BURST = 6, FOCUS = 7;
    static final int RED = 0xff2a2a, WHITE = 0xfff0e0;
    private final List<Vec3> marks = new ArrayList<>();

    // ---- nine spheres (8 ring units + core), each with its own HP; the boss dies when all are destroyed ----
    private static final EntityDataAccessor<Integer> MASK = SynchedEntityData.defineId(HegelUnit.class, EntityDataSerializers.INT);
    private final float[] sphereHp = new float[9];
    private boolean hpInit;
    static final double RING_R = 13.0 / 16.0 * 2.2, RING_Y = 14.0 / 16.0 * 2.2;

    @Override protected void defineSynchedData() { super.defineSynchedData(); entityData.define(MASK, 0); }
    public int mask() { return entityData.get(MASK); }
    public boolean sphereAlive(int i) { return (mask() & (1 << i)) == 0; }
    public int aliveCount() { int n = 0; for (int i = 0; i < 9; i++) if (sphereAlive(i)) n++; return n; }

    private void initHp() {
        if (hpInit) return; hpInit = true;
        for (int i = 0; i < 9; i++) sphereHp[i] = sphereAlive(i) ? getMaxHealth() / 9f : 0f;
    }
    /** World position of sphere i (0-7 ring, 8 core). */
    public Vec3 spherePos(int i) {
        Vec3 c = position().add(0, RING_Y, 0);
        if (i >= 8) return c;
        float y = (float) Math.toRadians(getYRot()); double a = i * Math.PI / 4;
        Vec3 f = new Vec3(-Math.sin(y), 0, Math.cos(y)), left = new Vec3(Math.cos(y), 0, Math.sin(y));
        return c.add(f.scale(Math.cos(a) * RING_R)).add(left.scale(Math.sin(a) * RING_R));
    }
    private int nearestAlive(Vec3 from) {
        int best = -1; double bd = Double.MAX_VALUE;
        for (int i = 0; i < 9; i++) if (sphereAlive(i)) { double d = spherePos(i).distanceToSqr(from); if (d < bd) { bd = d; best = i; } }
        return best;
    }
    @Override public boolean hurt(DamageSource src, float amount) {
        if (level().isClientSide || src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return super.hurt(src, amount);
        initHp();
        Vec3 from = src.getSourcePosition() != null ? src.getSourcePosition() : position().add(0, RING_Y, 0);
        if (src.getEntity() != null) from = src.getEntity().getEyePosition();
        int idx = nearestAlive(from); if (idx < 0) return super.hurt(src, amount);
        float before = getHealth();
        boolean r = super.hurt(src, amount);
        float lost = before - getHealth();
        if (lost > 0) {
            if (lost > sphereHp[idx]) { setHealth(Math.min(getMaxHealth(), getHealth() + (lost - sphereHp[idx]))); lost = sphereHp[idx]; }   // no spill-over to other spheres
            sphereHp[idx] -= lost;
            if (sphereHp[idx] <= 0.01f) {
                sphereHp[idx] = 0; entityData.set(MASK, mask() | (1 << idx));
                Vec3 p = spherePos(idx);
                BossFx.puff(this, p, ParticleTypes.EXPLOSION, 2, 0.3); BossFx.puff(this, p, ParticleTypes.LARGE_SMOKE, 20, 0.6);
                BossFx.sound(this, SoundEvents.GENERIC_EXPLODE, 2f, 0.8f);
                if (aliveCount() == 0) setHealth(0f);
            }
        }
        return r;
    }
    @Override public void addAdditionalSaveData(CompoundTag t) {
        super.addAdditionalSaveData(t); initHp(); t.putInt("SphereMask", mask());
        for (int i = 0; i < 9; i++) t.putFloat("SphereHp" + i, sphereHp[i]);
    }
    @Override public void readAdditionalSaveData(CompoundTag t) {
        super.readAdditionalSaveData(t); entityData.set(MASK, t.getInt("SphereMask")); hpInit = true;
        for (int i = 0; i < 9; i++) sphereHp[i] = t.contains("SphereHp" + i) ? t.getFloat("SphereHp" + i) : (sphereAlive(i) ? getMaxHealth() / 9f : 0f);
    }

    public HegelUnit(EntityType<? extends HegelUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 8; }
    @Override public boolean isPushable() { return false; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(SWEEP_LASER, "Sweeping Laser", 50, 80, 3, 20, (u, t, k) -> {
                Vec3 eye = BossFx.at(u, 1.6, 1.9);
                if (k < 14) BossFx.puff(u, eye, BossFx.dust(RED, 1.5f), 4, 0.2);
                if (k >= 14 && k <= 40) {
                    double a = Math.toRadians(-50 + (k - 14) * 100.0 / 26);
                    BossFx.beam(u, eye, BossFx.fwd(u).yRot((float) a).add(0, -0.12, 0), 20, 0.6, k % 2 == 0 ? 0.35f : 0f, BossFx.dust(RED, 2f));
                    if (k % 6 == 0) BossFx.sound(u, SoundEvents.BEACON_AMBIENT, 2, 1.6f);
                }
            }).weight(3).noTurn(),
            new Skill(LATTICE, "Laser Lattice", 44, 110, 0, 20, (u, t, k) -> {
                if (k == 10 && t != null) {
                    marks.clear(); marks.add(t.position());
                    for (int i = 0; i < 5; i++) marks.add(t.position().add(new Vec3(3, 0, 0).yRot(i * 1.2566f + u.getRandom().nextFloat())));
                    for (Vec3 m : marks) BossFx.warn(u, m, 1.3, RED);
                }
                if (k > 10 && k < 24 && k % 4 == 0) for (Vec3 m : marks) BossFx.warn(u, m, 1.3, RED);
                if (k == 24) { for (Vec3 m : marks) BossFx.pillar(u, m, 1.3, 1.3f, ParticleTypes.END_ROD); BossFx.sound(u, SoundEvents.BEACON_DEACTIVATE, 2, 1.8f); }
            }).weight(2),
            new Skill(RING_LASERS, "Ring Lasers", 40, 90, 0, 14, (u, t, k) -> {
                if (k >= 18 && k <= 32) {
                    double base = (k - 18) * 0.06;
                    for (int i = 0; i < 8; i++) {
                        if (!((HegelUnit) u).sphereAlive(i)) continue;
                        Vec3 dir = new Vec3(Math.cos(base + i * Math.PI / 4), 0, Math.sin(base + i * Math.PI / 4));
                        BossFx.beam(u, ((HegelUnit) u).spherePos(i), dir.add(0, -0.08, 0), 12, 0.45, k % 2 == 0 ? 0.3f : 0f, BossFx.dust(RED, 1.6f));
                    }
                }
                if (k == 18) BossFx.sound(u, SoundEvents.BEACON_POWER_SELECT, 2, 1.5f);
            }).forms(0, 0).weight(2).noTurn(),
            phase(0.5f, 1, new Skill(SEPARATE, "Separation", 50, 0, 0, 99, (u, t, k) -> {
                if (k % 4 == 0) BossFx.ring(u, u.position().add(0, 1.6, 0), 2 + k * 0.06, BossFx.dust(WHITE, 1.5f), 16);
                if (k == 30) { u.setForm(1); BossFx.blast(u, u.position().add(0, 1, 0), 6, 1.0f, 2.5, false); BossFx.sound(u, SoundEvents.IRON_GOLEM_DAMAGE, 3, 0.5f); }
            })),
            new Skill(FOCUS, "Sphere Focus Fire", 44, 90, 3, 22, (u, t, k) -> {
                HegelUnit h = (HegelUnit) u;
                for (int i = 0; i < 9; i++) if (h.sphereAlive(i) && k == 10 + i * 3 && t != null) {
                    Vec3 p = h.spherePos(i);
                    BossFx.orb(u, p, t.position().add(0, 1, 0).subtract(p).normalize().scale(0.9), 1.0f, RED, 0.7f, BossOrb.STRAIGHT, 40);
                    BossFx.sound(u, SoundEvents.BLAZE_SHOOT, 1.5f, 0.6f);
                }
            }).weight(3).noTurn(),
            new Skill(SPHERE_SLAM, "Sphere Slam", 44, 60, 0, 20, (u, t, k) -> {
                if ((k == 12 || k == 18 || k == 24) && t != null) { marks.add(0, t.position()); BossFx.warn(u, t.position(), 2.5, RED); }
                if (k == 8) marks.clear();
                if ((k == 20 || k == 26 || k == 32) && !marks.isEmpty()) {
                    Vec3 m = marks.remove(marks.size() - 1);
                    BossFx.blast(u, m, 2.5, 1.3f, 1.2, true); BossFx.sound(u, SoundEvents.ANVIL_LAND, 1.5f, 0.5f);
                }
            }).forms(1, 1).weight(3),
            new Skill(SPHERE_BURST, "Sphere Burst", 50, 100, 0, 24, (u, t, k) -> {
                if (k == 16) {
                    for (int i = 0; i < 8; i++) if (((HegelUnit) u).sphereAlive(i))
                        BossFx.orb(u, ((HegelUnit) u).spherePos(i), new Vec3(Math.cos(i * Math.PI / 4), 0, Math.sin(i * Math.PI / 4)).scale(0.5), 1.1f, RED, 0.8f, BossOrb.STRAIGHT, 40);
                    BossFx.sound(u, SoundEvents.FIREWORK_ROCKET_BLAST, 2, 0.6f);
                }
                if (k == 30 || k == 34 || k == 38) {
                    BossFx.orb(u, u.position().add(0, 2.6, 0), new Vec3(0, 0.5, 0).add(BossFx.fwd(u).yRot(k * 0.7f).scale(0.3)), 1.0f, WHITE, 0.7f, BossOrb.HOMING, 120);
                    BossFx.sound(u, SoundEvents.FIREWORK_ROCKET_LAUNCH, 2, 0.6f);
                }
            }).forms(1, 1).weight(2)
        );
    }
}
