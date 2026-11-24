package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.FlyerUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import com.nier.automata.unit.machine.MachineVariant.Size;
import com.nier.automata.unit.machine.MachineVariant.Weapon;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Flyer: compact core with side rotor nacelles (gun-tipped), hanging cutter arms (medium) and missile pod. */
public class FlyerModel extends NierModel<MachineUnit> {
    public FlyerModel(ModelPart root) {
        super(root);
        bind(MachineGeo.FLYER_PATHS);

        // Orb Volley: pitch back 0-9, recoil pulses t10/14/18/22, nacelles alternate kick
        AnimClip vo = AnimClip.of(30);
        AnimClip.Bone vb = vo.bone("body").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0).pos(0, 0, 0, 0);
        AnimClip.Bone vr = vo.bone("rightWing").rot(0, 0, 0, 0).rot(8, 0, 0.3f, 0.15f), vl = vo.bone("leftWing").rot(0, 0, 0, 0).rot(8, 0, -0.3f, -0.15f);
        for (int f = 10; f <= 22; f += 4) { boolean r = (f / 4) % 2 == 0;
            vb.rot(f, -0.42f, r ? 0.08f : -0.08f, 0, OUT).pos(f, 0, 0, 1.2f, OUT).rot(f + 2, -0.3f, 0, 0).pos(f + 2, 0, 0, 0);
            (r ? vr : vl).rot(f, 0, r ? 0.55f : -0.55f, r ? 0.3f : -0.3f, OUT).rot(f + 3, 0, r ? 0.3f : -0.3f, r ? 0.15f : -0.15f); }
        vb.rot(30, 0, 0, 0).pos(30, 0, 0, 0); vr.rot(30, 0, 0, 0); vl.rot(30, 0, 0, 0);
        vo.bone("rightArm").rot(0, 0, 0, 0).rot(8, 0.5f, 0, 0.3f).rot(24, 0.5f, 0, 0.3f).rot(30, 0, 0, 0);
        vo.bone("leftArm").rot(0, 0, 0, 0).rot(8, 0.5f, 0, -0.3f).rot(24, 0.5f, 0, -0.3f).rot(30, 0, 0, 0);
        clip(VOLLEY, vo);

