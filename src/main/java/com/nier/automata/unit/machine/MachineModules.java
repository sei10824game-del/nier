package com.nier.automata.unit.machine;

import static com.nier.automata.unit.machine.MachineVariant.Arch.*;
import static com.nier.automata.unit.machine.MachineVariant.Size.*;
import static com.nier.automata.unit.machine.MachineVariant.Skin.*;
import static com.nier.automata.unit.machine.MachineVariant.Skin.SHADE;
import static com.nier.automata.unit.machine.MachineVariant.Weapon.*;

import com.nier.automata.core.UnitModule;
import java.util.List;

/** Every ordinary machine-lifeform entity. Add {@link #ALL} to Modules.ALL. */
public final class MachineModules {
    private MachineModules() {}
    private static MachineModule m(String id, String name, MachineVariant.Arch a, MachineVariant.Size sz, MachineVariant.Weapon w,
                                   MachineVariant.Skin sk, float scale, float wd, float h, double hp, double dmg, double speed, double armor) {
        return new MachineModule(new MachineVariant(id, name, a, sz, w, sk, scale, wd, h, hp, dmg, speed, armor));
    }

    public static final MachineModule
        SMALL_STUBBY = m("machine_small_stubby", "Small Stubby", STUBBY, SMALL, NONE, RUST, 1f, 0.8f, 1.3f, 20, 4, 0.25, 2),
        SMALL_STUBBY_AXE = m("machine_small_stubby_axe", "Small Stubby (Axe)", STUBBY, SMALL, AXE, RUST, 1f, 0.8f, 1.3f, 24, 5, 0.25, 3),
        SMALL_STUBBY_SPEAR = m("machine_small_stubby_spear", "Small Stubby (Spear)", STUBBY, SMALL, SPEAR, RUST, 1f, 0.8f, 1.3f, 24, 5, 0.25, 3),
        SMALL_STUBBY_HAMMER = m("machine_small_stubby_hammer", "Small Stubby (Hammer)", STUBBY, SMALL, HAMMER, RUST, 1f, 0.8f, 1.3f, 24, 5, 0.24, 3),
        SMALL_STUBBY_STAFF = m("machine_small_stubby_staff", "Small Stubby (Staff)", STUBBY, SMALL, STAFF, RUST, 1f, 0.8f, 1.3f, 22, 5, 0.25, 2),
        SMALL_STUBBY_SHIELD = m("machine_small_stubby_shield", "Small Stubby (Shield)", STUBBY, SMALL, SHIELD, RUST, 1f, 0.8f, 1.3f, 26, 4, 0.23, 4),
        GOLD_STUBBY_AXE = m("machine_gold_stubby_axe", "Gold Stubby (Axe)", STUBBY, SMALL, AXE, GOLD, 1.05f, 0.85f, 1.4f, 60, 7, 0.27, 8),
        GOLD_STUBBY_SPEAR = m("machine_gold_stubby_spear", "Gold Stubby (Spear)", STUBBY, SMALL, SPEAR, GOLD, 1.05f, 0.85f, 1.4f, 60, 7, 0.27, 8),
        GOLD_STUBBY_HAMMER = m("machine_gold_stubby_hammer", "Gold Stubby (Hammer)", STUBBY, SMALL, HAMMER, GOLD, 1.05f, 0.85f, 1.4f, 60, 7, 0.26, 8),
        GOLD_STUBBY_STAFF = m("machine_gold_stubby_staff", "Gold Stubby (Staff)", STUBBY, SMALL, STAFF, GOLD, 1.05f, 0.85f, 1.4f, 55, 7, 0.27, 8),
        MEDIUM_STUBBY = m("machine_medium_stubby", "Medium Stubby", STUBBY, MEDIUM, NONE, RUST, 1.8f, 1.4f, 2.3f, 60, 8, 0.24, 6),
        LARGE_STUBBY = m("machine_large_stubby", "Large Stubby", STUBBY, LARGE, NONE, RUST, 3f, 2.4f, 3.9f, 150, 12, 0.22, 10),
        HACKED_STUBBY = m("machine_hacked_stubby", "Hacked Small Stubby", STUBBY, SMALL, NONE, HACKED, 1f, 0.8f, 1.3f, 30, 5, 0.27, 3),
        SMALL_BIPED = m("machine_small_biped", "Small Biped", BIPED, SMALL, NONE, RUST, 0.8f, 0.6f, 1.6f, 22, 4, 0.28, 2),
        MEDIUM_BIPED = m("machine_medium_biped", "Medium Biped", BIPED, MEDIUM, CLUB, RUST, 1.15f, 0.8f, 2.25f, 50, 8, 0.27, 5),
        MEDIUM_BIPED_SHIELD = m("machine_medium_biped_shield", "Medium Biped (Shield)", BIPED, MEDIUM, SHIELD, RUST, 1.15f, 0.8f, 2.25f, 55, 7, 0.25, 7),
        LARGE_BIPED = m("machine_large_biped", "Large Biped", BIPED, LARGE, NONE, RUST, 2.4f, 1.7f, 4.7f, 160, 13, 0.24, 8),
        HACKED_BIPED = m("machine_hacked_biped", "Hacked Medium Biped", BIPED, MEDIUM, CLUB, HACKED, 1.15f, 0.8f, 2.25f, 60, 8, 0.28, 5),
        SMALL_FLYER = m("machine_small_flyer", "Small Flyer", FLYER, SMALL, NONE, RUST, 0.9f, 0.9f, 1.1f, 14, 3, 0.3, 1),
        MEDIUM_FLYER = m("machine_medium_flyer", "Medium Flyer", FLYER, MEDIUM, NONE, RUST, 1.6f, 1.5f, 1.8f, 45, 6, 0.28, 4),
        MACHINE_FLYER = m("machine_flyer", "Machine Flyer", FLYER, SMALL, MISSILE, RUST, 1.2f, 1.1f, 1.3f, 30, 5, 0.3, 3),
        SMALL_GOLIATH = m("machine_small_goliath", "Small Goliath Biped", GOLIATH, SMALL, NONE, RUST, 2f, 2.4f, 4.1f, 200, 14, 0.22, 10),
        MEDIUM_GOLIATH = m("machine_medium_goliath", "Goliath Biped", GOLIATH, MEDIUM, NONE, RUST, 2.8f, 3.3f, 5.8f, 340, 18, 0.2, 12),
        LARGE_GOLIATH = m("machine_large_goliath", "Large Goliath Biped", GOLIATH, LARGE, NONE, RUST, 3.8f, 4.5f, 7.8f, 520, 24, 0.18, 14),
        GOLIATH_TANK = m("machine_goliath_tank", "Goliath Tank", TANK, LARGE, NONE, RUST, 2.2f, 3.8f, 3f, 420, 20, 0.2, 14),
        MULTILEG_UNIT = m("machine_multileg", "Medium Multi-Leg", MachineVariant.Arch.MULTILEG, MEDIUM, NONE, RUST, 1.5f, 1.6f, 1.8f, 70, 8, 0.28, 4),
        DRILL_SPHERE = m("machine_drill_sphere", "Drill Sphere", SPHERE, SMALL, DRILL, RUST, 1.2f, 0.9f, 1.3f, 24, 6, 0.27, 3),
        SMALL_EXPLODER = m("machine_small_exploder", "Small Exploder", EXPLODER, SMALL, NONE, RUST, 1f, 0.8f, 1.5f, 14, 3, 0.34, 1),
        SAW_SPHERE = m("machine_saw_sphere", "Saw Sphere", SPHERE, SMALL, SAW, RUST, 1.2f, 0.9f, 1.3f, 24, 6, 0.27, 3),
        QUADRUPED = m("machine_medium_quadruped", "Medium Quadruped", MachineVariant.Arch.QUAD, MEDIUM, NONE, RUST, 1.5f, 1.5f, 1.5f, 60, 8, 0.34, 3),
        GOLD_GOLIATH = m("machine_gold_goliath", "Gold Goliath Biped", GOLIATH, MEDIUM, NONE, GOLD, 2.4f, 2.6f, 4.2f, 520, 22, 0.24, 14),
        GOLD_TANK = m("machine_gold_tank", "Gold Tank", TANK, LARGE, NONE, GOLD, 2.2f, 3.8f, 3f, 760, 26, 0.2, 18),
        MEDIUM_EXPLODER = m("machine_medium_exploder", "Medium Exploder", EXPLODER, MEDIUM, NONE, RUST, 1.8f, 1.4f, 2.5f, 40, 6, 0.3, 2),
        GOLIATH_FLYER = m("machine_goliath_flyer", "Goliath Flyer", FLYER, LARGE, MISSILE, RUST, 2.6f, 2.6f, 2.4f, 260, 16, 0.3, 10),
        RAMPAGING_SMALL_STUBBY = m("machine_rampaging_small_stubby", "Rampaging Small Stubby", STUBBY, SMALL, NONE, RAMPAGE, 1f, 0.8f, 1.3f, 30, 7, 0.4, 2),
        RAMPAGING_SMALL_BIPED = m("machine_rampaging_small_biped", "Rampaging Small Biped", BIPED, SMALL, NONE, RAMPAGE, 0.8f, 0.6f, 1.6f, 40, 8, 0.38, 2),
        RAMPAGING_MEDIUM_BIPED = m("machine_rampaging_medium_biped", "Rampaging Medium Biped", BIPED, MEDIUM, CLUB, RAMPAGE, 1.15f, 0.8f, 2.25f, 110, 12, 0.34, 6),
        LINKED_SPHERE = m("machine_linked_sphere", "Linked-sphere Type", SPHERE, MEDIUM, LINK, RUST, 1.5f, 1.2f, 1.6f, 80, 10, 0.3, 5),
        KO_SHI = m("machine_ko_shi", "Ko-Shi", BIPED, LARGE, NONE, KOSHI, 2.4f, 1.7f, 4.7f, 320, 14, 0.27, 8),
        RO_SHI = m("machine_ro_shi", "Ro-Shi", BIPED, LARGE, NONE, ROSHI, 2.4f, 1.7f, 4.7f, 320, 14, 0.27, 8),
        GUNMAN_STUBBY = m("machine_gunman_stubby", "Gunman Stubby", STUBBY, SMALL, GUN, RUST, 1f, 0.8f, 1.5f, 30, 6, 0.26, 3),
        GUNMAN_BIPED = m("machine_gunman_biped", "Gunman Biped", BIPED, MEDIUM, GUN, RUST, 1.15f, 0.8f, 2.4f, 55, 8, 0.27, 5),
        GUNMAN_FLYER = m("machine_gunman_flyer", "Gunman Flyer", FLYER, SMALL, GUN, RUST, 1.2f, 1.1f, 1.5f, 34, 6, 0.3, 3),
        DYING_GOLIATH = m("machine_dying_goliath", "Dying Goliath Biped", GOLIATH, MEDIUM, NONE, DYING, 2.4f, 2.6f, 4.2f, 260, 18, 0.16, 8),
        MONSTER_TYPE = m("machine_monster_type", "Monster Type", MachineVariant.Arch.MONSTER, LARGE, NONE, MachineVariant.Skin.MONSTER, 2.2f, 2.2f, 2.2f, 160, 14, 0.34, 6),
        ANIMAL_MACHINE = m("machine_animal_machine", "Animal Machine", QUAD, MEDIUM, NONE, ANIMAL, 1.5f, 1.5f, 1.6f, 60, 8, 0.34, 3),
        PARK_RABBIT = m("machine_park_rabbit", "Amusement Park Rabbit", STUBBY, SMALL, NONE, BRONZE, 1.1f, 0.9f, 1.5f, 40, 5, 0.25, 4),
        BLOOD_DRENCHED = m("machine_blood_drenched", "Blood-drenched Machine", STUBBY, SMALL, AXE, BLOOD, 1f, 0.8f, 1.3f, 40, 8, 0.3, 2),
        COURAGEOUS_BROTHER = m("machine_courageous_brother", "Courageous Brother", STUBBY, SMALL, NONE, GOLD, 1f, 0.8f, 1.3f, 40, 6, 0.28, 4),
        HATEFUL_SISTER = m("machine_hateful_sister", "Hateful Sister", STUBBY, SMALL, NONE, GOLD, 1f, 0.8f, 1.3f, 40, 6, 0.28, 4),
        VENGEFUL_CHILD = m("machine_vengeful_child", "Vengeful Child", STUBBY, SMALL, NONE, GOLD, 0.8f, 0.7f, 1.1f, 30, 7, 0.32, 3),
        GRAVEKEEPER = m("machine_gravekeeper", "Gravekeeper", STUBBY, MEDIUM, STAFF, GRAVE, 1.8f, 1.4f, 2.6f, 120, 10, 0.22, 8),
        ZOMBIE_CLOWN = m("machine_zombie_clown", "Zombie Clown", BIPED, SMALL, NONE, CLOWN, 0.9f, 0.7f, 1.8f, 50, 7, 0.32, 3),
        MASAMUNE = m("machine_masamune", "Masamune", STUBBY, MEDIUM, AXE, SAMURAI, 1.8f, 1.4f, 2.6f, 200, 15, 0.27, 10),
        HO229_WHITE = m("machine_ho229_white", "Flight Unit Ho229 Type-B", FLYER, LARGE, MISSILE, FLIGHT_W, 2.4f, 2.4f, 2.2f, 300, 14, 0.34, 10),
        HO229_BLACK = m("machine_ho229_black", "Flight Unit Ho229 Type-B (Black)", FLYER, LARGE, MISSILE, FLIGHT_B, 2.4f, 2.4f, 2.2f, 300, 14, 0.34, 10),
        HIGH_SPEED = m("machine_high_speed", "High-speed Machine", SPHERE, SMALL, NONE, BLUE, 1.3f, 1.2f, 1.4f, 30, 5, 0.5, 3),
        EMIL_CLONE = m("machine_emil_clone", "Emil Clones", SPHERE, MEDIUM, LINK, PALE, 1.5f, 1.2f, 1.6f, 90, 10, 0.3, 5),
        MULTI_TIER = m("machine_multi_tier", "Multi-tier Type", STUBBY, SMALL, NONE, TIER, 1.1f, 0.9f, 1.5f, 36, 6, 0.26, 4),
        MULTI_TIER_VILLAGE = m("machine_multi_tier_village", "Multi-tier Type (Village)", STUBBY, SMALL, NONE, TIER, 0.8f, 0.7f, 1.1f, 20, 3, 0.24, 1),
        REVERSE_GOLIATH = m("machine_reverse_goliath", "Reverse-jointed Goliath", GOLIATH, MEDIUM, NONE, REVERSE, 2.2f, 2.6f, 4f, 300, 18, 0.26, 12),
        SHADE_SMALL = m("machine_shade_small", "Shade", MachineVariant.Arch.SHADE, SMALL, NONE, MachineVariant.Skin.SHADE, 0.8f, 0.7f, 1.2f, 16, 4, 0.3, 0),
        SHADE_MEDIUM = m("machine_shade_medium", "Medium Shade", MachineVariant.Arch.SHADE, MEDIUM, NONE, MachineVariant.Skin.SHADE, 1.3f, 1.1f, 1.9f, 45, 7, 0.28, 3),
        SHADE_LARGE = m("machine_shade_large", "Large Shade", MachineVariant.Arch.SHADE, LARGE, NONE, MachineVariant.Skin.SHADE, 2.2f, 1.9f, 3.2f, 130, 12, 0.26, 6),
        SHADE_GIANT = m("machine_shade_giant", "Giant Shade", MachineVariant.Arch.SHADE, LARGE, SPEAR, MachineVariant.Skin.SHADE, 3.4f, 2.8f, 4.8f, 320, 20, 0.24, 10),
        SHADE_BEAST = m("machine_shade_beast", "Shade Beast", MachineVariant.Arch.QUAD, MEDIUM, NONE, MachineVariant.Skin.SHADE, 1.5f, 1.5f, 1.5f, 60, 9, 0.36, 3),
        SHADE_BEETLE = m("machine_shade_beetle", "Shade Beetle", MachineVariant.Arch.MULTILEG, MEDIUM, NONE, MachineVariant.Skin.SHADE, 1.8f, 1.9f, 2f, 90, 9, 0.28, 5),
        SHADE_FLYER = m("machine_shade_flyer", "Shade Flyer", FLYER, MEDIUM, NONE, MachineVariant.Skin.SHADE, 1.5f, 1.5f, 1.8f, 40, 6, 0.3, 2),
        SHADE_MULTI_LIMB = m("machine_shade_multi_limb", "Multi-limb Shade", MachineVariant.Arch.MONSTER, LARGE, NONE, MachineVariant.Skin.SHADE, 2.4f, 2.4f, 2.4f, 220, 16, 0.3, 8),
        WOLF = m("machine_wolf", "Wolf", MachineVariant.Arch.QUAD, SMALL, NONE, MachineVariant.Skin.WOLF, 1.1f, 1f, 1.1f, 24, 5, 0.38, 0),
        SCORPION = m("machine_scorpion", "Scorpion", MachineVariant.Arch.QUAD, SMALL, NONE, MachineVariant.Skin.SCORPION, 1.0f, 1f, 0.8f, 20, 6, 0.3, 2),
        HANSEL = m("machine_hansel", "Hansel", BIPED, MEDIUM, SHIELD, MachineVariant.Skin.SILVER, 1.8f, 1.2f, 3.2f, 360, 14, 0.25, 14),
        GRETEL = m("machine_gretel", "Gretel", BIPED, MEDIUM, CLUB, MachineVariant.Skin.SILVER, 1.8f, 1.2f, 3.2f, 360, 16, 0.26, 12),
        ROC = m("machine_roc", "Roc", MachineVariant.Arch.QUAD, LARGE, NONE, MachineVariant.Skin.ROC, 2.8f, 2.8f, 2.8f, 420, 18, 0.36, 8),
        HOOK = m("machine_hook", "Hook", MachineVariant.Arch.MONSTER, LARGE, NONE, MachineVariant.Skin.HOOK, 3.2f, 3f, 3f, 520, 22, 0.28, 10),
        JACK = m("machine_jack", "Jack", MachineVariant.Arch.MONSTER, LARGE, NONE, MachineVariant.Skin.JACK, 4.2f, 3.8f, 4f, 900, 26, 0.22, 14),
        LOUISE = m("machine_louise", "Louise", MachineVariant.Arch.MULTILEG, LARGE, NONE, MachineVariant.Skin.LOUISE, 3.0f, 3f, 3.2f, 700, 20, 0.3, 10),
        WENDY = m("machine_wendy", "Wendy", SPHERE, LARGE, LINK, MachineVariant.Skin.WENDY, 3.0f, 2.6f, 3.4f, 600, 18, 0.26, 12),
        MOURNING_MOTHER = m("machine_mourning_mother", "Mourning Mother", MachineVariant.Arch.MONSTER, LARGE, NONE, MachineVariant.Skin.STONE, 5.2f, 4.2f, 5.2f, 1100, 28, 0.18, 16),
        SPHERE_UNIT = m("machine_sphere", "Sphere", SPHERE, SMALL, NONE, RUST, 1.2f, 0.9f, 0.9f, 18, 4, 0.27, 2),
        HACKED_SPHERE = m("machine_hacked_sphere", "Hacked Sphere", SPHERE, SMALL, NONE, HACKED, 1.2f, 0.9f, 0.9f, 26, 5, 0.28, 3);

