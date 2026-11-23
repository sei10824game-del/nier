package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.TankUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;

/** Tank: tracked hull, turret with cannon + missile pods, front cleaver arm, rear crane hammer, cargo box. */
public class TankModel extends NierModel<MachineUnit> {
    public TankModel(ModelPart root) {
        super(root);
        bind(MachineGeo.TANK_PATHS);

        // Cannon Rounds: barrel elevates 0-13, three recoiling shots t14/18/22
        AnimClip cn = AnimClip.of(36);
        AnimClip.Bone cb = cn.bone("cannon").rot(0, 0, 0, 0).rot(12, -0.15f, 0, 0).pos(0, 0, 0, 0);
        AnimClip.Bone tb = cn.bone("turret").pos(0, 0, 0, 0), hb = cn.bone("hull").rot(0, 0, 0, 0);
        for (int f = 14; f <= 22; f += 4) { cb.pos(f, 0, 0, 4, OUT).rot(f, -0.2f, 0, 0, OUT).pos(f + 3, 0, 0, 0).rot(f + 3, -0.15f, 0, 0);
            tb.pos(f, 0, 0, 0.6f, OUT).pos(f + 2, 0, 0, 0); hb.rot(f, -0.03f, 0, 0, OUT).rot(f + 2, 0, 0, 0); }
        cb.rot(36, 0, 0, 0).pos(36, 0, 0, 0); tb.pos(36, 0, 0, 0); hb.rot(36, 0, 0, 0);
        clip(CANNON, cn);

        // Sweeping Laser: barrel draws in charging 0-19 while turret swings to -50deg, sweeps to +50deg 20-51 (matches server)
        clip(LASER, AnimClip.of(64)
            .bone("turret").rot(0, 0, 0, 0).rot(18, 0, -0.87f, 0).rot(20, 0, -0.87f, 0).rot(51, 0, 0.87f, 0, LINEAR).rot(56, 0, 0.87f, 0).rot(64, 0, 0, 0)
            .bone("cannon").rot(0, 0, 0, 0).rot(18, 0.05f, 0, 0).rot(51, 0.05f, 0, 0).rot(64, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(16, 0, 0, 3).pos(20, 0, 0, -1, OUT).pos(51, 0, 0, -1).pos(58, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(19, 1.15f, 1.15f, 1).scale(21, 1, 1, 1)
            .bone("rightPod").rot(0, 0, 0, 0).rot(18, 0.3f, 0, 0).rot(51, 0.3f, 0, 0).rot(64, 0, 0, 0)
            .bone("leftPod").rot(0, 0, 0, 0).rot(18, 0.3f, 0, 0).rot(51, 0.3f, 0, 0).rot(64, 0, 0, 0)
            .bone("hull").pos(0, 0, 0, 0).pos(20, 0, 0.4f, 0).pos(51, 0, 0.4f, 0).pos(64, 0, 0, 0).done());

        // Arm Cleave: cleaver arm rears up 0-12, chops down t14 (blade bites in), hull dips, pull out
        clip(CLEAVE, AnimClip.of(34)
            .bone("arm").rot(0, 0, 0, 0).rot(11, -1.1f, 0.15f, 0).rot(13, -1.2f, 0.15f, 0).rot(14, 0.35f, 0, 0, IN).rot(21, 0.3f, 0, 0).rot(34, 0, 0, 0)
            .bone("blade").rot(0, 0, 0, 0).rot(11, -0.7f, 0, 0).rot(14, 0.35f, 0, 0, IN).rot(21, 0.3f, 0, 0).rot(34, 0, 0, 0)
            .bone("hull").rot(0, 0, 0, 0).rot(12, -0.04f, 0, 0).rot(14, 0.05f, 0, 0, IN).rot(20, 0.03f, 0, 0).rot(34, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(14, 0, 0.6f, -1, IN).pos(34, 0, 0, 0)
            .bone("turret").rot(0, 0, 0, 0).rot(12, -0.05f, 0, 0).rot(14, 0.08f, 0, 0).rot(34, 0, 0, 0).done());

        // Crane Hammer: boom hoists the hammer high 0-15, drops it behind the tank t18, swing settles
        clip(HAMMER, AnimClip.of(40)
            .bone("boom").rot(0, 0, 0, 0).rot(15, 1.0f, 0, 0).rot(16, 1.05f, 0, 0).rot(18, -0.3f, 0, 0, IN).rot(26, -0.25f, 0, 0).rot(40, 0, 0, 0)
            .bone("hammer").rot(0, 0, 0, 0).rot(15, -0.9f, 0, 0).rot(18, 0.4f, 0, 0, IN).rot(21, -0.2f, 0, 0).rot(24, 0.1f, 0, 0).rot(30, 0, 0, 0)
            .bone("crane").rot(0, 0, 0, 0).rot(15, -0.2f, 0, 0).rot(18, 0.15f, 0, 0, IN).rot(40, 0, 0, 0)
            .bone("hull").rot(0, 0, 0, 0).rot(15, 0.03f, 0, 0).rot(18, -0.05f, 0, 0, IN).rot(24, 0, 0, 0)
            .bone("turret").rot(0, 0, 0, 0).rot(15, 0, 0.6f, 0).rot(30, 0, 0.6f, 0).rot(40, 0, 0, 0).done());

        // Turn & Charge: engine shudder while turning 0-17, ramming run 18-43 (bouncing hull, arm forward), brake
        AnimClip cg = AnimClip.of(56);
        AnimClip.Bone ch = cg.bone("hull").rot(0, 0, 0, 0).pos(0, 0, 0, 0);
        for (int t = 1; t <= 17; t++) ch.rot(t, 0, 0, t % 2 == 0 ? 0.015f : -0.015f);
        for (int t = 18; t <= 43; t += 2) ch.rot(t, -0.04f, 0, 0).pos(t, 0, (t / 2) % 2 == 0 ? -0.6f : 0, 0);
        ch.rot(47, 0.08f, 0, 0).pos(47, 0, 0.3f, -1).rot(56, 0, 0, 0).pos(56, 0, 0, 0);
        cg.bone("arm").rot(0, 0, 0, 0).rot(16, 0.35f, 0, 0).rot(44, 0.35f, 0, 0).rot(48, -0.2f, 0, 0).rot(56, 0, 0, 0);
        cg.bone("cannon").rot(0, 0, 0, 0).rot(16, 0.12f, 0, 0).rot(44, 0.12f, 0, 0).rot(48, -0.15f, 0, 0).rot(56, 0, 0, 0);
        cg.bone("turret").rot(0, 0, 0, 0).rot(16, 0, 0, 0).rot(47, 0.06f, 0, 0).rot(56, 0, 0, 0);
        clip(CHARGE, cg);

        // Missile Pods: pods raise 0-11, right/left alternating launches every 3 ticks 12-33
        AnimClip mp = AnimClip.of(46);
        AnimClip.Bone rp = mp.bone("rightPod").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0).pos(0, 0, 0, 0);
        AnimClip.Bone lp = mp.bone("leftPod").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0).pos(0, 0, 0, 0);
        for (int f = 12; f <= 33; f += 3) { boolean r = (f / 3) % 2 == 0;
            (r ? rp : lp).pos(f, 0, 0, 1.5f, OUT).rot(f, -0.7f, 0, 0, OUT).pos(f + 2, 0, 0, 0).rot(f + 2, -0.6f, 0, 0); }
        rp.rot(38, -0.6f, 0, 0).rot(46, 0, 0, 0).pos(46, 0, 0, 0); lp.rot(38, -0.6f, 0, 0).rot(46, 0, 0, 0).pos(46, 0, 0, 0);
        mp.bone("turret").rot(0, 0, 0, 0).rot(10, -0.05f, 0, 0).rot(36, -0.05f, 0, 0).rot(46, 0, 0, 0);
        clip(MISSILES, mp);

        // Deploy Cargo: rear deck tilts, crane swings out over each side to lower a Small Stubby (t20 right, t26 left)
        clip(DEPLOY, AnimClip.of(40)
            .bone("crane").rot(0, 0, 0, 0).rot(14, 0, -1.3f, 0).rot(20, 0, -1.4f, 0).rot(23, 0, 1.3f, 0).rot(26, 0, 1.4f, 0).rot(34, 0, 0, 0)
            .bone("boom").rot(0, 0, 0, 0).rot(14, 0.6f, 0, 0).rot(19, 0.6f, 0, 0).rot(20, -0.2f, 0, 0, IN).rot(23, 0.6f, 0, 0).rot(26, -0.2f, 0, 0, IN).rot(34, 0, 0, 0)
            .bone("hammer").rot(0, 0, 0, 0).rot(20, 0.3f, 0, 0).rot(22, -0.2f, 0, 0).rot(26, 0.3f, 0, 0).rot(30, 0, 0, 0)
            .bone("hull").rot(0, 0, 0, 0).rot(12, 0.05f, 0, 0).rot(30, 0.05f, 0, 0).rot(40, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        ModelPart hull = bone("hull");
        hull.y += Mth.sin(ls * 1.3f) * 0.25f * la + Mth.sin(age * 0.8f) * 0.05f;   // engine idle rumble
        hull.xRot = Mth.sin(ls * 0.7f) * 0.015f * la;
        bone("turret").yRot = Mth.clamp(headYaw, -60, 60) * Mth.DEG_TO_RAD;
        bone("cannon").xRot = Mth.clamp(headPitch, -20, 10) * Mth.DEG_TO_RAD;
        bone("hammer").xRot = Mth.sin(age * 0.05f) * 0.08f;
    }
}
