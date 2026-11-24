package com.nier.automata.unit.yorha;

import com.nier.automata.core.Faction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** 2B Copy: a virus-infected clone of the B android (always the Virtuous Contract + Treaty kit); hostile to every non-machine. */
public class Yorha2bCopyUnit extends Yorha2bUnit {
    public Yorha2bCopyUnit(EntityType<? extends Yorha2bUnit> t, Level l) { super(t, l); }
    @Override public Faction faction() { return Faction.MACHINE; }
}
