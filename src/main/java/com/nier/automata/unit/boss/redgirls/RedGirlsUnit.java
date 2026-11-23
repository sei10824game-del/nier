package com.nier.automata.unit.boss.redgirls;

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
import net.minecraft.world.phys.Vec3;

/**
 * The Red Girls - twin avatars of the machine network, fought as one unit. Alternating crimson volleys, glyph
 * lattice beams, hands erupting from the ground, twin blink strikes. At 50% HP: Network Manifest -> form 1 adding
 * sweeping network tendrils and converging homing orbs.
 */
public class RedGirlsUnit extends BossUnit {
    public static final int VOLLEY = 1, LATTICE = 2, HANDS = 3, BLINK = 4, MANIFEST = 5, TENDRILS = 6, CONVERGE = 7;
    static final int CRIMSON = 0xff1830, BLACK = 0x200008;
    private final List<Vec3> marks = new ArrayList<>();
    private Vec3 center = Vec3.ZERO;

    public RedGirlsUnit(EntityType<? extends RedGirlsUnit> t, Level l) { super(t, l); setNoGravity(true); }

    @Override public double preferredRange() { return 7; }
    /** Hovers about a block above its target's feet (or the ground when idle). */
    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        double want = getTarget() != null ? getTarget().getY() + 1.0 : getY() - (onGround() ? 0 : 0.5);
        setDeltaMovement(getDeltaMovement().multiply(1, 0.8, 1).add(0, Math.max(-0.04, Math.min(0.04, (want - getY()) * 0.05)), 0));
    }

    /** Position of twin a (left) or b (right). */
    private Vec3 twin(boolean a) { return BossFx.at(this, 0.3, 1.4).add(BossFx.fwd(this).yRot((float) Math.PI / 2).scale(a ? 0.6 : -0.6)); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(VOLLEY, "Crimson Volley", 30, 40, 3, 26, (u, t, k) -> {
                RedGirlsUnit r = (RedGirlsUnit) u;
                if (k == 10) BossFx.orbFan(u, r.twin(true), t, 5, 40, 0.5, 0.5f, CRIMSON);
                if (k == 18) BossFx.orbFan(u, r.twin(false), t, 6, 60, 0.45, 0.5f, CRIMSON);
                if (k == 10 || k == 18) BossFx.sound(u, SoundEvents.AMETHYST_BLOCK_CHIME, 2, 0.5f);
            }).weight(4),
            new Skill(LATTICE, "Glyph Lattice", 60, 120, 0, 20, (u, t, k) -> {
                if (k == 8 && t != null) center = t.position();
                if (k >= 8 && k < 20 && k % 3 == 2)
                    for (int i = -1; i <= 1; i++) {
                        for (double d = -6; d <= 6; d += 1.5) {
                            BossFx.puff(u, center.add(i * 3, 0.1, d), BossFx.dust(CRIMSON, 1f), 1, 0);
                            BossFx.puff(u, center.add(d, 0.1, i * 3), BossFx.dust(CRIMSON, 1f), 1, 0);
                        }
                    }
                if (k >= 20 && k <= 44) {
                    float m = k % 2 == 0 ? 0.35f : 0f;
                    boolean xs = (k / 8) % 2 == 0;   // lattice alternates direction every 8 ticks
                    for (int i = -1; i <= 1; i++) {
                        if (xs) BossFx.beam(u, center.add(i * 3, 0.8, -7), new Vec3(0, 0, 1), 14, 0.5, m, BossFx.dust(CRIMSON, 1.6f));
                        else BossFx.beam(u, center.add(-7, 0.8, i * 3), new Vec3(1, 0, 0), 14, 0.5, m, BossFx.dust(CRIMSON, 1.6f));
                    }
                    if (k % 8 == 4) BossFx.sound(u, SoundEvents.BEACON_POWER_SELECT, 2, 1.8f);
                }
            }).weight(2),
            new Skill(HANDS, "Grasping Hands", 40, 90, 0, 18, (u, t, k) -> {
                if (k == 8 && t != null) {
                    marks.clear(); marks.add(t.position());
                    Vec3 v = t.getDeltaMovement(); marks.add(t.position().add(v.x * 15, 0, v.z * 15));
                    marks.add(t.position().add(new Vec3(2.5, 0, 0).yRot(u.getRandom().nextFloat() * 6.28f)));
                }
                if (k >= 8 && k < 20 && k % 3 == 2) for (Vec3 m : marks) BossFx.warn(u, m, 1.4, CRIMSON);
                if (k == 20) { for (Vec3 m : marks) BossFx.pillar(u, m, 1.4, 1.4f, BossFx.dust(BLACK, 2.5f)); BossFx.sound(u, SoundEvents.EVOKER_FANGS_ATTACK, 2, 0.5f); }
            }).weight(3),
            new Skill(BLINK, "Twin Blink", 30, 70, 0, 16, (u, t, k) -> {
                if (k == 6) BossFx.blink(u, t, 1.8, 90, CRIMSON);
                if (k == 12 || k == 22) { BossFx.arc(u, 2.8, 80, 1.2f, 1.0); BossFx.sound(u, SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.5f); }
                if (k == 18) BossFx.blink(u, t, 1.8, -90, CRIMSON);
            }).weight(3),
            phase(0.5f, 1, new Skill(MANIFEST, "Network Manifest", 60, 0, 0, 99, (u, t, k) -> {
                if (k % 3 == 0) BossFx.ring(u, u.position().add(0, 0.2, 0), 1 + k * 0.1, BossFx.dust(CRIMSON, 1.5f), 18);
                if (k % 5 == 0) BossFx.puff(u, u.position().add(0, 1.5, 0), ParticleTypes.REVERSE_PORTAL, 20, 1);
                if (k == 40) { u.setForm(1); BossFx.blast(u, u.position(), 7, 1.2f, 3.0, false); BossFx.sound(u, SoundEvents.WITHER_SPAWN, 1, 1.8f); }
            })),
            new Skill(TENDRILS, "Network Tendrils", 50, 100, 0, 14, (u, t, k) -> {
                RedGirlsUnit r = (RedGirlsUnit) u;
                if (k >= 16 && k <= 40) {
                    double a = Math.toRadians(-70 + (k - 16) * 140.0 / 24);
                    float m = k % 2 == 0 ? 0.3f : 0f;
                    BossFx.beam(u, r.twin(true), BossFx.fwd(u).yRot((float) a), 13, 0.6, m, BossFx.dust(CRIMSON, 2f));
                    BossFx.beam(u, r.twin(false), BossFx.fwd(u).yRot((float) -a), 13, 0.6, m, BossFx.dust(BLACK, 2f));
                    if (k % 6 == 0) BossFx.sound(u, SoundEvents.BEACON_AMBIENT, 2, 2f);
                }
            }).forms(1, 1).weight(3).noTurn(),
            new Skill(CONVERGE, "Convergence", 50, 110, 0, 24, (u, t, k) -> {
                if (k == 14 && t != null) {
                    for (int i = 0; i < 12; i++) {
                        Vec3 dir = new Vec3(Math.cos(i * Math.PI / 6), 0, Math.sin(i * Math.PI / 6));
                        BossFx.orb(u, t.position().add(0, 1, 0).add(dir.scale(7)), dir.scale(0.22), 0.6f, i % 2 == 0 ? CRIMSON : 0xffffff, 0.4f, BossOrb.HOMING, 120);
                    }
                    BossFx.sound(u, SoundEvents.ILLUSIONER_CAST_SPELL, 2, 0.6f);
                }
            }).forms(1, 1).weight(2)
        );
    }
}
