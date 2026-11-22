package com.nier.automata.unit.boss.marx;

import com.nier.automata.core.Skill;
import com.nier.automata.unit.boss.BossFx;
import com.nier.automata.unit.boss.BossUnit;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Marx - Goliath-class tank built from a bucket-wheel excavator (Abandoned Factory). Original pattern is slow and
 * predictable: a slow horizontal buzzsaw sweep, a forward saw thrust, and projectile fire while the foe is out of reach.
 */
public class MarxUnit extends BossUnit {
    public static final int SWEEP = 1, THRUST = 2, VOLLEY = 3;
    static final int PINK = 0xff4a90;

    public MarxUnit(EntityType<? extends MarxUnit> t, Level l) { super(t, l); }

    @Override public double preferredRange() { return 7; }
    @Override public boolean isPushable() { return false; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(SWEEP, "Buzzsaw Sweep", 60, 60, 0, 11, (u, t, k) -> {
                if (k >= 24 && k <= 44 && k % 5 == 4) {
                    BossFx.arc(u, 10, 100, 1.4f, 2.0);
                    BossFx.puff(u, BossFx.at(u, 7, 1).add(BossFx.fwd(u).yRot((float) Math.toRadians(70 - (k - 24) * 7)).scale(2)), ParticleTypes.CRIT, 20, 1.0);
                    BossFx.sound(u, SoundEvents.GRINDSTONE_USE, 2, 0.5f);
                }
            }).weight(3).noTurn(),
            new Skill(THRUST, "Saw Thrust", 40, 50, 0, 12, (u, t, k) -> {
                if (k >= 22 && k <= 30 && k % 4 == 2) {
                    BossFx.beam(u, BossFx.at(u, 1.5, 1.6), BossFx.fwd(u), 11, 1.5, 1.2f, ParticleTypes.CRIT);
                    BossFx.sound(u, SoundEvents.GRINDSTONE_USE, 2, 0.8f);
                }
            }).weight(3),
            new Skill(VOLLEY, "Cab Volley", 40, 70, 10, 32, (u, t, k) -> {
                if (k >= 12 && k <= 30 && k % 6 == 0) {
                    BossFx.orbFan(u, BossFx.at(u, -0.5, 4.2), t, 3, 30, 0.45, 0.6f, PINK);
                    BossFx.sound(u, SoundEvents.BLAZE_SHOOT, 1.5f, 0.7f);
                }
            }).weight(2)
        );
    }
}
