package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Medium quadruped: pounce, charging gore, tail-blade whip. */
public class QuadUnit extends MachineUnit {
    public static final int POUNCE = 1, CHARGE = 2, TAIL = 3;

    public QuadUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 2.0 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(POUNCE, "Pounce", 30, 50, 2, 9, (u, t, k) -> {
            if (k == 10) { leap(targetPos(t), 9, 0.5); sound(SoundEvents.RAVAGER_ROAR, 0.5f, 1.8f); }
            if (k >= 10 && k < 22) swing(1.8 * s, 120, dmg(1.4), 0.6, true);
            if (!landed && k >= 16 && onGround()) { landed = true; area(position(), 2.0 * s, dmg(1.0), 0.8, true); dust(position(), 12, 0.4); }
        }).weight(3));
        l.add(new Skill(CHARGE, "Gore Charge", 40, 70, 3, 14, (u, t, k) -> {
            if (k < 12 && k % 3 == 0) dust(at(-0.5, 0.1, 0), 3, 0.3);
            if (k >= 12 && k < 30) { dash(0.6); swing(1.7 * s, 110, dmg(1.5), 1.0, true); }
        }).weight(2));
        l.add(new Skill(TAIL, "Tail Blade", 26, 40, 0, 3.5 * s, (u, t, k) -> {
            if (k == 12) { sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.9f); swing(3.4 * s, 300, dmg(1.3), 0.8, true); }
            if (k >= 10 && k <= 15) fx(ParticleTypes.SWEEP_ATTACK, at(-1.5 * s, 0.8 * s, 0), 1, 0.4, 0);
        }).weight(2));
        return l;
    }
}
