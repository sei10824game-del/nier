package com.nier.automata.unit.ally;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** "YoRHa Betrayers" targets 8B (sword), 22B (spear), 64B (heavy rifle): the soldier kit with a fixed loadout, armoured. */
public class YorhaBetrayerUnit extends YorhaSoldierUnit {
    private final int loadout;
    public YorhaBetrayerUnit(EntityType<? extends YorhaSoldierUnit> t, Level l, int loadout) { super(t, l); this.loadout = loadout; }
    @Override protected int variants() { return 1; }
    @Override public int getForm() { return loadout; }
}
