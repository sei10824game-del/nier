package com.nier.automata.unit.boss.adam;

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
 * Adam - network-born machine who studies humanity. Form 0 (Copied City): teleport strikes, erupting
 * spike lines, block/orb volleys, gravity sphere, energy beam. At 50% HP: Awakening -> form 1 (blade drawn,
 * levitating) adding a sword combo and a ring of spikes.
 */
public class AdamUnit extends BossUnit {
    public static final int PHASE_STRIKE = 1, SPIKE_LINE = 2, CUBE_VOLLEY = 3, SINGULARITY = 4, BEAM = 5, AWAKEN = 6, BLADE_WALTZ = 7, SPIKE_CROWN = 8;
    static final int RED = 0xff2a3a, DARK = 0x301018;
    private Vec3 lockDir = Vec3.ZERO;

    public AdamUnit(EntityType<? extends AdamUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 7; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(PHASE_STRIKE, "Phase Strike", 24, 60, 0, 16, (u, t, k) -> {
                if (k == 6) BossFx.blink(u, t, 1.8, 180, RED);
                if (k == 12) { BossFx.arc(u, 3, 70, 1.4f, 1.2); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_STRONG, 1, 0.8f); }
            }).weight(3),
            new Skill(SPIKE_LINE, "Spike Line", 40, 100, 3, 18, (u, t, k) -> {
                if (k == 12) { lockDir = BossFx.toward(u, t); BossFx.sound(u, SoundEvents.EVOKER_CAST_SPELL, 1, 0.6f); }
                if (k >= 12 && k <= 32 && (k - 12) % 3 == 0) {
                    int i = (k - 12) / 3;
                    Vec3 p = u.position().add(lockDir.scale(2 + i * 2.2));
                    BossFx.warn(u, p.add(lockDir.scale(2.2)), 1.1, RED);
                    BossFx.pillar(u, p, 1.1, 1.1f, BossFx.dust(RED, 2f));
                    BossFx.sound(u, SoundEvents.EVOKER_FANGS_ATTACK, 1, 0.7f);
                }
            }).weight(3).noTurn(),
            new Skill(CUBE_VOLLEY, "Cube Volley", 36, 70, 5, 24, (u, t, k) -> {
                if (k == 14) BossFx.orbFan(u, BossFx.at(u, 0.5, 1.8).add(BossFx.fwd(u).yRot(1.4f).scale(0.6)), t, 5, 50, 0.55, 0.6f, RED);
                if (k == 24) BossFx.orbFan(u, BossFx.at(u, 0.5, 1.8).add(BossFx.fwd(u).yRot(-1.4f).scale(0.6)), t, 6, 70, 0.5, 0.6f, 0xffffff);
                if (k == 14 || k == 24) BossFx.sound(u, SoundEvents.ILLUSIONER_CAST_SPELL, 1, 1.2f);
            }).weight(3),
            new Skill(SINGULARITY, "Singularity", 50, 200, 4, 20, (u, t, k) -> {
                if (k < 20 && k % 2 == 0) BossFx.puff(u, BossFx.at(u, 0.8, 2.6), ParticleTypes.REVERSE_PORTAL, 6, 0.4);
                if (k == 20 && t != null) {
                    Vec3 from = BossFx.at(u, 0.8, 2.4);
                    BossFx.orb(u, from, t.position().add(0, 1, 0).subtract(from).normalize().scale(0.12), 1.2f, DARK, 1.0f, BossOrb.WELL, 90);
                    BossFx.sound(u, SoundEvents.BEACON_ACTIVATE, 1, 0.5f);
                }
            }),
            new Skill(BEAM, "Annihilation Beam", 44, 140, 4, 22, (u, t, k) -> {
                Vec3 hand = BossFx.at(u, 0.7, 1.5);
                if (k < 18) BossFx.puff(u, hand, BossFx.dust(RED, 1.5f), 4, 0.2);
                if (k == 18) { lockDir = t == null ? BossFx.fwd(u) : t.getEyePosition().subtract(hand).normalize(); BossFx.sound(u, SoundEvents.BEACON_POWER_SELECT, 2, 0.6f); }
                if (k >= 18 && k <= 36) {
                    if (t != null) lockDir = lockDir.add(t.getEyePosition().subtract(hand).normalize().subtract(lockDir).scale(0.06)).normalize();
                    BossFx.beam(u, hand, lockDir, 22, 0.6, k % 2 == 0 ? 0.3f : 0f, BossFx.dust(RED, 2f));
                }
            }).weight(2),
            phase(0.5f, 1, new Skill(AWAKEN, "Awakening", 50, 0, 0, 99, (u, t, k) -> {
                if (k < 30) { u.setDeltaMovement(0, 0.04, 0); BossFx.ring(u, u.position().add(0, 0.2, 0), 1 + k * 0.1, BossFx.dust(RED, 1.5f), 12); }
                if (k == 30) { u.setForm(1); BossFx.blast(u, u.position(), 5, 1.0f, 2.0, true); BossFx.sound(u, SoundEvents.WITHER_SPAWN, 1, 1.4f); }
            })),
            new Skill(BLADE_WALTZ, "Blade Waltz", 40, 50, 0, 6, (u, t, k) -> {
                if (k == 6) BossFx.lunge(u, BossFx.toward(u, t), 0.9, 0);
                if (k == 10 || k == 20) { BossFx.arc(u, 3.5, 80, 1.0f, 0.6); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.1f); }
                if (k == 16) BossFx.lunge(u, BossFx.toward(u, t), 0.5, 0);
                if (k == 30) { BossFx.arc(u, 4, 180, 1.6f, 1.6); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_STRONG, 1, 0.7f);
                    BossFx.ring(u, u.position().add(0, 1, 0), 3.5, ParticleTypes.SWEEP_ATTACK, 10); }
            }).forms(1, 1).weight(4),
            new Skill(SPIKE_CROWN, "Spike Crown", 40, 120, 0, 8, (u, t, k) -> {
                if (k == 6) { BossFx.warn(u, u.position(), 3, RED); BossFx.warn(u, u.position(), 5.5, RED); }
                if (k == 14) for (int i = 0; i < 8; i++) BossFx.pillar(u, u.position().add(new Vec3(3, 0, 0).yRot(i * 0.785f)), 1.0, 1.2f, BossFx.dust(RED, 2f));
                if (k == 24) for (int i = 0; i < 12; i++) BossFx.pillar(u, u.position().add(new Vec3(5.5, 0, 0).yRot(i * 0.524f + 0.26f)), 1.0, 1.2f, BossFx.dust(RED, 2f));
                if (k == 14 || k == 24) BossFx.sound(u, SoundEvents.EVOKER_FANGS_ATTACK, 2, 0.5f);
            }).forms(1, 1).noTurn().weight(2)
        );
    }
}
