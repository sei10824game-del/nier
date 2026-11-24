package com.nier.automata.unit.machine;

import com.nier.automata.core.Faction;

/** Data describing one spawnable machine (one entity id). Several ids share one {@link Arch}. */
public record MachineVariant(String id, String name, Arch arch, Size size, Weapon weapon, Skin skin,
                             float scale, float width, float height, double hp, double damage, double speed, double armor) {
    public enum Arch { STUBBY, BIPED, FLYER, GOLIATH, TANK, SPHERE, MULTILEG, QUAD, MONSTER, EXPLODER, SHADE }
    public enum Size { SMALL, MEDIUM, LARGE }
    public enum Weapon { NONE, AXE, SPEAR, HAMMER, STAFF, SHIELD, CLUB, MISSILE, DRILL, SAW, BOMB, LINK, GUN }
    public enum Skin {
        RUST(""), GOLD("_gold"), HACKED("_hacked"), RAMPAGE("_rampage"), KOSHI("_koshi"), ROSHI("_roshi"),
        BRONZE("_bronze"), BLOOD("_blood"), GRAVE("_grave"), CLOWN("_clown"), DYING("_dying"), MONSTER("_monster"), ANIMAL("_animal"), SAMURAI("_samurai"), FLIGHT_W("_flightw"), FLIGHT_B("_flightb"), BLUE("_blue"), PALE("_pale"), TIER("_tier"), REVERSE("_reverse"), SHADE("_shade"), WOLF("_wolf"), SCORPION("_scorpion"), SILVER("_silver"), ROC("_roc"), JACK("_jack"), LOUISE("_louise"), STONE("_stone"), WENDY("_wendy"), HOOK("_hook"), GEPPETTO("_geppetto");
        public final String suffix;
        Skin(String s) { suffix = s; }
    }
    public Faction faction() { return skin == Skin.HACKED || flight() ? Faction.ANDROID : Faction.MACHINE; }
    public boolean flight() { return skin == Skin.FLIGHT_W || skin == Skin.FLIGHT_B; }
    public boolean shade() { return skin == Skin.SHADE; }
    public boolean electric() { return skin == Skin.KOSHI || skin == Skin.ROSHI; }
    public boolean small() { return size == Size.SMALL; }
    public boolean large() { return size == Size.LARGE; }
}
