package com.nier.automata;

import com.nier.automata.core.UnitModule;
import com.nier.automata.unit.ally.*;
import com.nier.automata.unit.boss.adam.AdamModule;
import com.nier.automata.unit.boss.engels.EngelsModule;
import com.nier.automata.unit.boss.eve.EveModule;
import com.nier.automata.unit.boss.grun.GrunModule;
import com.nier.automata.unit.boss.hegel.HegelModule;
import com.nier.automata.unit.boss.marx.MarxModule;
import com.nier.automata.unit.boss.redgirls.RedGirlsModule;
import com.nier.automata.unit.boss.simone.SimoneModule;
import com.nier.automata.unit.machine.MachineModules;
import com.nier.automata.unit.yorha.*;
import java.util.ArrayList;
import java.util.List;

/** Central list of unit modules. Append new modules here. */
public final class Modules {
    public static final List<UnitModule<?>> ALL = new ArrayList<>();
    static {
        ALL.addAll(List.of(new Yorha2bModule(), new Yorha2bCopyModule(), new Yorha9sModule(), new A2Module(),
            new YorhaSoldierModule(), new YorhaHeavyModule(), new GrimoireModule(),
            new ReplicantModule("ally_nier_brother", "Nier (Brother)", 0, 90, 7, 0xe8e8ee, 0x303040), new ReplicantModule("ally_nier_father", "Nier (Father)", 1, 140, 12, 0xe8e8ee, 0x8a6a4a),
            new ReplicantModule("ally_kaine", "Kaine", 2, 100, 6, 0x3a4a78, 0xf0f0f4), new ReplicantModule("ally_fyra", "Fyra", 3, 80, 7, 0xeeeae0, 0xd6b050), new ReplicantModule("ally_yonah", "Yonah", 3, 60, 5, 0xece6dc, 0x60442e), new ReplicantModule("ally_tyrann", "Tyrann", 2, 200, 10, 0x1c1828, 0x7846be), new ReplicantModule("ally_halua", "Halua", 3, 90, 8, 0x96beaa, 0xd2c8a0), new ReplicantModule("ally_king_of_facade", "King of Facade", 6, 300, 10, 0x1c1a22, 0xd6b050), new ReplicantModule("ally_shadowlord_brother", "Shadowlord (Brother)", 4, 360, 12, 0x3a423a, 0x9aa09a), new ReplicantModule("ally_shadowlord_father", "Shadowlord (Father)", 5, 360, 12, 0x3a423a, 0xb09070),
            new ReplicantModule("ally_emil_replicant", "Emil (Replicant)", 3, 70, 8, 0x6e4696, 0xd2aa5a),
            new YorhaBetrayerModule("ally_yorha_8b", "YoRHa 8B", 0, 0xe8e8f0), new YorhaBetrayerModule("ally_yorha_22b", "YoRHa 22B", 1, 0xc4606a), new YorhaBetrayerModule("ally_yorha_64b", "YoRHa 64B", 2, 0x7a5842), new Yorha2EModule(), new CommanderModule(), new Operator6OModule(), new Operator21OModule(),
            new ResistanceModule(), new AnemoneModule(), new JackassModule(), new PascalModule(), new DevolaModule(),
            new PopolaModule(), new EmilModule(), new EmilHackedModule(), new PodModule(),
            new AdamModule(), new EveModule(), new EngelsModule(), new MarxModule(), new HegelModule(),
            new GrunModule(), new SimoneModule(), new RedGirlsModule()));
        ALL.addAll(MachineModules.ALL);
    }
    private Modules() {}
}
