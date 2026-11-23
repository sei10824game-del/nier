package com.nier.automata.unit.boss.eve;

import com.nier.automata.core.Skill;
import com.nier.automata.unit.boss.BossFx;
import com.nier.automata.unit.boss.BossUnit;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Eve - the younger network brother, a brawler. Fist combos, ground pound, charging rush, spinning steel pipes,
 * meteor leap. At 50% HP: Eve's Rage -> form 1 (glowing) adding radial energy barrages and rotating chain beams.
 */
public class EveUnit extends BossUnit {
    public static final int FISTS = 1, POUND = 2, RUSH = 3, PIPE_STORM = 4, LEAP = 5, RAGE = 6, BARRAGE = 7, CHAIN_BEAMS = 8;
    static final int RED = 0xff3020, GOLD = 0xffc040;
    private Vec3 lockDir = Vec3.ZERO;

    public EveUnit(EntityType<? extends EveUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 2.5; }
    @Override public boolean isPushable() { return false; }

    private boolean rage() { return getForm() == 1; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(FISTS, "Hammer Fists", 30, 40, 0, 4.5, (u, t, k) -> {
                if (k == 6 || k == 13 || k == 21) BossFx.lunge(u, BossFx.toward(u, t), 0.35, 0);
                if (k == 8 || k == 15) { BossFx.arc(u, 3.2, 60, 0.9f, 0.5); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_STRONG, 1, 1.1f); }
                if (k == 24) { BossFx.arc(u, 3.5, 70, 1.5f, 1.8); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 0.6f);
                    BossFx.puff(u, BossFx.at(u, 2, 1.2), ParticleTypes.CRIT, 15, 0.4); }
            }).weight(4),
            new Skill(POUND, "Ground Pound", 34, 90, 0, 7, (u, t, k) -> {
                if (k == 6) BossFx.lunge(u, Vec3.ZERO, 0, 0.55);
                if (k == 18) {
                    BossFx.blast(u, u.position(), 5.5, 1.6f, 1.5, true);
                    BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 1.5f, 0.6f);
                    BossFx.orbRing(u, u.position().add(0, 0.5, 0), ((EveUnit) u).rage() ? 16 : 10, 0.45, 0, 0.5f, RED);
                }
            }).weight(3),
            new Skill(RUSH, "Charging Rush", 30, 80, 4, 16, (u, t, k) -> {
                if (k == 7) lockDir = BossFx.toward(u, t);
                if (k >= 8 && k <= 20) {
                    BossFx.lunge(u, lockDir, 1.1, 0);
                    if (k % 3 == 2) BossFx.arc(u, 2.2, 75, 0.9f, 2.0);
                    BossFx.puff(u, u.position().add(0, 0.2, 0), ParticleTypes.CLOUD, 3, 0.3);
                }
                if (k == 8) BossFx.sound(u, SoundEvents.RAVAGER_ROAR, 1, 1.2f);
            }).weight(2).noTurn(),
            new Skill(PIPE_STORM, "Pipe Storm", 50, 120, 0, 6, (u, t, k) -> {
                if (k >= 10 && k <= 40 && k % 5 == 0) {
                    BossFx.blast(u, u.position().add(0, 1, 0), 4.5, 0.6f, 1.0, false);
                    BossFx.ring(u, u.position().add(0, 1.4, 0), 4, ParticleTypes.SWEEP_ATTACK, 8);
                    BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.7f + k * 0.01f);
                }
                if (k >= 10 && k <= 40 && t != null) BossFx.lunge(u, BossFx.toward(u, t), 0.12, 0);
            }).weight(2),
            new Skill(LEAP, "Meteor Leap", 40, 120, 5, 20, (u, t, k) -> {
                if (k == 8 && t != null) {
                    Vec3 d = t.position().subtract(u.position());
                    u.setDeltaMovement(d.x / 20, 1.0, d.z / 20); u.hurtMarked = true;
                    BossFx.sound(u, SoundEvents.RAVAGER_ROAR, 1, 0.9f);
                }
                if (k == 22 && t != null) BossFx.warn(u, t.position(), 4, RED);
                if (k == 26) BossFx.lunge(u, Vec3.ZERO, 0, -1.4);
                if (k == 29) { BossFx.blast(u, u.position(), 4.5, 1.8f, 1.8, true); BossFx.sound(u, SoundEvents.GENERIC_EXPLODE, 2, 0.5f); }
            }).weight(2).noTurn(),
            phase(0.5f, 1, new Skill(RAGE, "Eve's Rage", 50, 0, 0, 99, (u, t, k) -> {
                if (k < 30 && k % 3 == 0) BossFx.puff(u, u.position().add(0, 1.2, 0), BossFx.dust(RED, 2f), 20, 0.8);
                if (k == 10) BossFx.sound(u, SoundEvents.RAVAGER_ROAR, 2, 0.5f);
                if (k == 30) { u.setForm(1); BossFx.blast(u, u.position(), 6, 1.2f, 2.5, true); BossFx.sound(u, SoundEvents.WITHER_SPAWN, 1, 1.1f); }
            })),
            new Skill(BARRAGE, "Rage Barrage", 44, 80, 0, 24, (u, t, k) -> {
                if (k >= 12 && k <= 36 && k % 8 == 4) {
                    BossFx.orbRing(u, u.position().add(0, 1.4, 0), 14, 0.42, k * 0.11, 0.6f, k % 16 == 4 ? RED : GOLD);
                    BossFx.sound(u, SoundEvents.BLAZE_SHOOT, 1, 0.8f);
                }
            }).forms(1, 1).weight(3),
            new Skill(CHAIN_BEAMS, "Chain Beams", 50, 140, 0, 12, (u, t, k) -> {
                if (k == 12) BossFx.sound(u, SoundEvents.BEACON_POWER_SELECT, 2, 1.4f);
                if (k >= 14 && k <= 44) {
                    double base = (k - 14) * 0.09;
                    for (int i = 0; i < 4; i++) {
                        Vec3 dir = new Vec3(Math.cos(base + i * Math.PI / 2), 0, Math.sin(base + i * Math.PI / 2));
                        BossFx.beam(u, u.position().add(0, 1.2, 0).add(dir.scale(0.8)), dir, 10, 0.5, k % 2 == 0 ? 0.25f : 0f, BossFx.dust(RED, 1.8f));
                    }
                }
            }).forms(1, 1).noTurn().weight(2)
        );
    }
}
