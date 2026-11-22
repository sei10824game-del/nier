package com.nier.automata.unit.boss.grun;

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
 * Grun - colossal sea Goliath (what looks like its body is only its head). Stages follow the original fight:
 * form 0 EMP-shielded (takes half damage): arm smash/sweep, four converging lasers, slow EMP-core shots.
 * At 66%: Core Breach -> form 1, lasers destroyed, the exposed core fires strange homing spheres.
 * At 33%: Maw Opening -> form 2, the mouth opens: missile storm and maw cannon (the artillery/missile stages).
 */
public class GrunUnit extends BossUnit {
    public static final int SMASH = 1, SWEEP = 2, QUAD_LASER = 3, EMP_SHOTS = 4, BREACH = 5, CORE_SPHERES = 6, MAW_OPEN = 7, MISSILES = 8, MAW_CANNON = 9;
    static final int CYAN = 0x40e0ff, LIME = 0xd0ff40, ORANGE = 0xff9a30, RED = 0xff3030;

    public GrunUnit(EntityType<? extends GrunUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 9; }
    @Override public boolean isPushable() { return false; }
    @Override protected float damageTakenMult() { return getForm() == 0 ? 0.5f : 1f; }

    /** World position of laser emitter i (0..3) on top of the head. */
    private Vec3 emitter(int i) {
        Vec3 side = BossFx.fwd(this).yRot((float) Math.PI / 2);
        return BossFx.at(this, (i < 2 ? 1.2 : -1.2), 7.2).add(side.scale(i % 2 == 0 ? 2.0 : -2.0));
    }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(SMASH, "Arm Smash", 50, 70, 0, 12, (u, t, k) -> {
                if (k == 16) BossFx.warn(u, BossFx.at(u, 7, 0), 4, RED);
                if (k == 30) { BossFx.blast(u, BossFx.at(u, 7, 0), 4, 2.2f, 2.0, true); BossFx.puff(u, BossFx.at(u, 7, 0.5), ParticleTypes.SPLASH, 60, 2); BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 3, 0.4f); }
            }).weight(3).noTurn(),
            new Skill(SWEEP, "Arm Sweep", 50, 80, 0, 12, (u, t, k) -> {
                if (k >= 26 && k <= 34 && k % 4 == 2) { BossFx.arc(u, 11, 120, 1.5f, 3.0); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 3, 0.3f); }
            }).weight(3).noTurn(),
            new Skill(QUAD_LASER, "Quad Laser", 60, 100, 4, 30, (u, t, k) -> {
                GrunUnit g = (GrunUnit) u;
                if (k < 20) for (int i = 0; i < 4; i++) BossFx.puff(u, g.emitter(i), BossFx.dust(CYAN, 1.5f), 2, 0.2);
                if (k >= 20 && k <= 50 && t != null) {
                    for (int i = 0; i < 4; i++) {
                        Vec3 e = g.emitter(i), aim = t.position().add(0, 0.8, 0);
                        double conv = Math.max(0, 1 - (k - 20) / 20.0) * 3;   // beams start apart and converge on the target
                        Vec3 off = new Vec3(i % 2 == 0 ? conv : -conv, 0, i < 2 ? conv : -conv);
                        Vec3 d = aim.add(off).subtract(e);
                        BossFx.beam(u, e, d, d.length() + 2, 0.5, k % 2 == 0 ? 0.25f : 0f, BossFx.dust(CYAN, 1.8f));
                    }
                    if (k % 8 == 0) BossFx.sound(u, SoundEvents.BEACON_AMBIENT, 3, 1.8f);
                }
            }).forms(0, 0).weight(2),
            new Skill(EMP_SHOTS, "EMP Core Shots", 40, 60, 4, 30, (u, t, k) -> {
                if (k == 10 || k == 18 || k == 26) { BossFx.orbFan(u, BossFx.at(u, 2.6, 4.2), t, 5, 50, 0.25, 0.7f, CYAN); BossFx.sound(u, SoundEvents.BEACON_DEACTIVATE, 2, 1.2f); }
            }).weight(2),
            phase(0.66f, 1, new Skill(BREACH, "Core Breach", 50, 0, 0, 99, (u, t, k) -> {
                GrunUnit g = (GrunUnit) u;
                if (k == 10 || k == 16 || k == 22 || k == 28) { Vec3 e = g.emitter((k - 10) / 6); BossFx.puff(u, e, ParticleTypes.EXPLOSION, 3, 0.4); BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 2, 0.8f); }
                if (k == 34) { u.setForm(1); BossFx.puff(u, BossFx.at(u, 2.6, 4.2), ParticleTypes.FLASH, 1, 0); BossFx.blast(u, BossFx.at(u, 3, 0), 7, 1.0f, 3.0, false); }
            })),
            new Skill(CORE_SPHERES, "Core Spheres", 40, 90, 3, 30, (u, t, k) -> {
                if (k >= 14 && k <= 26 && k % 4 == 2) {
                    Vec3 from = BossFx.at(u, 2.6, 4.2);
                    BossFx.orb(u, from, BossFx.fwd(u).yRot((k - 20) * 0.12f).scale(0.35).add(0, 0.15, 0), 1.0f, LIME, 0.7f, BossOrb.HOMING, 140);
                    BossFx.sound(u, SoundEvents.SHULKER_SHOOT, 2, 0.6f);
                }
            }).forms(1, 2).weight(3),
            phase(0.33f, 2, new Skill(MAW_OPEN, "Maw Opening", 60, 0, 0, 99, (u, t, k) -> {
                if (k % 3 == 0) BossFx.puff(u, BossFx.at(u, 2.8, 2), ParticleTypes.LARGE_SMOKE, 6, 0.8);
                if (k == 20) BossFx.sound(u, SoundEvents.ELDER_GUARDIAN_CURSE, 3, 0.5f);
                if (k == 40) { u.setForm(2); BossFx.blast(u, u.position(), 8, 1.0f, 3.5, true); BossFx.sound(u, SoundEvents.ENDER_DRAGON_GROWL, 3, 0.6f); }
            })),
            new Skill(MISSILES, "Missile Storm", 60, 120, 0, 32, (u, t, k) -> {
                if (k >= 10 && k <= 40 && k % 3 == 1) {
                    Vec3 from = BossFx.at(u, -1.5, 6.5).add(BossFx.fwd(u).yRot(1.57f).scale((k % 2 == 0 ? 1 : -1) * 1.8));
                    BossFx.orb(u, from, new Vec3(u.getRandom().nextGaussian() * 0.1, 0.55, u.getRandom().nextGaussian() * 0.1), 0.8f, ORANGE, 0.45f, BossOrb.HOMING, 160);
                    BossFx.sound(u, SoundEvents.FIREWORK_ROCKET_LAUNCH, 2, 0.7f);
                }
            }).forms(2, 2).weight(3),
            new Skill(MAW_CANNON, "Maw Cannon", 60, 140, 4, 30, (u, t, k) -> {
                Vec3 mouth = BossFx.at(u, 2.8, 1.8);
                if (k < 24) BossFx.puff(u, mouth, BossFx.dust(RED, 2f), 6, 0.4);
                if (k >= 24 && k <= 50) {
                    Vec3 d = t == null ? BossFx.fwd(u) : t.position().add(0, 0.8, 0).subtract(mouth);
                    BossFx.beam(u, mouth, d, 28, 1.2, k % 2 == 0 ? 0.45f : 0f, BossFx.dust(RED, 2.5f));
                    if (k % 6 == 0) BossFx.sound(u, SoundEvents.BEACON_POWER_SELECT, 3, 0.5f);
                }
            }).forms(2, 2).weight(2)
        );
    }
}
