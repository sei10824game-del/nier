package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.MultiLegUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Eight-legged machine with two slab panels. Walk cycles the legs in alternating diagonal groups. */
public class MultiLegModel extends NierModel<MachineUnit> {
    private static final float PI = Mth.PI;

    public MultiLegModel(ModelPart root) {
        super(root);
        bind(MachineGeo.MULTILEG_PATHS);

        // Leg Stab: rear up on hind legs, front legs cock back 0-11, thrust 12, recover
        AnimClip st = AnimClip.of(28).blend(3);
        st.bone("body")
            .bone("body").rot(0, 0, 0, 0).rot(8, -0.35f, 0, 0).rot(12, 0.3f, 0, 0, OUT).rot(20, 0.1f, 0, 0).rot(28, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 1, 1).pos(12, 0, 1, -3, OUT).pos(28, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0).rot(12, -0.3f, 0, 0).rot(28, 0, 0, 0);
        for (String sd : new String[]{"R", "L"}) {
            float sg = sd.equals("R") ? 1 : -1;
            st.bone("leg" + sd + "0").rot(0, 0, 0, 0).rot(8, -1.2f, 0, 0.4f * sg).rot(12, 0.8f, 0, -0.3f * sg, OUT).rot(20, 0, 0, 0).rot(28, 0, 0, 0);
            st.bone("low" + sd + "0").rot(0, 0, 0, 0).rot(8, -0.8f, 0, 0).rot(12, 0.6f, 0, 0, OUT).rot(28, 0, 0, 0);
            st.bone("arm" + sd).rot(0, 0, 0, 0).rot(8, 0, 0, 0.5f * sg).rot(28, 0, 0, 0);
        }
        clip(STAB, st);

        // Panel Sweep: panels swing back 0-12, whip together across the front at 14
        clip(SWEEP, AnimClip.of(34).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(12, 0, 0.7f, 0).rot(14, 0, -0.5f, 0, OUT).rot(22, 0, -0.3f, 0).rot(34, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(12, 0, -0.4f, 0).rot(14, 0, 0.3f, 0).rot(34, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(12, 0, 1.6f, 0).rot(14, 0, -1.0f, 0, OUT).rot(24, 0, -0.5f, 0).rot(34, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(12, 0, 1.0f, 0).rot(14, 0, -1.6f, 0, OUT).rot(24, 0, -0.5f, 0).rot(34, 0, 0, 0)
            .bone("panelR").rot(0, 0, 0, 0).rot(12, 0, 0.4f, 0).rot(14, 0, -0.9f, 0, OUT).rot(34, 0, 0, 0)
            .bone("panelL").rot(0, 0, 0, 0).rot(12, 0, 0.9f, 0).rot(14, 0, -0.4f, 0, OUT).rot(34, 0, 0, 0).done());

        clip(10, AnimClip.of(36).blend(3)
            .bone("handR").rot(0, 0, 0, 0).rot(10, 0, 1.2f, 0).rot(12, 0, -1.2f, 0, OUT).rot(20, 0, 1.0f, 0).rot(24, 0, -1.0f, 0, OUT).rot(36, 0, 0, 0)
            .bone("handL").rot(0, 0, 0, 0).rot(10, 0, -1.2f, 0).rot(12, 0, 1.2f, 0, OUT).rot(20, 0, -1.0f, 0).rot(24, 0, 1.0f, 0, OUT).rot(36, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(10, 0, 0.6f, 0).rot(14, 0, -0.6f, 0).rot(20, 0, 0.6f, 0).rot(24, 0, -0.6f, 0).rot(36, 0, 0, 0).done());

        // Blade Whirl: panels fold out horizontal 0-9, body spins four turns 10-41, dizzy settle
        clip(WHIRL, AnimClip.of(50).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(9, 0.1f, 0, 0).rot(10, 0, 0, 0).rot(41, 0, 8 * PI, 0, LINEAR).rot(42, 0, 8 * PI, 0).rot(44, 0.1f, 0, 0.12f, SNAP).rot(47, 0, 0, -0.1f).rot(50, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(9, 0, 1.5f, 0).pos(41, 0, 1.5f, 0).pos(50, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(9, 0, 0, 1.2f).rot(41, 0, 0, 1.2f).rot(48, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(9, 0, 0, -1.2f).rot(41, 0, 0, -1.2f).rot(48, 0, 0, 0)
            .bone("panelR").rot(0, 0, 0, 0).rot(9, 0, 1.5f, 0).rot(41, 0, 1.5f, 0).rot(48, 0, 0, 0)
            .bone("panelL").rot(0, 0, 0, 0).rot(9, 0, -1.5f, 0).rot(41, 0, -1.5f, 0).rot(48, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(9, 0.2f, 0, 0).rot(41, 0.2f, 0, 0).rot(50, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        float c = ls * 0.8f;
        for (int i = 0; i < 4; i++) {
            float ph = (i % 2 == 0 ? 0 : PI);                       // diagonal gait
            for (String sd : new String[]{"R", "L"}) {
                float p = c + ph + (sd.equals("L") ? PI : 0);
                bone("leg" + sd + i).yRot = Mth.cos(p) * 0.4f * la * 3;
                bone("leg" + sd + i).xRot = Mth.sin(p) * 0.25f * la * 3;
            }
        }
        bone("handR").visible = bone("handL").visible = e.v.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.LOUISE;
        bone("handR").zRot = Mth.sin(age * 0.1f) * 0.2f; bone("handL").zRot = -Mth.sin(age * 0.1f) * 0.2f;
        bone("body").y += Mth.sin(age * 0.12f) * 0.25f;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD * 0.8f; bone("head").xRot = headPitch * Mth.DEG_TO_RAD * 0.8f;
        bone("panelR").zRot = Mth.sin(age * 0.1f) * 0.05f; bone("panelL").zRot = -Mth.sin(age * 0.1f) * 0.05f;
    }
}
