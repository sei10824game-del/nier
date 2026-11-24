package com.nier.automata.unit.machine;

import com.nier.automata.core.Fx;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Shade: the black ringed-eye creatures of Replicant. Lunge bite, double claw swipe, ground slam (large), dark orb volley (giant). */
public class ShadeUnit extends MachineUnit {
    public static final int LUNGE = 1, SWIPE = 2, SLAM = 3, VOLLEY = 4;

    public ShadeUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 1.6 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(LUNGE, "Shade Lunge", 26, 40, 2, 8 * Math.max(1, s * 0.7), (u, t, k) -> {
            if (k < 8 && k % 2 == 0) fx(ParticleTypes.SMOKE, at(0, 0.6 * s, 0), 3, 0.3 * s, 0.01);
            if (k == 8) { dash(0.7); sound(SoundEvents.PHANTOM_BITE, 0.7f, 0.6f); }
            if (k >= 8 && k < 15) swing(1.7 * s, 100, dmg(1.3), 0.6, true);
        }).weight(3));
        l.add(new Skill(SWIPE, "Claw Swipe", 28, 30, 0, 2.4 * s, (u, t, k) -> {
            if (k == 9 || k == 16) { swing(2.3 * s, 130, dmg(1.0), 0.5, false); hit.clear(); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 0.6f); }
        }).weight(3));
        if (v.size() == MachineVariant.Size.LARGE) l.add(new Skill(SLAM, "Dark Slam", 36, 70, 0, 4 * s, (u, t, k) -> {
            if (k == 16) { area(position(), 3.0 * s, dmg(1.6), 1.0, true); Fx.shock(level(), position(), 2.6 * s, 0x6020a0); dust(position(), 20, 0.6 * s); sound(SoundEvents.GENERIC_EXPLODE, 0.6f, 0.7f); }
        }).weight(2));
        if (v.weapon() == MachineVariant.Weapon.SPEAR) l.add(new Skill(VOLLEY, "Dark Blast", 40, 90, 3, 22, (u, t, k) -> {
            if (k < 14 && k % 3 == 0) Fx.burst(level(), at(0, 3.2 * s * 0.5, 0), 0x8040ff, 3);
            if (k == 16 || k == 22 || k == 28) { for (int i = -1; i <= 1; i++) shoot(at(0.8 * s, 2.4 * s, 0), targetPos(t).add(i * 1.6, 0, i * 0.6), 0.55, MachineShot.Kind.HARD_ORB, dmg(0.8)); sound(SoundEvents.EVOKER_CAST_SPELL, 0.8f, 0.7f); }
        }).weight(3));
        return l;
    }
}
