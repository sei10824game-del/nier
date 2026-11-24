package com.nier.automata.unit.machine;

import static com.nier.automata.unit.machine.MachineVariant.Arch.*;
import static com.nier.automata.unit.machine.MachineVariant.Size.*;
import static com.nier.automata.unit.machine.MachineVariant.Skin.*;
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
        SMALL_EXPLODER = m("machine_small_exploder", "Small Exploder", STUBBY, SMALL, BOMB, RUST, 1f, 0.8f, 1.5f, 14, 3, 0.34, 1),
        SAW_SPHERE = m("machine_saw_sphere", "Saw Sphere", SPHERE, SMALL, SAW, RUST, 1.2f, 0.9f, 1.3f, 24, 6, 0.27, 3),
        QUADRUPED = m("machine_medium_quadruped", "Medium Quadruped", MachineVariant.Arch.QUAD, MEDIUM, NONE, RUST, 1.5f, 1.5f, 1.5f, 60, 8, 0.34, 3),
        GOLD_GOLIATH = m("machine_gold_goliath", "Gold Goliath Biped", GOLIATH, MEDIUM, NONE, GOLD, 2.4f, 2.6f, 4.2f, 520, 22, 0.24, 14),
        GOLD_TANK = m("machine_gold_tank", "Gold Tank", TANK, LARGE, NONE, GOLD, 2.2f, 3.8f, 3f, 760, 26, 0.2, 18),
        MEDIUM_EXPLODER = m("machine_medium_exploder", "Medium Exploder", STUBBY, MEDIUM, BOMB, RUST, 1.8f, 1.4f, 2.5f, 40, 6, 0.3, 2),
        GOLIATH_FLYER = m("machine_goliath_flyer", "Goliath Flyer", FLYER, LARGE, MISSILE, RUST, 2.6f, 2.6f, 2.4f, 260, 16, 0.3, 10),
        RAMPAGING_SMALL_STUBBY = m("machine_rampaging_small_stubby", "Rampaging Small Stubby", STUBBY, SMALL, NONE, RAMPAGE, 1f, 0.8f, 1.3f, 30, 7, 0.4, 2),
        RAMPAGING_SMALL_BIPED = m("machine_rampaging_small_biped", "Rampaging Small Biped", BIPED, SMALL, NONE, RAMPAGE, 0.8f, 0.6f, 1.6f, 40, 8, 0.38, 2),
        RAMPAGING_MEDIUM_BIPED = m("machine_rampaging_medium_biped", "Rampaging Medium Biped", BIPED, MEDIUM, CLUB, RAMPAGE, 1.15f, 0.8f, 2.25f, 110, 12, 0.34, 6),
        LINKED_SPHERE = m("machine_linked_sphere", "Linked-sphere Type", SPHERE, MEDIUM, LINK, RUST, 1.5f, 1.2f, 1.6f, 80, 10, 0.3, 5),
        KO_SHI = m("machine_ko_shi", "Ko-Shi", BIPED, LARGE, NONE, KOSHI, 2.4f, 1.7f, 4.7f, 320, 14, 0.27, 8),
        RO_SHI = m("machine_ro_shi", "Ro-Shi", BIPED, LARGE, NONE, ROSHI, 2.4f, 1.7f, 4.7f, 320, 14, 0.27, 8),
        SPHERE_UNIT = m("machine_sphere", "Sphere", SPHERE, SMALL, NONE, RUST, 1.2f, 0.9f, 0.9f, 18, 4, 0.27, 2),
        HACKED_SPHERE = m("machine_hacked_sphere", "Hacked Sphere", SPHERE, SMALL, NONE, HACKED, 1.2f, 0.9f, 0.9f, 26, 5, 0.28, 3);

    public static final List<UnitModule<?>> ALL = List.of(SMALL_STUBBY, SMALL_STUBBY_AXE, SMALL_STUBBY_SPEAR, SMALL_STUBBY_HAMMER,
            SMALL_STUBBY_STAFF, SMALL_STUBBY_SHIELD, GOLD_STUBBY_AXE, GOLD_STUBBY_SPEAR, GOLD_STUBBY_HAMMER, GOLD_STUBBY_STAFF,
            MEDIUM_STUBBY, LARGE_STUBBY, HACKED_STUBBY, SMALL_BIPED, MEDIUM_BIPED, MEDIUM_BIPED_SHIELD, LARGE_BIPED, HACKED_BIPED,
            SMALL_FLYER, MEDIUM_FLYER, MACHINE_FLYER, SMALL_GOLIATH, MEDIUM_GOLIATH, LARGE_GOLIATH, GOLIATH_TANK, SPHERE_UNIT, HACKED_SPHERE, MULTILEG_UNIT, DRILL_SPHERE, SMALL_EXPLODER, SAW_SPHERE, QUADRUPED, GOLD_GOLIATH, GOLD_TANK, MEDIUM_EXPLODER, GOLIATH_FLYER, RAMPAGING_SMALL_STUBBY, RAMPAGING_SMALL_BIPED, RAMPAGING_MEDIUM_BIPED, LINKED_SPHERE, KO_SHI, RO_SHI);
}
