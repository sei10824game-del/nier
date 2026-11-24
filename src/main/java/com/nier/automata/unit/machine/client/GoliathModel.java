package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.GoliathUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Goliath: massive torso with back rocket and shoulder pods, rail-mounted fists that slide out, pillar legs. */
public class GoliathModel extends NierModel<MachineUnit> {
    public GoliathModel(ModelPart root) {
        super(root);
        bind(MachineGeo.GOLIATH_PATHS);

        // Rail Arm Sweep: twist + arm cocked out to the right 0-13, fist slides out on its rail, sweep t15
        clip(RAIL_SWIPE, AnimClip.of(36)
            .bone("body").rot(0, 0, 0, 0).rot(13, 0.05f, 0.7f, 0).rot(15, 0.1f, -0.8f, 0, OUT).rot(20, 0.1f, -1.0f, 0).rot(36, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(13, 0, 1, 1).pos(15, 0, 1.5f, -1.5f, OUT).pos(36, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(13, -1.3f, 1.2f, 0.3f).rot(15, -1.45f, -1.1f, 0, OUT).rot(20, -1.2f, -1.4f, 0).rot(28, -0.4f, -0.4f, 0).rot(36, 0, 0, 0)
            .bone("rightFist").pos(0, 0, 0, 0).pos(12, 0, 3, 0).pos(15, 0, 6, 0, OUT).pos(22, 0, 6, 0).pos(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(13, 0.4f, 0, -0.4f).rot(15, -0.5f, 0, -0.5f).rot(36, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(13, 0, -0.5f, 0).rot(15, 0.1f, 0.6f, 0, OUT).rot(20, 0, 0.7f, 0).rot(36, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(13, 0.3f, 0, 0.1f).rot(15, -0.3f, 0, 0.12f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(13, -0.3f, 0, -0.1f).rot(15, 0.25f, 0, -0.12f).rot(36, 0, 0, 0).done());

        // Rail Punch: pull back 0-10, jab t12 with the fist firing out along its rail, hold, retract
        clip(PUNCH, AnimClip.of(30)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -1.0f, 0.3f, 0.2f).rot(12, -1.6f, 0, 0, OUT).rot(17, -1.55f, 0, 0).rot(30, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(10, 0, 0, 3).pos(12, 0, 0, -2, OUT).pos(30, 0, 0, 0)
            .bone("rightFist").pos(0, 0, 0, 0).pos(11, 0, 0, 0).pos(12, 0, 8, 0, OUT).pos(17, 0, 8, 0).pos(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.8f, -0.2f, -0.2f).rot(12, 0.4f, 0, -0.3f).rot(30, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(10, 0, 0.5f, 0).rot(12, 0.2f, -0.4f, 0, OUT).rot(17, 0.15f, -0.3f, 0).rot(30, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(10, 0, 0.5f, 1).pos(12, 0, 0.5f, -2, OUT).pos(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0.1f, -0.3f, 0).rot(12, 0.1f, 0.3f, 0).rot(30, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.2f, 0, 0).rot(12, -0.4f, 0, 0, OUT).rot(30, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, -0.2f, 0, 0).rot(12, 0.35f, 0, 0, OUT).rot(30, 0, 0, 0).done());

        // Grab & Slam: both arms lunge out 0-11, clamp t12, hoist overhead 13-32, slam down t38, shockwave, recover
        clip(GRAB, AnimClip.of(56)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -1.45f, -0.25f, 0).rot(12, -1.4f, -0.35f, 0, OUT).rot(32, -2.7f, -0.3f, 0).rot(35, -2.8f, -0.3f, 0)
                .rot(38, -0.4f, -0.2f, 0, IN).rot(46, -0.5f, -0.2f, 0).rot(56, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -1.45f, 0.25f, 0).rot(12, -1.4f, 0.35f, 0, OUT).rot(32, -2.7f, 0.3f, 0).rot(35, -2.8f, 0.3f, 0)
                .rot(38, -0.4f, 0.2f, 0, IN).rot(46, -0.5f, 0.2f, 0).rot(56, 0, 0, 0)
            .bone("rightFist").pos(0, 0, 0, 0).pos(10, 0, 5, 0).pos(12, 0, 2, 0, OUT).pos(46, 0, 2, 0).pos(56, 0, 0, 0)
            .bone("leftFist").pos(0, 0, 0, 0).pos(10, 0, 5, 0).pos(12, 0, 2, 0, OUT).pos(46, 0, 2, 0).pos(56, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(10, 0.35f, 0, 0).rot(14, 0.2f, 0, 0).rot(32, -0.25f, 0, 0).rot(35, -0.3f, 0, 0).rot(38, 0.55f, 0, 0, IN).rot(46, 0.5f, 0, 0).rot(56, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(10, 0, 1, -2).pos(32, 0, -1, 0).pos(38, 0, 2.5f, -1.5f, IN).pos(46, 0, 2, -1).pos(56, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0).rot(32, -0.5f, 0, 0).rot(38, 0.4f, 0, 0, IN).rot(56, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.5f, 0, 0).rot(32, 0.1f, 0, 0.1f).rot(38, -0.5f, 0, 0.15f).rot(46, -0.5f, 0, 0.15f).rot(56, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.4f, 0, 0).rot(32, 0.1f, 0, -0.1f).rot(38, 0.4f, 0, -0.15f).rot(46, 0.4f, 0, -0.15f).rot(56, 0, 0, 0).done());

        // Ground Slam (both fists, t18) and Shockwave Stomp (t16)
        clip(SLAM, MachineClips.pound(44, 18));
        clip(STOMP, MachineClips.stomp(38, 16));

        // Eye Beam: head dips charging 0-19, sweeps left -> right 20-49 (matches server sweep), cool-down shake
        clip(BEAM, AnimClip.of(60)
            .bone("head").rot(0, 0, 0, 0).rot(16, 0.3f, 0, 0).rot(19, -0.1f, 0, 0).rot(20, 0.05f, -0.45f, 0, OUT).rot(49, 0.05f, 0.45f, 0, LINEAR)
                .rot(52, 0.1f, 0.2f, 0.1f).rot(55, 0.1f, -0.1f, -0.1f).rot(60, 0, 0, 0).scale(0, 1, 1, 1).scale(18, 1.08f, 1.08f, 1.08f).scale(21, 1, 1, 1)
            .bone("body").rot(0, 0, 0, 0).rot(19, -0.15f, 0, 0).rot(20, -0.1f, -0.2f, 0).rot(49, -0.1f, 0.2f, 0, LINEAR).rot(60, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(20, 0, 0, 1).pos(49, 0, 0, 1).pos(60, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(19, 0.2f, 0, 0.4f).rot(49, 0.2f, 0, 0.4f).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(19, 0.2f, 0, -0.4f).rot(49, 0.2f, 0, -0.4f).rot(60, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(19, -0.25f, 0, 0.08f).rot(49, -0.25f, 0, 0.08f).rot(60, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(19, 0.25f, 0, -0.08f).rot(49, 0.25f, 0, -0.08f).rot(60, 0, 0, 0).done());

        // Missile Barrage: pods swing up 0-11, alternating launch kicks every 3 ticks 12-42
        AnimClip mb = AnimClip.of(50);
        AnimClip.Bone rp = mb.bone("rightPod").rot(0, 0, 0, 0).rot(10, -0.7f, 0, 0.15f).pos(0, 0, 0, 0);
        AnimClip.Bone lp = mb.bone("leftPod").rot(0, 0, 0, 0).rot(10, -0.7f, 0, -0.15f).pos(0, 0, 0, 0);
        AnimClip.Bone bb = mb.bone("body").rot(0, 0, 0, 0).rot(10, -0.1f, 0, 0).pos(0, 0, 0, 0);
        for (int f = 12; f <= 42; f += 3) { boolean r = (f / 3) % 2 == 0;
            (r ? rp : lp).pos(f, 0, 0, 1.5f, OUT).rot(f, -0.8f, 0, r ? 0.2f : -0.2f, OUT).pos(f + 2, 0, 0, 0).rot(f + 2, -0.7f, 0, r ? 0.15f : -0.15f);
            bb.rot(f, -0.12f, r ? 0.04f : -0.04f, 0, OUT); }
        rp.rot(50, 0, 0, 0).pos(50, 0, 0, 0); lp.rot(50, 0, 0, 0).pos(50, 0, 0, 0); bb.rot(50, 0, 0, 0).pos(50, 0, 0, 0);
        mb.bone("rightArm").rot(0, 0, 0, 0).rot(10, 0.2f, 0, 0.5f).rot(44, 0.2f, 0, 0.5f).rot(50, 0, 0, 0);
        mb.bone("leftArm").rot(0, 0, 0, 0).rot(10, 0.2f, 0, -0.5f).rot(44, 0.2f, 0, -0.5f).rot(50, 0, 0, 0);
        mb.bone("head").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(44, -0.3f, 0, 0).rot(50, 0, 0, 0);
        clip(MISSILES, mb);

        // Mine Scatter: hunch forward, pods flip backward-up 0-12, lobbing a mine every 2 ticks 14-24
        AnimClip mn = AnimClip.of(36);
        AnimClip.Bone mr = mn.bone("rightPod").rot(0, 0, 0, 0).rot(12, 0.9f, 0, 0), ml = mn.bone("leftPod").rot(0, 0, 0, 0).rot(12, 0.9f, 0, 0);
        for (int f = 14; f <= 24; f += 2) { mr.rot(f, f % 4 == 2 ? 1.1f : 0.9f, 0, 0); ml.rot(f, f % 4 == 0 ? 1.1f : 0.9f, 0, 0); }
        mr.rot(36, 0, 0, 0); ml.rot(36, 0, 0, 0);
        mn.bone("body").rot(0, 0, 0, 0).rot(12, 0.35f, 0, 0).rot(24, 0.35f, 0, 0).rot(36, 0, 0, 0).pos(0, 0, 0, 0).pos(12, 0, 1.5f, 0).pos(24, 0, 1.5f, 0).pos(36, 0, 0, 0);
        mn.bone("head").rot(0, 0, 0, 0).rot(12, -0.3f, 0, 0).rot(24, -0.3f, 0, 0).rot(36, 0, 0, 0);
        mn.bone("rightArm").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0.3f).rot(24, -0.5f, 0, 0.3f).rot(36, 0, 0, 0);
        mn.bone("leftArm").rot(0, 0, 0, 0).rot(12, -0.5f, 0, -0.3f).rot(24, -0.5f, 0, -0.3f).rot(36, 0, 0, 0);
        mn.bone("rightLeg").rot(0, 0, 0, 0).rot(12, -0.35f, 0, 0.1f).rot(24, -0.35f, 0, 0.1f).rot(36, 0, 0, 0);
        mn.bone("leftLeg").rot(0, 0, 0, 0).rot(12, 0.25f, 0, -0.1f).rot(24, 0.25f, 0, -0.1f).rot(36, 0, 0, 0);
        clip(MINES, mn);

        // Rocket Dive Kick: crouch 0-9, rocket ascent 10-21, flip at apex 22-25, extended dive kick, impact ~t32
        clip(DIVE_KICK, AnimClip.of(60)
            .bone("body").rot(0, 0, 0, 0).rot(9, 0.35f, 0, 0).rot(12, -0.2f, 0, 0, OUT).rot(21, -0.2f, 0, 0).rot(25, 0.6f, 0, 0).rot(31, 0.6f, 0, 0)
                .rot(32, 0.35f, 0, 0, IN).rot(42, 0.3f, 0, 0).rot(60, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(9, 0, 3, 0).pos(12, 0, -1, 0, OUT).pos(31, 0, -1, 0).pos(32, 0, 4, -1, IN).pos(42, 0, 3, -1).pos(60, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(9, -0.5f, 0, 0).rot(14, -0.9f, 0, 0).rot(21, -0.9f, 0, 0).rot(25, -1.4f, 0, 0).rot(31, -1.4f, 0, 0)
                .rot(32, -0.6f, 0, 0.2f, IN).rot(42, -0.6f, 0, 0.2f).rot(60, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(9, -0.5f, 0, 0).rot(14, 0.4f, 0, 0).rot(21, 0.4f, 0, 0).rot(25, 0.7f, 0, 0).rot(31, 0.7f, 0, 0)
                .rot(32, 0.5f, 0, -0.2f, IN).rot(42, 0.5f, 0, -0.2f).rot(60, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(9, 0.8f, 0, 0.2f).rot(14, 0.2f, 0, 0.9f).rot(25, -0.6f, 0, 0.8f).rot(31, -0.6f, 0, 0.8f).rot(32, 0.2f, 0, 1.2f, IN).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(9, 0.8f, 0, -0.2f).rot(14, 0.2f, 0, -0.9f).rot(25, -0.6f, 0, -0.8f).rot(31, -0.6f, 0, -0.8f).rot(32, 0.2f, 0, -1.2f, IN).rot(60, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(9, 0.2f, 0, 0).rot(14, -0.4f, 0, 0).rot(25, 0.3f, 0, 0).rot(32, 0.45f, 0, 0, IN).rot(60, 0, 0, 0).done());
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        bone("dome").visible = e.v.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.REVERSE;
        float c = Mth.cos(ls * 0.5f) * 0.7f * la;
        bone("rightLeg").xRot = c; bone("leftLeg").xRot = -c;
        bone("rightArm").xRot = -c * 0.6f; bone("leftArm").xRot = c * 0.6f;
        bone("rightArm").zRot = 0.12f; bone("leftArm").zRot = -0.12f;
        ModelPart body = bone("body");
        body.y += -Mth.abs(Mth.cos(ls * 0.5f)) * 1.0f * la + Mth.sin(age * 0.05f) * 0.4f;
        body.zRot = Mth.cos(ls * 0.5f) * 0.05f * la;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD; bone("head").xRot = headPitch * Mth.DEG_TO_RAD;
    }
}
