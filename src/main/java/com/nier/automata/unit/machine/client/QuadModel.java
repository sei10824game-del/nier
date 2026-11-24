package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.QuadUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Four-legged machine with a bladed tail; trot gait in idle. */
public class QuadModel extends NierModel<MachineUnit> {
    private static final float PI = Mth.PI;

    public QuadModel(ModelPart root) {
        super(root);
        bind(MachineGeo.QUAD_PATHS);

        // Pounce: crouch + coil 0-9, spring at 10 with forelegs reaching, land 20, recover
        AnimClip po = AnimClip.of(30).blend(3);
        po.bone("body").rot(0, 0, 0, 0).rot(9, 0.35f, 0, 0).rot(11, -0.5f, 0, 0, OUT).rot(18, -0.2f, 0, 0).rot(21, 0.2f, 0, 0, IN).rot(30, 0, 0, 0)
            .pos(0, 0, 0, 0).pos(9, 0, 2, 1).pos(11, 0, -1, -2, OUT).pos(30, 0, 0, 0);
        po.bone("head").rot(0, 0, 0, 0).rot(9, -0.4f, 0, 0).rot(11, 0.4f, 0, 0).rot(30, 0, 0, 0);
        po.bone("tail").rot(0, 0, 0, 0).rot(9, 0.5f, 0, 0).rot(11, -0.6f, 0, 0).rot(30, 0, 0, 0);
        for (String sd : new String[]{"R", "L"}) {
            po.bone("leg" + sd + "0").rot(0, 0, 0, 0).rot(9, 0.5f, 0, 0).rot(11, -1.3f, 0, 0, OUT).rot(20, -0.4f, 0, 0).rot(30, 0, 0, 0);
            po.bone("leg" + sd + "1").rot(0, 0, 0, 0).rot(9, -0.6f, 0, 0).rot(11, 0.9f, 0, 0, OUT).rot(20, 0.3f, 0, 0).rot(30, 0, 0, 0);
        }
        clip(POUNCE, po);

        // Gore Charge: lower head and paw 0-11, galloping legs 12-29
        AnimClip ch = AnimClip.of(40).blend(3);
        ch.bone("body").rot(0, 0, 0, 0).rot(10, 0.15f, 0, 0).rot(12, -0.1f, 0, 0).rot(30, -0.1f, 0, 0).rot(40, 0, 0, 0).pos(0, 0, 0, 0).pos(10, 0, 1, 1).pos(40, 0, 0, 0);
        ch.bone("head").rot(0, 0, 0, 0).rot(10, 0.7f, 0, 0).rot(30, 0.7f, 0, 0).rot(40, 0, 0, 0);
        AnimClip.Bone fl = ch.bone("legR0").rot(0, 0, 0, 0), fr = ch.bone("legL0").rot(0, 0, 0, 0);
        for (int t = 4; t <= 28; t += 4) { float a = (t / 4) % 2 == 0 ? 0.9f : -0.9f; fl.rot(t, a, 0, 0); fr.rot(t, -a, 0, 0); }
        fl.rot(40, 0, 0, 0); fr.rot(40, 0, 0, 0);
        ch.bone("legR1").rot(0, 0, 0, 0).rot(12, -0.8f, 0, 0).rot(20, 0.8f, 0, 0).rot(28, -0.8f, 0, 0).rot(40, 0, 0, 0);
        ch.bone("legL1").rot(0, 0, 0, 0).rot(12, 0.8f, 0, 0).rot(20, -0.8f, 0, 0).rot(28, 0.8f, 0, 0).rot(40, 0, 0, 0);
        clip(CHARGE, ch);

        // Tail Blade: body winds right, tail whips around 12, head follows
        clip(TAIL, AnimClip.of(26).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(9, 0, 0.6f, 0).rot(12, 0, -0.9f, 0, OUT).rot(20, 0, -0.3f, 0).rot(26, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(9, 0, -0.5f, 0).rot(12, 0, 0.6f, 0).rot(26, 0, 0, 0)
            .bone("tail").rot(0, 0, 0, 0).rot(9, 0, 1.2f, 0.3f).rot(12, 0, -1.4f, -0.3f, OUT).rot(20, 0, -0.5f, 0).rot(26, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        bone("antlers").visible = e.v.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.ANIMAL;
        float c = ls * 0.9f;
        bone("legR0").xRot = Mth.cos(c) * 0.8f * la; bone("legL1").xRot = Mth.cos(c) * 0.8f * la;
        bone("legL0").xRot = Mth.cos(c + PI) * 0.8f * la; bone("legR1").xRot = Mth.cos(c + PI) * 0.8f * la;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD * 0.8f; bone("head").xRot = headPitch * Mth.DEG_TO_RAD * 0.8f;
        bone("tail").yRot = Mth.sin(age * 0.1f) * 0.2f;
        bone("body").y += Mth.sin(age * 0.12f) * 0.2f;
    }
}
