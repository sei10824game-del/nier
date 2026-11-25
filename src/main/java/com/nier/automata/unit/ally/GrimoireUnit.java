package com.nier.automata.unit.ally;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** The Grimoires (form 0 Weiss, 1 Noir, 2 Rubrum): floating tomes casting through the Pod kit (Dark Blast bolts, laser lance, missiles, hammer dive). */
public class GrimoireUnit extends PodUnit {
    public GrimoireUnit(EntityType<? extends PodUnit> t, Level l) { super(t, l); }
    @Override protected int variants() { return 3; }

    /** With no target the tome trails the nearest Replicant human it can find. */
    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide || getTarget() != null || tickCount % 10 != 0) return;
        var owner = level().getEntitiesOfClass(ReplicantUnit.class, getBoundingBox().inflate(32)).stream().min(java.util.Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
        if (owner != null && distanceToSqr(owner) > 36) getNavigation().moveTo(owner, 1.1);
    }
}
