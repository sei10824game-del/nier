package com.nier.automata.unit.machine;

import com.nier.automata.core.Faction;

/** Data describing one spawnable machine (one entity id). Several ids share one {@link Arch}. */
public record MachineVariant(String id, String name, Arch arch, Size size, Weapon weapon, Skin skin,
                             float scale, float width, float height, double hp, double damage, double speed, double armor) {
    public enum Arch { STUBBY, BIPED, FLYER, GOLIATH, TANK, SPHERE, MULTILEG, QUAD }
    public enum Size { SMALL, MEDIUM, LARGE }
    public enum Weapon { NONE, AXE, SPEAR, HAMMER, STAFF, SHIELD, CLUB, MISSILE, DRILL, SAW, BOMB }
    public enum Skin {
        RUST(""), GOLD("_gold"), HACKED("_hacked"), RAMPAGE("_rampage");
        public final String suffix;
        Skin(String s) { suffix = s; }
    }
    public Faction faction() { return skin == Skin.HACKED ? Faction.ANDROID : Faction.MACHINE; }
    public boolean small() { return size == Size.SMALL; }
    public boolean large() { return size == Size.LARGE; }
}
