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
        ALL.addAll(List.of(new Yorha2bModule(), new Yorha9sModule(), new A2Module(),
            new YorhaSoldierModule(), new YorhaHeavyModule(), new Yorha2EModule(), new CommanderModule(), new Operator6OModule(), new Operator21OModule(),
            new ResistanceModule(), new AnemoneModule(), new JackassModule(), new PascalModule(), new DevolaModule(),
            new PopolaModule(), new EmilModule(), new EmilHackedModule(), new PodModule(),
            new AdamModule(), new EveModule(), new EngelsModule(), new MarxModule(), new HegelModule(),
            new GrunModule(), new SimoneModule(), new RedGirlsModule()));
        ALL.addAll(MachineModules.ALL);
    }
    private Modules() {}
}
