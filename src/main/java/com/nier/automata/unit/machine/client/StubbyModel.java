package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.StubbyUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import com.nier.automata.unit.machine.MachineVariant.Weapon;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Stubby: round head, squat torso, long arms, stub legs; weapon parts toggled per variant. Clips match StubbyUnit frames. */
public class StubbyModel extends NierModel<MachineUnit> {
    private static final float PI = Mth.PI;

    public StubbyModel(ModelPart root) {
        super(root);
        bind(MachineGeo.STUBBY_PATHS);

        // Arm Swipe: right swipe lands t9, left swipe t15 (body twists through both, steps in on each)
        clip(SWIPE, AnimClip.of(24)
            .bone("body").rot(0, 0, 0, 0).rot(6, -0.1f, 0.5f, 0).rot(9, 0.25f, -0.6f, 0, OUT).rot(12, 0.1f, -0.5f, 0).rot(15, 0.25f, 0.6f, 0, OUT).rot(19, 0.1f, 0.2f, 0).rot(24, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0.5f, -0.5f).pos(9, 0, 0.5f, -2, OUT).pos(14, 0, 0.5f, -1).pos(15, 0, 0.5f, -2, OUT).pos(24, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(6, -2.2f, 0.3f, 0.6f).rot(9, -0.9f, -0.9f, -0.2f, OUT).rot(11, -0.5f, -1.1f, 0).rot(16, -0.2f, 0, 0.1f).rot(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(9, 0.3f, 0, -0.3f).rot(12, -2.2f, -0.3f, -0.6f).rot(15, -0.9f, 0.9f, 0.2f, OUT).rot(18, -0.5f, 1.1f, 0).rot(24, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, -0.15f, -0.3f, 0).rot(9, 0.15f, 0.3f, 0).rot(12, -0.1f, 0.3f, 0).rot(15, 0.15f, -0.3f, 0).rot(24, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0).rot(9, -0.5f, 0, 0, OUT).rot(14, 0.2f, 0, 0).rot(15, 0.3f, 0, 0).rot(24, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0).rot(9, 0.4f, 0, 0).rot(14, -0.4f, 0, 0).rot(15, -0.5f, 0, 0, OUT).rot(24, 0, 0, 0).done());

        // Spinning Arms: crouch 0-8, spins four turns 9-33 (hits every 6 ticks), dizzy wobble 35-39
        clip(SPIN, AnimClip.of(40)
            .bone("body").rot(0, 0, 0, 0).rot(8, 0.2f, -0.6f, 0).rot(9, 0.1f, 0, 0).rot(33, 0.1f, 8 * PI, 0, LINEAR).rot(34, 0, 8 * PI, 0).rot(35, 0, 0, 0.15f, SNAP)
                .rot(37, 0, 0, -0.15f).rot(39, 0, 0, 0.05f).pos(0, 0, 0, 0).pos(8, 0, 1.5f, 0).pos(12, 0, 0.5f, 0).pos(33, 0, 0.5f, 0).pos(36, 0, 1, 0).pos(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0.6f).rot(11, -0.2f, 0, 1.45f, OUT).rot(33, -0.2f, 0, 1.45f).rot(36, 0.2f, 0, 0.3f).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, 0.3f, 0, -0.6f).rot(11, -0.2f, 0, -1.45f, OUT).rot(33, -0.2f, 0, -1.45f).rot(36, 0.2f, 0, -0.3f).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0).rot(12, -0.2f, 0, 0).rot(33, -0.2f, 0, 0).rot(35, 0.2f, 0.4f, 0.2f).rot(37, 0.1f, -0.4f, -0.2f).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0.2f).rot(15, 0.4f, 0, 0.1f).rot(21, -0.4f, 0, 0.1f).rot(27, 0.4f, 0, 0.1f).rot(33, -0.3f, 0, 0.1f).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, 0.3f, 0, -0.2f).rot(15, -0.4f, 0, -0.1f).rot(21, 0.4f, 0, -0.1f).rot(27, -0.4f, 0, -0.1f).rot(33, 0.3f, 0, -0.1f).rot(40, 0, 0, 0).done());

        // Jump Press: crouch 0-7, launch t8, tuck in air, belly-slam lands ~t20
        clip(PRESS, MachineClips.press(34, 8, 20, 1.3f));

        // Headbutt Tackle: paw the ground 0-9, head-down charge 10-19, stumble 20-29
        AnimClip tackle = AnimClip.of(30);
        tackle.bone("body").rot(0, 0, 0, 0).rot(5, -0.3f, 0, 0).rot(9, -0.35f, 0, 0).rot(11, 0.6f, 0, 0, OUT).rot(19, 0.6f, 0, 0).rot(22, -0.2f, 0, 0.2f).rot(25, -0.1f, 0, -0.2f).rot(30, 0, 0, 0)
            .pos(0, 0, 0, 0).pos(9, 0, 0, 1).pos(11, 0, 1, -1.5f).pos(19, 0, 1, -1.5f).pos(30, 0, 0, 0);
        tackle.bone("head").rot(0, 0, 0, 0).rot(9, -0.4f, 0, 0).rot(11, 0.45f, 0, 0).rot(19, 0.45f, 0, 0).rot(22, 0, 0.35f, 0).rot(24, 0, -0.35f, 0).rot(26, 0, 0.25f, 0).rot(30, 0, 0, 0);
        tackle.bone("rightArm").rot(0, 0, 0, 0).rot(9, 0.7f, 0, 0.3f).rot(11, 1.1f, 0, 0.4f).rot(19, 1.1f, 0, 0.4f).rot(22, -0.8f, 0, 0.6f).rot(30, 0, 0, 0);
        tackle.bone("leftArm").rot(0, 0, 0, 0).rot(9, 0.7f, 0, -0.3f).rot(11, 1.1f, 0, -0.4f).rot(19, 1.1f, 0, -0.4f).rot(22, -0.8f, 0, -0.6f).rot(30, 0, 0, 0);
        AnimClip.Bone rl = tackle.bone("rightLeg").rot(0, 0, 0, 0).rot(2, 0.6f, 0, 0).rot(4, -0.2f, 0, 0).rot(6, 0.6f, 0, 0).rot(8, -0.2f, 0, 0);
        AnimClip.Bone ll = tackle.bone("leftLeg").rot(0, 0, 0, 0).rot(9, 0.2f, 0, 0);
        for (int t = 10; t <= 19; t += 2) { float a = (t / 2) % 2 == 0 ? 0.9f : -0.9f; rl.rot(t, a, 0, 0); ll.rot(t, -a, 0, 0); }
        rl.rot(23, -0.3f, 0, 0).rot(30, 0, 0, 0); ll.rot(23, 0.3f, 0, 0).rot(30, 0, 0, 0);
        clip(TACKLE, tackle);

        // Energy Round: rear back + charge 0-11, head snaps forward firing at t12
        clip(ORB, AnimClip.of(26)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.55f, 0, 0).rot(11, -0.6f, 0, 0).rot(12, 0.25f, 0, 0, OUT).rot(16, 0.1f, 0, 0).rot(26, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(11, 1.08f, 1.08f, 1.08f).scale(13, 1, 1, 1)
            .bone("body").rot(0, 0, 0, 0).rot(11, -0.25f, 0, 0).rot(12, 0.15f, 0, 0, OUT).rot(26, 0, 0, 0).pos(0, 0, 0, 0).pos(11, 0, 0.5f, 0).pos(13, 0, 0.5f, 1.2f, OUT).pos(26, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(11, 0.5f, 0, 0.5f).rot(13, -0.3f, 0, 0.9f).rot(26, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(11, 0.5f, 0, -0.5f).rot(13, -0.3f, 0, -0.9f).rot(26, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(11, -0.3f, 0, 0.15f).rot(26, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(11, 0.3f, 0, -0.15f).rot(26, 0, 0, 0).done());

        // Self-Destruct: jolt, frantic arm-waving sprint 6-39, swell + tremble 40-49 then boom
        AnimClip sd = AnimClip.of(50).blend(2);
        AnimClip.Bone sb = sd.bone("body").rot(0, 0, 0, 0).rot(3, -0.3f, 0, 0).rot(6, 0.35f, 0, 0).rot(39, 0.35f, 0, 0).rot(40, 0, 0, 0)
            .scale(0, 1, 1, 1).scale(3, 1.12f, 0.9f, 1.12f).scale(6, 1, 1, 1).scale(39, 1, 1, 1).scale(49, 1.4f, 1.3f, 1.4f);
        for (int t = 41; t <= 49; t++) sb.rot(t, 0, 0, t % 2 == 0 ? 0.12f : -0.12f);
        AnimClip.Bone sra = sd.bone("rightArm").rot(0, 0, 0, 0).rot(3, -2.8f, 0, 0.4f);
        AnimClip.Bone sla = sd.bone("leftArm").rot(0, 0, 0, 0).rot(3, -2.8f, 0, -0.4f);
        AnimClip.Bone srl = sd.bone("rightLeg").rot(0, 0, 0, 0), sll = sd.bone("leftLeg").rot(0, 0, 0, 0);
        AnimClip.Bone sh = sd.bone("head").rot(0, 0, 0, 0).rot(3, -0.4f, 0, 0);
        for (int t = 6; t <= 39; t += 3) {
            boolean a = (t / 3) % 2 == 0;
            sra.rot(t, a ? -3.0f : -2.0f, 0, a ? 0.6f : 0.2f); sla.rot(t, a ? -2.0f : -3.0f, 0, a ? -0.2f : -0.6f);
            srl.rot(t, a ? 0.9f : -0.9f, 0, 0); sll.rot(t, a ? -0.9f : 0.9f, 0, 0);
            sh.rot(t, -0.2f, a ? 0.25f : -0.25f, 0);
        }
        sra.rot(42, -0.4f, 0, 1.2f).rot(49, -0.6f, 0, 1.4f); sla.rot(42, -0.4f, 0, -1.2f).rot(49, -0.6f, 0, -1.4f);
        srl.rot(42, 0, 0, 0.2f); sll.rot(42, 0, 0, -0.2f); sh.rot(42, -0.5f, 0, 0).rot(49, -0.6f, 0, 0);
        clip(SELF_DESTRUCT, sd);

        // Orb Burst: arms raised + charge 0-15, ring released t16 (second ring t26 for large)
        clip(ORB_RING, AnimClip.of(36)
            .bone("rightArm").rot(0, 0, 0, 0).rot(14, -2.9f, 0, 0.4f).rot(16, 0, 0, 1.6f, OUT).rot(22, -0.6f, 0, 0.9f).rot(26, 0, 0, 1.6f, OUT).rot(36, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(14, -2.9f, 0, -0.4f).rot(16, 0, 0, -1.6f, OUT).rot(22, -0.6f, 0, -0.9f).rot(26, 0, 0, -1.6f, OUT).rot(36, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(14, -0.3f, 0, 0).rot(16, 0.15f, 0, 0, OUT).rot(26, 0.15f, 0, 0).rot(36, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(14, 0, -1, 0).pos(16, 0, 1.5f, 0, OUT).pos(22, 0, 0.5f, 0).pos(26, 0, 1.5f, 0, OUT).pos(36, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(14, -0.45f, 0, 0).rot(16, 0.2f, 0, 0).rot(36, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(14, 0, 0, 0.25f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(14, 0, 0, -0.25f).rot(36, 0, 0, 0).done());

        // Ground Pound: both fists overhead on tiptoe 0-13, slam t14, shockwave hold, recover
        clip(POUND, MachineClips.pound(36, 14));

        // Axe Chop: big overhead windup 0-11, chop t12 into the ground, stuck 13-18
        clip(CHOP, MachineClips.overhead(28, 12, -2.9f, 0.6f, 1.0f, "weapon"));
        // Hammer Smash: heavier, two-handed, longer windup, lands t14
        clip(SMASH, MachineClips.overhead(34, 14, -3.1f, 0.85f, 1.3f, "weapon"));

        // Axe Sweep: twist right 0-10, horizontal 220deg sweep t11, follow-through spin
        clip(AXE_SWEEP, MachineClips.sweep(30, 11, 1.0f, "weapon"));
        // Staff Strike: lighter sweep t10
        clip(STAFF_BASH, MachineClips.sweep(22, 10, 0.7f, "weapon"));

        // Spear Thrust: draw back 0-8, lunge-thrust t9
        clip(THRUST, AnimClip.of(22)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, -1.35f, 0.15f, 0).rot(9, -1.6f, 0, 0, OUT).rot(13, -1.55f, 0, 0).rot(22, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0, 3).pos(9, 0, 0, -3, OUT).pos(13, 0, 0, -2).pos(22, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, -1.0f, -0.4f, 0).rot(9, -1.3f, -0.2f, 0, OUT).rot(22, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(8, -0.1f, 0.45f, 0).rot(9, 0.3f, -0.3f, 0, OUT).rot(14, 0.2f, -0.2f, 0).rot(22, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0.5f, 1).pos(9, 0, 1, -2, OUT).pos(22, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0, -0.4f, 0).rot(9, 0.15f, 0.25f, 0).rot(22, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0).rot(9, -0.7f, 0, 0, OUT).rot(22, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, -0.2f, 0, 0).rot(9, 0.5f, 0, 0, OUT).rot(22, 0, 0, 0).done());

        // Rapid Thrusts: four jabs at t8/16/24/32 with alternating shoulder rock
        AnimClip mt = AnimClip.of(40);
        AnimClip.Bone ra = mt.bone("rightArm").rot(0, 0, 0, 0).pos(0, 0, 0, 0);
        AnimClip.Bone bd = mt.bone("body").rot(0, 0, 0, 0).pos(0, 0, 0, 0);
        AnimClip.Bone hd = mt.bone("head").rot(0, 0, 0, 0);
        for (int f = 8; f <= 32; f += 8) {
            float side = (f / 8) % 2 == 0 ? -1 : 1;
            ra.rot(f - 4, -1.4f, 0.1f * side, 0).pos(f - 4, 0, 0, 2.5f).rot(f, -1.6f, 0, 0, OUT).pos(f, 0, 0, -3.5f, OUT);
            bd.rot(f - 4, 0, 0.35f, 0).pos(f - 4, 0, 0.5f, 0.8f).rot(f, 0.25f, -0.25f, 0, OUT).pos(f, 0, 0.5f, -1.5f, OUT);
            hd.rot(f - 4, -0.1f, -0.3f, 0).rot(f, 0.1f, 0.2f, 0);
        }
        ra.rot(40, 0, 0, 0).pos(40, 0, 0, 0); bd.rot(40, 0, 0, 0).pos(40, 0, 0, 0); hd.rot(40, 0, 0, 0);
        mt.bone("leftArm").rot(0, 0, 0, 0).rot(4, -1.0f, -0.4f, 0).rot(34, -1.0f, -0.4f, 0).rot(40, 0, 0, 0);
        mt.bone("rightLeg").rot(0, 0, 0, 0).rot(4, -0.5f, 0, 0).rot(34, -0.5f, 0, 0).rot(40, 0, 0, 0);
        mt.bone("leftLeg").rot(0, 0, 0, 0).rot(4, 0.4f, 0, 0).rot(34, 0.4f, 0, 0).rot(40, 0, 0, 0);
        clip(MULTI, mt);

        // Pike Charge: crouched brace 0-11, spear-levelled sprint 12-27, skid 28-35
        AnimClip pk = AnimClip.of(36);
        pk.bone("rightArm").rot(0, 0, 0, 0).rot(10, -1.5f, 0.1f, 0).rot(27, -1.5f, 0.1f, 0).rot(30, -2.2f, 0, 0.3f).rot(36, 0, 0, 0).pos(0, 0, 0, 0).pos(10, 0, 0, 2).pos(13, 0, 0, -1).pos(27, 0, 0, -1).pos(36, 0, 0, 0);
        pk.bone("leftArm").rot(0, 0, 0, 0).rot(10, -1.2f, -0.4f, 0).rot(27, -1.2f, -0.4f, 0).rot(30, -2.0f, 0, -0.4f).rot(36, 0, 0, 0);
        pk.bone("body").rot(0, 0, 0, 0).rot(10, 0.4f, 0, 0).rot(13, 0.5f, 0, 0).rot(27, 0.5f, 0, 0).rot(30, -0.3f, 0, 0).rot(36, 0, 0, 0)
            .pos(0, 0, 0, 0).pos(10, 0, 2, 1).pos(13, 0, 1, -1).pos(27, 0, 1, -1).pos(30, 0, 0, 1).pos(36, 0, 0, 0);
        pk.bone("head").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(27, -0.3f, 0, 0).rot(30, -0.5f, 0, 0).rot(36, 0, 0, 0);
        AnimClip.Bone prl = pk.bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.5f, 0, 0), pll = pk.bone("leftLeg").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0);
        for (int t = 12; t <= 27; t += 2) { float a = (t / 2) % 2 == 0 ? 1.0f : -1.0f; prl.rot(t, a, 0, 0); pll.rot(t, -a, 0, 0); }
        prl.rot(30, -0.8f, 0, 0).rot(36, 0, 0, 0); pll.rot(30, -0.6f, 0, 0).rot(36, 0, 0, 0);
        clip(PIKE, pk);

        // Hammer Whirl: arm out + twist 0-8, three full turns 9-35 (hits every 7), dizzy
        clip(WHIRL, AnimClip.of(44)
            .bone("body").rot(0, 0, 0, 0).rot(8, 0.1f, 0.7f, 0).rot(9, 0.1f, 0, 0).rot(35, 0.1f, -6 * PI, 0, LINEAR).rot(36, 0, 0, 0.18f, SNAP).rot(39, 0, 0, -0.15f).rot(42, 0, 0, 0.08f).rot(44, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 1.5f, 0).pos(35, 0, 1, 0).pos(44, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, -0.6f, 0.4f, 1.0f).rot(11, -0.3f, 0, 1.5f, OUT).rot(35, -0.3f, 0, 1.5f).rot(38, -0.4f, 0, 0.5f).rot(44, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(11, -0.2f, 0, -1.3f).rot(35, -0.2f, 0, -1.3f).rot(38, 0, 0, -0.4f).rot(44, 0, 0, 0)
            .bone("weapon").rot(0, 0, 0, 0).rot(11, 0, 1.4f, 0).rot(35, 0, 1.4f, 0).rot(38, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0, -0.5f, 0).rot(12, -0.2f, 0, 0).rot(35, -0.2f, 0, 0).rot(37, 0.2f, 0.4f, 0.2f).rot(40, 0, -0.4f, -0.2f).rot(44, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0.2f).rot(16, 0.4f, 0, 0.1f).rot(23, -0.4f, 0, 0.1f).rot(30, 0.4f, 0, 0.1f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, 0.3f, 0, -0.2f).rot(16, -0.4f, 0, -0.1f).rot(23, 0.4f, 0, -0.1f).rot(30, -0.4f, 0, -0.1f).rot(36, 0, 0, 0).done());

        // Staff Volley: staff raised high charging 0-11, points and fires at t12/18/24
        AnimClip sv = AnimClip.of(36);
        AnimClip.Bone svr = sv.bone("rightArm").rot(0, 0, 0, 0).rot(10, -2.8f, 0, 0.2f);
        AnimClip.Bone svb = sv.bone("body").rot(0, 0, 0, 0).rot(10, -0.2f, 0.25f, 0).pos(0, 0, 0, 0);
        for (int f = 12; f <= 24; f += 6) { svr.rot(f, -1.5f, -0.1f, 0, OUT).rot(f + 3, -2.2f, 0, 0.15f); svb.rot(f, 0.1f, 0.1f, 0, OUT).pos(f, 0, 0, 0.8f, OUT).rot(f + 3, -0.1f, 0.2f, 0).pos(f + 3, 0, 0, 0); }
        svr.rot(36, 0, 0, 0); svb.rot(36, 0, 0, 0).pos(36, 0, 0, 0);
        sv.bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.4f, 0, -0.9f).rot(28, -0.4f, 0, -0.9f).rot(36, 0, 0, 0);
        sv.bone("head").rot(0, 0, 0, 0).rot(10, -0.35f, 0, 0).rot(12, 0.05f, 0, 0).rot(28, 0.05f, 0, 0).rot(36, 0, 0, 0);
        sv.bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0.1f).rot(28, 0.3f, 0, 0.1f).rot(36, 0, 0, 0);
        sv.bone("leftLeg").rot(0, 0, 0, 0).rot(10, -0.3f, 0, -0.1f).rot(28, -0.3f, 0, -0.1f).rot(36, 0, 0, 0);
        clip(STAFF_ORBS, sv);

        // Staff Nova: lift staff overhead two-handed 0-15, plunge into ground t16, pulse
        clip(NOVA, AnimClip.of(40)
            .bone("rightArm").rot(0, 0, 0, 0).rot(14, -3.0f, 0, 0.1f).rot(15, -3.05f, 0, 0.1f).rot(16, -0.4f, 0, 0, IN).rot(26, -0.4f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(14, -3.0f, -0.4f, -0.1f).rot(16, -0.5f, -0.5f, 0, IN).rot(26, -0.5f, -0.5f, 0).rot(40, 0, 0, 0)
            .bone("weapon").rot(0, 0, 0, 0).rot(14, -0.5f, 0, 0).rot(16, 1.25f, 0, 0, IN).rot(26, 1.25f, 0, 0).rot(32, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(15, -0.3f, 0, 0).rot(16, 0.5f, 0, 0, IN).rot(26, 0.45f, 0, 0).rot(40, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(15, 0, -1.2f, 0).pos(16, 0, 2, -0.5f, IN).pos(26, 0, 2, -0.5f).pos(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(15, -0.5f, 0, 0).rot(16, 0.3f, 0, 0).rot(26, 0.2f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(16, -0.4f, 0, 0.25f).rot(26, -0.4f, 0, 0.25f).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(16, -0.4f, 0, -0.25f).rot(26, -0.4f, 0, -0.25f).rot(40, 0, 0, 0).done());

        // Shield Bash: shield squared up + crouch 0-9, shoulder-charge 10-14, recover
        clip(SHIELD_BASH, MachineClips.shieldBash(26, 10));
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        Weapon w = e.v.weapon();
        bone("axe").visible = w == Weapon.AXE; bone("spear").visible = w == Weapon.SPEAR;
        bone("hammer").visible = w == Weapon.HAMMER; bone("staff").visible = w == Weapon.STAFF; bone("shield").visible = w == Weapon.SHIELD;
        float c = Mth.cos(ls * 0.9f) * 1.1f * la;
        bone("rightLeg").xRot = c; bone("leftLeg").xRot = -c;
        boolean armed = w != Weapon.NONE && w != Weapon.SHIELD;
        bone("rightArm").xRot = -c * 0.8f - (armed ? 0.35f : 0); bone("leftArm").xRot = c * 0.8f - (w == Weapon.SHIELD ? 0.5f : 0);
        bone("rightArm").zRot = 0.1f + Mth.cos(age * 0.09f) * 0.05f; bone("leftArm").zRot = -0.1f - Mth.cos(age * 0.09f) * 0.05f;
        ModelPart body = bone("body");
        body.y += -Mth.abs(Mth.sin(ls * 0.9f)) * 1.2f * la + Mth.sin(age * 0.1f) * 0.3f;
        body.zRot = Mth.sin(ls * 0.45f) * 0.1f * la;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD; bone("head").xRot = headPitch * Mth.DEG_TO_RAD;
        if (e.getForm() == 1) { body.zRot += Mth.sin(age * 2.5f) * 0.05f; }
    }
}
