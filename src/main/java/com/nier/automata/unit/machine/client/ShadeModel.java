package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.ShadeUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Shade: a black hunched body with two ringed eyes, long arms and stubby legs. */
public class ShadeModel extends NierModel<MachineUnit> {
    public ShadeModel(ModelPart root) {
        super(root);
        bind(MachineGeo.SHADE_PATHS);
        clip(LUNGE, AnimClip.of(26).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(7, 0.5f, 0, 0).rot(9, -0.4f, 0, 0, OUT).rot(16, -0.1f, 0, 0).rot(26, 0, 0, 0).pos(0, 0, 0, 0).pos(7, 0, 2, 1).pos(9, 0, 1, -3, OUT).pos(26, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(7, 0.8f, 0, 0.5f).rot(9, -2.2f, 0, 0.2f, OUT).rot(26, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(7, 0.8f, 0, -0.5f).rot(9, -2.2f, 0, -0.2f, OUT).rot(26, 0, 0, 0).done());
        clip(SWIPE, AnimClip.of(28).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(7, 0, 0.6f, 0).rot(9, 0, -0.6f, 0, OUT).rot(14, 0, -0.5f, 0).rot(16, 0, 0.6f, 0, OUT).rot(28, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(7, -2.0f, 0, -0.5f).rot(9, -0.5f, 0, 0.6f, OUT).rot(28, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(14, -2.0f, 0, 0.5f).rot(16, -0.5f, 0, -0.6f, OUT).rot(28, 0, 0, 0).done());
        clip(SLAM, AnimClip.of(36).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0).rot(16, 0.6f, 0, 0, IN).rot(26, 0.3f, 0, 0).rot(36, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(12, -3.0f, 0, 0.3f).rot(16, -0.3f, 0, 0, IN).rot(36, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(12, -3.0f, 0, -0.3f).rot(16, -0.3f, 0, 0, IN).rot(36, 0, 0, 0).done());
        clip(VOLLEY, AnimClip.of(40).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(14, -0.3f, 0, 0).rot(16, 0.2f, 0, 0, OUT).rot(40, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(14, -2.6f, 0, 0.4f).rot(28, -2.2f, 0, 0.4f).rot(40, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(14, -2.6f, 0, -0.4f).rot(28, -2.2f, 0, -0.4f).rot(40, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        float c = ls * 0.9f;
        bone("legR").xRot = Mth.cos(c) * 0.8f * la; bone("legL").xRot = Mth.cos(c + Mth.PI) * 0.8f * la;
        bone("armR").xRot = Mth.cos(c + Mth.PI) * 0.5f * la + Mth.sin(age * 0.1f) * 0.05f; bone("armL").xRot = Mth.cos(c) * 0.5f * la - Mth.sin(age * 0.1f) * 0.05f;
        bone("body").y += Mth.sin(age * 0.14f) * 0.3f;
        bone("body").yRot = headYaw * Mth.DEG_TO_RAD * 0.4f;
        bone("crest").visible = e.v.size() != com.nier.automata.unit.machine.MachineVariant.Size.SMALL;
    }
}