    public static final List<UnitModule<?>> ALL = List.of(SMALL_STUBBY, SMALL_STUBBY_AXE, SMALL_STUBBY_SPEAR, SMALL_STUBBY_HAMMER,
            SMALL_STUBBY_STAFF, SMALL_STUBBY_SHIELD, GOLD_STUBBY_AXE, GOLD_STUBBY_SPEAR, GOLD_STUBBY_HAMMER, GOLD_STUBBY_STAFF,
            MEDIUM_STUBBY, LARGE_STUBBY, HACKED_STUBBY, SMALL_BIPED, MEDIUM_BIPED, MEDIUM_BIPED_SHIELD, LARGE_BIPED, HACKED_BIPED,
            SMALL_FLYER, MEDIUM_FLYER, MACHINE_FLYER, SMALL_GOLIATH, MEDIUM_GOLIATH, LARGE_GOLIATH, GOLIATH_TANK, SPHERE_UNIT, HACKED_SPHERE, MULTILEG_UNIT, DRILL_SPHERE, SMALL_EXPLODER, SAW_SPHERE, QUADRUPED, GOLD_GOLIATH, GOLD_TANK, MEDIUM_EXPLODER, GOLIATH_FLYER, RAMPAGING_SMALL_STUBBY, RAMPAGING_SMALL_BIPED, RAMPAGING_MEDIUM_BIPED, LINKED_SPHERE, KO_SHI, RO_SHI,
            GUNMAN_STUBBY, GUNMAN_BIPED, GUNMAN_FLYER, DYING_GOLIATH, MONSTER_TYPE, ANIMAL_MACHINE, PARK_RABBIT, BLOOD_DRENCHED,
            COURAGEOUS_BROTHER, HATEFUL_SISTER, VENGEFUL_CHILD, GRAVEKEEPER, ZOMBIE_CLOWN, MASAMUNE, HO229_WHITE, HO229_BLACK, HIGH_SPEED, EMIL_CLONE, MULTI_TIER, MULTI_TIER_VILLAGE, REVERSE_GOLIATH,
            SHADE_SMALL, SHADE_MEDIUM, SHADE_LARGE, SHADE_GIANT, SHADE_BEAST, SHADE_BEETLE, SHADE_FLYER, SHADE_MULTI_LIMB, WOLF, SCORPION, HANSEL, GRETEL, ROC, HOOK, JACK, LOUISE, WENDY, MOURNING_MOTHER);
}
