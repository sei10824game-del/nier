package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Medium multi-leg (spider) machine: leg stab, panel sweep, spinning panel blades. */
public class MultiLegUnit extends MachineUnit {
    public static final int STAB = 1, SWEEP = 2, WHIRL = 3;

    public MultiLegUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 2.0 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(STAB, "Leg Stab", 28, 40, 0, 3.2 * s, (u, t, k) -> {
            if (k == 12) { sound(SoundEvents.PLAYER_ATTACK_STRONG, 0.8f, 1.3f); swing(3.0 * s, 70, dmg(1.3), 0.5, true); dash(0.3); }
            if (k == 15) swing(3.0 * s, 70, dmg(1.1), 0.3, true);
            if (k > 11 && k < 17) fx(ParticleTypes.CRIT, at(2.2 * s, 0.6 * s, 0), 2, 0.3, 0.1);
        }).weight(3));
        l.add(new Skill(SWEEP, "Panel Sweep", 34, 50, 0, 3.4 * s, (u, t, k) -> {
            if (k == 14) { sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f); swing(3.4 * s, 170, dmg(1.6), 1.0, true); }
            if (k >= 12 && k <= 18) fx(ParticleTypes.SWEEP_ATTACK, at(2.0 * s, 0.9 * s, 0), 1, 0.3, 0);
        }).weight(3));
        l.add(new Skill(WHIRL, "Blade Whirl", 50, 90, 0, 6 * s, (u, t, k) -> {
            if (k >= 10 && k < 42) { if (t != null) dash(0.2); if (k % 5 == 0) { area(position(), 3.2 * s, dmg(0.9), 0.7, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 1.4f); } }
            if (k % 4 == 0 && k >= 10 && k < 42) fx(ParticleTypes.CRIT, at(0, 0.9 * s, 0), 4, 1.5 * s, 0.1);
        }).weight(2));
        return l;
    }
}
