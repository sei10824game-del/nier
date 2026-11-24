package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Exploder: a hazard-striped block on two thin legs that sprints at its target, flashes, and detonates. */
public class ExploderUnit extends MachineUnit {
    public static final int DETONATE = 1;
    private Skill detonate;

    public ExploderUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 1.0; }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(detonate = new Skill(DETONATE, "Detonate", 44, 0, 0, 5 * s, (u, t, k) -> {
            if (k == 0) { setForm(1); sound(SoundEvents.CREEPER_PRIMED, 1f, 0.8f); }
            if (k < 24 && t != null) getNavigation().moveTo(t, 1.8);
            if (k >= 24) { getNavigation().stop(); }
            if (k % 3 == 0) com.nier.automata.core.Fx.burst(level(), at(0, 1.2 * s, 0), com.nier.automata.core.Fx.MACHINE, 2);
            if (k == 36) {
                area(position(), 4.0 * s, dmg(4.0), 1.6, true);
                com.nier.automata.core.Fx.shock(level(), position(), 3.5 * s, com.nier.automata.core.Fx.MACHINE_HOT);
                com.nier.automata.core.Fx.burst(level(), at(0, 1.0 * s, 0), com.nier.automata.core.Fx.MACHINE_HOT, 24);
                level().explode(this, getX(), getY() + 0.5, getZ(), 1.6f, Level.ExplosionInteraction.NONE);
                fx(ParticleTypes.EXPLOSION, at(0, 1.0 * s, 0), 3, 0.5 * s, 0); kill();
            }
        }).weight(1).noTarget().when(u -> false));
        return l;
    }

    /** Starts the fuse as soon as the target is near. */
    @Override @Nullable protected Skill forcedSkill() {
        var t = getTarget();
        return detonate != null && t != null && distanceTo(t) < 3.2 * v.scale() ? detonate : null;
    }
}
