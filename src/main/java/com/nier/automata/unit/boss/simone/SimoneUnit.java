package com.nier.automata.unit.boss.simone;

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
 * Simone - the performer of the Amusement Park stage. Spinning dress-blade pirouette, radial petal bullet rings,
 * tracking spotlight beam, a leaping grand jete and long arm lashes. At 50% HP: Crescendo -> form 1 (fan plates
 * unfurled) adding a spiral aria barrage and a curtain of falling orbs.
 */
public class SimoneUnit extends BossUnit {
    public static final int PIROUETTE = 1, PETALS = 2, SPOTLIGHT = 3, JETE = 4, LASH = 5, CRESCENDO = 6, ARIA = 7, CURTAIN = 8;
    static final int ROSE = 0xff4fa0, GOLD = 0xffd060, WHITE = 0xfff4f0;
    private Vec3 aim = Vec3.ZERO;

    public SimoneUnit(EntityType<? extends SimoneUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 6; }
    @Override public boolean isPushable() { return false; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(PIROUETTE, "Pirouette", 40, 60, 0, 5, (u, t, k) -> {
                if (k >= 12 && k <= 32 && k % 5 == 2) {
                    BossFx.blast(u, u.position().add(0, 0.6, 0), 4, 0.6f, 1.2, false);
                    BossFx.ring(u, u.position().add(0, 0.8, 0), 3.5, ParticleTypes.SWEEP_ATTACK, 8);
                    BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.3f);
                }
                if (k >= 10 && k <= 34 && t != null) BossFx.lunge(u, BossFx.toward(u, t), 0.1, 0);
            }).weight(3),
            new Skill(PETALS, "Petal Rings", 50, 80, 0, 24, (u, t, k) -> {
                if (k == 15 || k == 25 || k == 35) {
                    BossFx.orbRing(u, u.position().add(0, 1.6, 0), 16, 0.38, k * 0.1, 0.5f, k == 25 ? GOLD : ROSE);
                    BossFx.sound(u, SoundEvents.NOTE_BLOCK_CHIME.value(), 2, 0.5f + k * 0.02f);
                }
            }).weight(3),
            new Skill(SPOTLIGHT, "Spotlight", 50, 110, 4, 24, (u, t, k) -> {
                Vec3 lens = u.position().add(0, 4.4, 0);
                if (k < 16) BossFx.puff(u, lens, BossFx.dust(WHITE, 1.5f), 4, 0.2);
                if (k == 16) { aim = t == null ? BossFx.fwd(u) : t.position().subtract(lens).normalize(); BossFx.sound(u, SoundEvents.BEACON_ACTIVATE, 2, 1.6f); }
                if (k >= 16 && k <= 44) {
                    if (t != null) aim = aim.add(t.position().subtract(lens).normalize().subtract(aim).scale(0.1)).normalize();
                    BossFx.beam(u, lens, aim, 24, 0.9, k % 2 == 0 ? 0.3f : 0f, BossFx.dust(WHITE, 2.2f));
                }
            }).weight(2),
            new Skill(JETE, "Grand Jete", 40, 100, 4, 16, (u, t, k) -> {
                if (k == 8 && t != null) { Vec3 d = t.position().subtract(u.position()); u.setDeltaMovement(d.x / 20, 0.9, d.z / 20); u.hurtMarked = true; }
                if (k == 18 && t != null) BossFx.warn(u, t.position(), 4, ROSE);
                if (k == 28) { BossFx.blast(u, u.position(), 4, 1.6f, 1.6, true); BossFx.orbRing(u, u.position().add(0, 0.4, 0), 10, 0.35, 0, 0.4f, ROSE);
                               BossFx.sound(u, SoundEvents.ANVIL_LAND, 1.5f, 0.7f); }
            }).weight(2).noTurn(),
            new Skill(LASH, "Arm Lash", 30, 40, 0, 7, (u, t, k) -> {
                if (k == 14) { BossFx.arc(u, 6.5, 60, 1.2f, 1.4); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_STRONG, 1, 1.4f); }
            }).weight(3),
            phase(0.5f, 1, new Skill(CRESCENDO, "Crescendo", 50, 0, 0, 99, (u, t, k) -> {
                if (k < 30) { u.setDeltaMovement(0, 0.03, 0); BossFx.puff(u, u.position().add(0, 2, 0), BossFx.dust(k % 2 == 0 ? ROSE : GOLD, 1.5f), 8, 1.2); }
                if (k == 10) BossFx.sound(u, SoundEvents.NOTE_BLOCK_BELL.value(), 3, 0.5f);
                if (k == 30) { u.setForm(1); BossFx.blast(u, u.position(), 6, 1.0f, 2.5, false);
                    BossFx.orbRing(u, u.position().add(0, 1.6, 0), 24, 0.4, 0, 0.5f, GOLD); BossFx.sound(u, SoundEvents.NOTE_BLOCK_BELL.value(), 3, 1.0f); }
            })),
            new Skill(ARIA, "Aria Spiral", 60, 90, 0, 24, (u, t, k) -> {
                if (k >= 10 && k <= 50 && k % 2 == 0) {
                    double a = k * 0.35;
                    for (int i = 0; i < 2; i++) {
                        double b = a + i * Math.PI;
                        BossFx.orb(u, u.position().add(0, 1.8, 0), new Vec3(Math.cos(b), 0, Math.sin(b)).scale(0.4), 0.45f, i == 0 ? ROSE : GOLD, 0.3f, BossOrb.STRAIGHT, 70);
                    }
                    if (k % 6 == 0) BossFx.sound(u, SoundEvents.NOTE_BLOCK_HARP.value(), 1.5f, 0.6f + (k - 10) * 0.025f);
                }
            }).forms(1, 1).weight(3).noTurn(),
            new Skill(CURTAIN, "Curtain Fall", 50, 120, 0, 24, (u, t, k) -> {
                if (k == 8 && t != null) { aim = t.position(); BossFx.warn(u, aim, 5, GOLD); }
                if (k >= 12 && k <= 36 && k % 3 == 0) {
                    Vec3 p = aim.add((u.getRandom().nextDouble() - 0.5) * 9, 9, (u.getRandom().nextDouble() - 0.5) * 9);
                    BossFx.orb(u, p, new Vec3(0, -0.3, 0), 0.8f, k % 2 == 0 ? ROSE : GOLD, 0.6f, BossOrb.FALL, 60);
                }
                if (k == 12) BossFx.sound(u, SoundEvents.NOTE_BLOCK_BELL.value(), 2, 0.7f);
            }).forms(1, 1).weight(2)
        );
    }
}
