package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.MonsterUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Hunched predator: jaw hinge, clawed arms, digitigrade legs. */
public class MonsterModel extends NierModel<MachineUnit> {
    public MonsterModel(ModelPart root) {
        super(root);
        bind(MachineGeo.MONSTER_PATHS);

        // Crushing Bite: rear back + jaw wide 0-11, lunge and snap at 14, shake
        clip(BITE, AnimClip.of(30).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(10, -0.35f, 0, 0).rot(14, 0.4f, 0, 0, OUT).rot(20, 0.2f, 0.2f, 0).rot(30, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(10, 0, 1, 2).pos(14, 0, 2, -4, OUT).pos(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.5f, 0, 0).rot(14, 0.35f, 0, 0, OUT).rot(18, 0.2f, 0.4f, 0).rot(22, 0.2f, -0.4f, 0).rot(30, 0, 0, 0)
            .bone("jaw").rot(0, 0, 0, 0).rot(10, 0.9f, 0, 0).rot(14, 0, 0, 0, SNAP).rot(24, 0.1f, 0, 0).rot(30, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(10, -0.8f, 0, 0).rot(14, 0.5f, 0, 0).rot(30, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(10, -0.8f, 0, 0).rot(14, 0.5f, 0, 0).rot(30, 0, 0, 0).done());

        // Claw Rake: twist to the right then rake with the left (9) and right (17)
        clip(CLAWS, AnimClip.of(32).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(7, 0, 0.5f, 0).rot(9, 0, -0.6f, 0, OUT).rot(14, 0, -0.5f, 0).rot(17, 0, 0.6f, 0, OUT).rot(32, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(7, -1.8f, 0, -0.4f).rot(9, -0.3f, 0, 0.5f, OUT).rot(14, -0.6f, 0, 0).rot(32, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(13, -1.8f, 0, 0.4f).rot(17, -0.3f, 0, -0.5f, OUT).rot(32, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(7, 0, 0.3f, 0).rot(9, 0, -0.4f, 0).rot(17, 0, 0.4f, 0).rot(32, 0, 0, 0)
            .bone("jaw").rot(0, 0, 0, 0).rot(9, 0.5f, 0, 0).rot(17, 0.5f, 0, 0).rot(32, 0, 0, 0).done());

        // Pounce: crouch 0-11, leap 12, arms spread forward, land ~20
        clip(POUNCE, AnimClip.of(38).blend(3)
            .bone("body").rot(0, 0, 0, 0).rot(11, 0.5f, 0, 0).rot(13, -0.6f, 0, 0, OUT).rot(20, -0.3f, 0, 0).rot(24, 0.3f, 0, 0).rot(38, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(11, 0, 3, 1).pos(38, 0, 0, 0)
            .bone("legR").rot(0, 0, 0, 0).rot(11, -0.9f, 0, 0).rot(13, 0.7f, 0, 0, OUT).rot(38, 0, 0, 0)
            .bone("legL").rot(0, 0, 0, 0).rot(11, -0.9f, 0, 0).rot(13, 0.7f, 0, 0, OUT).rot(38, 0, 0, 0)
            .bone("armR").rot(0, 0, 0, 0).rot(11, 0.6f, 0, 0).rot(13, -2.2f, 0, 0.3f, OUT).rot(24, -1.2f, 0, 0).rot(38, 0, 0, 0)
            .bone("armL").rot(0, 0, 0, 0).rot(11, 0.6f, 0, 0).rot(13, -2.2f, 0, -0.3f, OUT).rot(24, -1.2f, 0, 0).rot(38, 0, 0, 0)
            .bone("jaw").rot(0, 0, 0, 0).rot(13, 0.8f, 0, 0).rot(22, 0, 0, 0, SNAP).rot(38, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        float c = ls * 0.8f;
        bone("legR").xRot = Mth.cos(c) * 0.7f * la; bone("legL").xRot = Mth.cos(c + Mth.PI) * 0.7f * la;
        bone("armR").xRot = Mth.cos(c + Mth.PI) * 0.4f * la; bone("armL").xRot = Mth.cos(c) * 0.4f * la;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD * 0.8f; bone("head").xRot = headPitch * Mth.DEG_TO_RAD * 0.8f + Mth.sin(age * 0.1f) * 0.04f;
        bone("jaw").xRot = (Mth.sin(age * 0.08f) * 0.5f + 0.5f) * 0.12f;
        bone("body").y += Mth.sin(age * 0.12f) * 0.25f;
    }
}
