package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Resistance soldier. Loadout (form): 0 = spear, 1 = rifle. Both carry a hand grenade. */
public class ResistanceUnit extends AllyUnit {
    public static final int THRUST = 1, SWEEP = 2, BURST = 3, GRENADE = 4;

    public ResistanceUnit(EntityType<? extends ResistanceUnit> t, Level l) { super(t, l); }
    @Override protected int variants() { return 2; }
    @Override public double preferredRange() { return getForm() == 1 ? 10 : 2.5; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(THRUST, "Spear Thrust", 20, 25, 0, 4, (u, t, i) -> {
                if (i == 10) lunge(0.4, 0);
                if (i == 12) { arc(4, 25, 6, 0.6, 0.05); sound(SoundEvents.TRIDENT_THROW, 0.7f, 1.2f); }
            }).forms(0, 0).weight(3),
            new Skill(SWEEP, "Spear Sweep", 22, 45, 0, 4, (u, t, i) -> {
                if (i == 12) { arc(4, 100, 5, 0.9, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.9f); }
            }).forms(0, 0),
            new Skill(BURST, "Rifle Fire", 32, 30, 2, 20, (u, t, i) -> {
                if (i >= 8 && i <= 26 && i % 3 == 2) { Vec3 m = at(0.9, 1.35, -0.3); shoot(m, aimAt(t, m).add(random.nextGaussian() * 0.04, random.nextGaussian() * 0.02, random.nextGaussian() * 0.04), 2.2)
                    .dmg(2.5f).trail(ParticleTypes.CRIT).life(15); fx(ParticleTypes.SMOKE, m, 1, 0.05, 0); sound(SoundEvents.CROSSBOW_SHOOT, 0.6f, 1.9f); }
            }).forms(1, 1).weight(3),
            new Skill(GRENADE, "Grenade Toss", 24, 120, 4, 14, (u, t, i) -> {
                if (i == 14 && t != null) { Vec3 h = at(0.3, 1.8, 0.3); double d = distanceTo(t);
                    shoot(h, t.position().subtract(h).normalize().add(0, 0.35, 0), Math.min(1.1, 0.35 + d * 0.06)).gravity(0.05).boom(2.5).dmg(7).trail(ParticleTypes.SMOKE).life(60).size(0.2);
                    sound(SoundEvents.SNOWBALL_THROW, 1, 0.7f); }
            })
        );
    }
}
