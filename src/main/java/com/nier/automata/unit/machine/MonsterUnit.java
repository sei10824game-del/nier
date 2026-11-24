package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Monster Type: hunched bipedal predator with a hinged jaw and claws - bite, claw rake, pounce. */
public class MonsterUnit extends MachineUnit {
    public static final int BITE = 1, CLAWS = 2, POUNCE = 3;

    public MonsterUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 2.2 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(BITE, "Crushing Bite", 30, 50, 0, 3.6 * s, (u, t, k) -> {
            if (k == 12) { dash(0.5); sound(SoundEvents.RAVAGER_ATTACK, 1f, 0.8f); }
            if (k == 14) { swing(3.4 * s, 90, dmg(1.9), 0.9, true); com.nier.automata.core.Fx.burst(level(), at(2.2 * s, 1.2 * s, 0), com.nier.automata.core.Fx.MACHINE, 6); }
        }).weight(3));
        l.add(new Skill(CLAWS, "Claw Rake", 32, 40, 0, 3.4 * s, (u, t, k) -> {
            if (k == 9 || k == 17) { swing(3.2 * s, 130, dmg(1.3), 0.6, false); hit.clear(); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.8f); }
        }).weight(3));
        l.add(new Skill(POUNCE, "Pounce", 38, 80, 3 * s, 10 * s, (u, t, k) -> {
            if (k == 12) { leap(targetPos(t), 12, 0.65); sound(SoundEvents.RAVAGER_ROAR, 0.8f, 1.1f); }
            if (k >= 14 && k < 26) swing(2.6 * s, 160, dmg(1.2), 0.6, true);
            if (!landed && k >= 18 && onGround()) { landed = true; area(position(), 2.8 * s, dmg(1.6), 1.0, true); dust(position(), 20, 0.7 * s); com.nier.automata.core.Fx.shock(level(), position(), 2.5 * s, com.nier.automata.core.Fx.MACHINE_HOT); }
        }).weight(2));
        return l;
    }
}
