package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.ExploderUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ExploderModel extends NierModel<MachineUnit> {
    public ExploderModel(ModelPart root) {
        super(root);
        bind(MachineGeo.EXPLODER_PATHS);
        // Detonate: sprint-shake 0-24, swell and shudder 24-36, burst
        AnimClip d = AnimClip.of(44).blend(2);
        AnimClip.Bone b = d.bone("body").scale(0, 1, 1, 1).scale(24, 1.05f, 1.05f, 1.05f).scale(36, 1.5f, 1.5f, 1.5f).scale(44, 1.6f, 1.6f, 1.6f).rot(0, 0, 0, 0).rot(44, 0, 0, 0);
        for (int t = 1; t < 40; t++) b.pos(t, t % 2 == 0 ? 0.4f : -0.4f, 0, 0);
        d.bone("antenna").rot(0, 0, 0, 0).rot(24, 0.3f, 0, 0).rot(36, -0.4f, 0, 0).rot(44, 0, 0, 0);
        clip(DETONATE, d);
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        float c = ls * 1.1f;
        bone("legR").xRot = Mth.cos(c) * 0.9f * la; bone("legL").xRot = Mth.cos(c + Mth.PI) * 0.9f * la;
        bone("body").y += Mth.sin(age * 0.2f) * 0.2f;
        bone("antenna").zRot = Mth.sin(age * 0.3f) * 0.12f;
    }
}
