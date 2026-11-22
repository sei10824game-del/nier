package com.nier.automata.unit.boss.engels;

import com.nier.automata.core.Skill;
import com.nier.automata.unit.boss.BossFx;
import com.nier.automata.unit.boss.BossOrb;
import com.nier.automata.unit.boss.BossUnit;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Engels - Goliath-class biped disguised as a factory building; its arms are two Marx-type bucket wheels.
 * Slow slams, sweeps, bucket-wheel grinding and energy-orb fire. At 50% HP: Overheat -> form 1, arms engulfed in
 * flame (slams ignite) and a flaming arm sweep is added.
 */
public class EngelsUnit extends BossUnit {
    public static final int SLAM = 1, DOUBLE_SLAM = 2, SWEEP = 3, GRINDER = 4, BARRAGE = 5, OVERHEAT = 6, FLAME_SWEEP = 7;
    static final int ORANGE = 0xff8a20, PINK = 0xff3c8c;

    public EngelsUnit(EntityType<? extends EngelsUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 6; }
    @Override public boolean isPushable() { return false; }
    private int fire() { return getForm() == 1 ? 5 : 0; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(SLAM, "Wheel Slam", 40, 60, 0, 9, (u, t, k) -> {
                if (k == 12) BossFx.warn(u, BossFx.at(u, 5, 0), 3, ORANGE);
                if (k == 24) BossFx.burning(fire(), () -> {
                    BossFx.blast(u, BossFx.at(u, 5, 0), 3, 2.0f, 1.5, true);
                    if (fire() > 0) BossFx.puff(u, BossFx.at(u, 5, 0.5), ParticleTypes.FLAME, 40, 1.2);
                    BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 2, 0.5f);
                });
            }).weight(3).noTurn(),
            new Skill(DOUBLE_SLAM, "Double Slam", 48, 100, 0, 9, (u, t, k) -> {
                if (k == 18) BossFx.warn(u, BossFx.at(u, 5, 0), 4.5, ORANGE);
                if (k == 30) BossFx.burning(fire(), () -> {
                    BossFx.blast(u, BossFx.at(u, 5, 0), 4.5, 2.4f, 2.0, true);
                    BossFx.orbRing(u, BossFx.at(u, 5, 0.6), 12, 0.35, 0, 0.5f, ORANGE);
                    BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 3, 0.4f);
                });
            }).weight(2).noTurn(),
            new Skill(SWEEP, "Arm Sweep", 44, 80, 0, 10, (u, t, k) -> {
                if (k >= 22 && k <= 30 && k % 4 == 2) {
                    BossFx.burning(fire(), () -> BossFx.arc(u, 8, 100, 1.4f, 2.5));
                    BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 2, 0.4f);
                }
            }).weight(3).noTurn(),
            new Skill(GRINDER, "Bucket Grinder", 50, 120, 0, 8, (u, t, k) -> {
                if (k >= 18 && k <= 42 && k % 4 == 2) {
                    BossFx.beam(u, BossFx.at(u, 1.5, 2.2), BossFx.fwd(u).add(0, -0.2, 0), 8, 1.4, 0.5f, ParticleTypes.CRIT);
                    BossFx.sound(u, SoundEvents.GRINDSTONE_USE, 2, 0.6f);
                }
            }).weight(2),
            new Skill(BARRAGE, "Orb Barrage", 40, 90, 6, 30, (u, t, k) -> {
                if (k == 12 || k == 20 || k == 28) {
                    Vec3 from = BossFx.at(u, 1.8, 4.2);
                    BossFx.orbFan(u, from, t, 5, 60, 0.45, 0.6f, k == 20 ? ORANGE : PINK);
                    if (k == 28 && t != null) BossFx.orb(u, from, t.getEyePosition().subtract(from).normalize().scale(0.3), 1.0f, PINK, 0.8f, BossOrb.HOMING, 120);
                    BossFx.sound(u, SoundEvents.BLAZE_SHOOT, 2, 0.6f);
                }
            }).weight(2),
            phase(0.5f, 1, new Skill(OVERHEAT, "Overheat", 50, 0, 0, 99, (u, t, k) -> {
                if (k % 2 == 0) BossFx.puff(u, BossFx.at(u, 0, 4.5), ParticleTypes.CAMPFIRE_COSY_SMOKE, 3, 0.8);
                if (k > 15) { BossFx.puff(u, BossFx.at(u, 0, 3).add(BossFx.fwd(u).yRot(1.57f).scale(2.4)), ParticleTypes.FLAME, 6, 0.5);
                              BossFx.puff(u, BossFx.at(u, 0, 3).add(BossFx.fwd(u).yRot(-1.57f).scale(2.4)), ParticleTypes.FLAME, 6, 0.5); }
                if (k == 30) { u.setForm(1); BossFx.burning(4, () -> BossFx.blast(u, u.position(), 6, 1.0f, 2.5, false)); BossFx.sound(u, SoundEvents.BLAZE_AMBIENT, 3, 0.4f); }
            })),
            new Skill(FLAME_SWEEP, "Flame Sweep", 50, 120, 0, 10, (u, t, k) -> {
                if (k >= 16 && k <= 36) {
                    double a = Math.toRadians(-60 + (k - 16) * 6);
                    Vec3 dir = BossFx.fwd(u).yRot((float) a);
                    BossFx.burning(6, () -> BossFx.beam(u, BossFx.at(u, 1, 1.2).add(dir.scale(2)), dir, 9, 1.2, k % 2 == 0 ? 0.5f : 0f, ParticleTypes.FLAME));
                    if (k % 4 == 0) BossFx.sound(u, SoundEvents.BLAZE_SHOOT, 2, 0.5f);
                }
            }).forms(1, 1).weight(3).noTurn()
        );
    }
}
