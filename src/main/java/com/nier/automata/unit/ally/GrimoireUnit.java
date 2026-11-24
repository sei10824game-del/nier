package com.nier.automata.unit.ally;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** The Grimoires (form 0 Weiss, 1 Noir, 2 Rubrum): floating tomes casting through the Pod kit (Dark Blast bolts, laser lance, missiles, hammer dive). */
public class GrimoireUnit extends PodUnit {
    public GrimoireUnit(EntityType<? extends PodUnit> t, Level l) { super(t, l); }
    @Override protected int variants() { return 3; }
}