        // Dive Ram: rear up 0-9, nose-down swept-wing dive 10-23, pull-up 24-35
        clip(DIVE, AnimClip.of(36)
            .bone("body").rot(0, 0, 0, 0).rot(9, -0.55f, 0, 0).rot(11, 1.0f, 0, 0, OUT).rot(23, 1.0f, 0, 0).rot(27, -0.45f, 0, 0).rot(36, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(9, 0, -1.5f, 1).pos(11, 0, 0, -1).pos(23, 0, 0, -1).pos(36, 0, 0, 0)
            .bone("rightWing").rot(0, 0, 0, 0).rot(9, 0, 0, 0.45f).rot(11, 0, 0.7f, -0.1f).rot(23, 0, 0.7f, -0.1f).rot(27, 0, 0, 0.3f).rot(36, 0, 0, 0)
            .bone("leftWing").rot(0, 0, 0, 0).rot(9, 0, 0, -0.45f).rot(11, 0, -0.7f, 0.1f).rot(23, 0, -0.7f, 0.1f).rot(27, 0, 0, -0.3f).rot(36, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(9, -0.6f, 0, 0.4f).rot(11, 1.3f, 0, 0).rot(23, 1.3f, 0, 0).rot(28, -0.3f, 0, 0.3f).rot(36, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(9, -0.6f, 0, -0.4f).rot(11, 1.3f, 0, 0).rot(23, 1.3f, 0, 0).rot(28, -0.3f, 0, -0.3f).rot(36, 0, 0, 0).done());

        // Missile Salvo: pod tilts up and rises 0-11, launches t12/16/20/24 with pod kick
        AnimClip ms = AnimClip.of(40);
        AnimClip.Bone mp = ms.bone("pod").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0).pos(0, 0, 0, 0).pos(10, 0, -1.5f, 0);
        AnimClip.Bone mb = ms.bone("body").rot(0, 0, 0, 0).rot(10, -0.15f, 0, 0).pos(0, 0, 0, 0);
        for (int f = 12; f <= 24; f += 4) { mp.rot(f, -0.75f, (f % 8 == 0 ? 0.12f : -0.12f), 0, OUT).pos(f, 0, -1.5f, 1.2f, OUT).rot(f + 2, -0.6f, 0, 0).pos(f + 2, 0, -1.5f, 0);
            mb.pos(f, 0, 0.6f, 0, OUT).pos(f + 2, 0, 0, 0); }
        mp.rot(32, -0.6f, 0, 0).rot(40, 0, 0, 0).pos(32, 0, -1.5f, 0).pos(40, 0, 0, 0); mb.rot(40, 0, 0, 0).pos(40, 0, 0, 0);
        ms.bone("rightWing").rot(0, 0, 0, 0).rot(10, 0, 0, -0.2f).rot(30, 0, 0, -0.2f).rot(40, 0, 0, 0);
        ms.bone("leftWing").rot(0, 0, 0, 0).rot(10, 0, 0, 0.2f).rot(30, 0, 0, 0.2f).rot(40, 0, 0, 0);
        clip(MISSILES, ms);

        // Self-Destruct Units: arms clutch bombs 0-11, release flicks t12/18/24 with a lift each time
        AnimClip bo = AnimClip.of(34);
        AnimClip.Bone br = bo.bone("rightArm").rot(0, 0, 0, 0).rot(10, 0.1f, 0, -0.4f), bl = bo.bone("leftArm").rot(0, 0, 0, 0).rot(10, 0.1f, 0, 0.4f);
        AnimClip.Bone bb = bo.bone("body").rot(0, 0, 0, 0).rot(10, -0.15f, 0, 0).pos(0, 0, 0, 0);
        for (int f = 12; f <= 24; f += 6) { br.rot(f, 0.2f, 0, 0.8f, OUT).rot(f + 4, 0.1f, 0, -0.4f); bl.rot(f, 0.2f, 0, -0.8f, OUT).rot(f + 4, 0.1f, 0, 0.4f);
            bb.pos(f, 0, -1.2f, 0, OUT).pos(f + 3, 0, 0, 0); }
        br.rot(34, 0, 0, 0); bl.rot(34, 0, 0, 0); bb.rot(34, 0, 0, 0).pos(34, 0, 0, 0);
        clip(BOMBS, bo);

        // Wire Cutters: blades flung back and wide 0-8, scissor snap t10, recoil
        clip(CUTTER, AnimClip.of(24)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, 0.9f, 0, 1.2f).rot(10, -1.3f, 0, -0.35f, OUT).rot(14, -1.1f, 0, -0.2f).rot(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, 0.9f, 0, -1.2f).rot(10, -1.3f, 0, 0.35f, OUT).rot(14, -1.1f, 0, 0.2f).rot(24, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0).rot(10, 0.35f, 0, 0, OUT).rot(14, 0.2f, 0, 0).rot(24, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0, 1).pos(10, 0, 0, -2, OUT).pos(24, 0, 0, 0)
            .bone("rightWing").rot(0, 0, 0, 0).rot(8, 0, -0.3f, 0.2f).rot(10, 0, 0.3f, -0.1f).rot(24, 0, 0, 0)
            .bone("leftWing").rot(0, 0, 0, 0).rot(8, 0, 0.3f, -0.2f).rot(10, 0, -0.3f, 0.1f).rot(24, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        bone("hat").visible = e.v.weapon() == com.nier.automata.unit.machine.MachineVariant.Weapon.GUN;
        boolean fl = e.v.flight(); bone("jwingR").visible = bone("jwingL").visible = bone("gsword").visible = fl;
        bone("rightRotor").visible = bone("leftRotor").visible = bone("rightWing").visible = bone("leftWing").visible = !fl;
        boolean medium = e.v.size() == Size.MEDIUM;
        bone("rightArm").visible = bone("leftArm").visible = medium;
        bone("pod").visible = medium || e.v.weapon() == Weapon.MISSILE;
        bone("rightRotor").yRot = age * 1.7f; bone("leftRotor").yRot = -age * 1.7f;
        ModelPart body = bone("body");
        body.y += Mth.sin(age * 0.15f) * 1.0f;
        body.xRot = la * 0.5f + headPitch * Mth.DEG_TO_RAD * 0.5f;
        body.yRot = headYaw * Mth.DEG_TO_RAD * 0.6f;
        body.zRot = Mth.sin(age * 0.07f) * 0.06f;
        bone("rightArm").xRot = Mth.sin(age * 0.11f) * 0.15f; bone("leftArm").xRot = Mth.sin(age * 0.11f + 1) * 0.15f;
    }
}
