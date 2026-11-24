package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.BipedUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import com.nier.automata.unit.machine.MachineVariant.Weapon;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Biped: tall thin frame, long legs and arms, round head. Club / shield toggled per variant. Clips match BipedUnit. */
public class BipedModel extends NierModel<MachineUnit> {
    public BipedModel(ModelPart root) {
        super(root);
        bind(MachineGeo.BIPED_PATHS);

        // Flailing Arms: windmilling overhand blows, right t8/t20, left t14/t26, shuffling forward
        AnimClip fl = AnimClip.of(36);
        AnimClip.Bone ra = fl.bone("rightArm").rot(0, 0, 0, 0), la = fl.bone("leftArm").rot(0, 0, 0, 0);
        AnimClip.Bone bd = fl.bone("body").rot(0, 0, 0, 0).pos(0, 0, 0, 0), hd = fl.bone("head").rot(0, 0, 0, 0);
        AnimClip.Bone rl = fl.bone("rightLeg").rot(0, 0, 0, 0), ll = fl.bone("leftLeg").rot(0, 0, 0, 0);
        for (int h = 8; h <= 26; h += 6) {
            boolean right = (h / 6) % 2 == 1;   // 8,20 right ; 14,26 left
            (right ? ra : la).rot(h - 5, -2.9f, 0, right ? 0.3f : -0.3f).rot(h, -0.45f, right ? -0.4f : 0.4f, 0, OUT);
            (right ? la : ra).rot(h, -2.4f, 0, right ? -0.4f : 0.4f);
            bd.rot(h - 4, 0.0f, right ? 0.35f : -0.35f, 0).rot(h, 0.25f, right ? -0.35f : 0.35f, 0, OUT).pos(h, 0, 0.5f, -1, OUT);
            hd.rot(h - 4, -0.2f, right ? -0.3f : 0.3f, 0).rot(h, 0.2f, right ? 0.2f : -0.2f, 0);
            rl.rot(h, right ? -0.5f : 0.3f, 0, 0); ll.rot(h, right ? 0.3f : -0.5f, 0, 0);
        }
        ra.rot(36, 0, 0, 0); la.rot(36, 0, 0, 0); bd.rot(36, 0, 0, 0).pos(36, 0, 0, 0); hd.rot(36, 0, 0, 0); rl.rot(36, 0, 0, 0); ll.rot(36, 0, 0, 0);
        clip(FLAIL, fl);

        // Charge: crouch 0-9, sprint 10-25 (legs pumping, arms driving), skid 26-33
        AnimClip ch = AnimClip.of(34);
        ch.bone("body").rot(0, 0, 0, 0).rot(9, 0.35f, 0, 0).rot(11, 0.55f, 0, 0).rot(25, 0.55f, 0, 0).rot(28, -0.3f, 0, 0).rot(34, 0, 0, 0)
            .pos(0, 0, 0, 0).pos(9, 0, 2, 1).pos(11, 0, 1, -1).pos(25, 0, 1, -1).pos(28, 0, 1.5f, 1).pos(34, 0, 0, 0);
        ch.bone("head").rot(0, 0, 0, 0).rot(9, -0.3f, 0, 0).rot(25, -0.4f, 0, 0).rot(28, 0.2f, 0, 0).rot(34, 0, 0, 0);
        AnimClip.Bone cra = ch.bone("rightArm").rot(0, 0, 0, 0).rot(9, 0.8f, 0, 0.2f), cla = ch.bone("leftArm").rot(0, 0, 0, 0).rot(9, 0.8f, 0, -0.2f);
        AnimClip.Bone crl = ch.bone("rightLeg").rot(0, 0, 0, 0).rot(9, -0.6f, 0, 0), cll = ch.bone("leftLeg").rot(0, 0, 0, 0).rot(9, 0.4f, 0, 0);
        for (int t = 11; t <= 25; t += 2) { float a = (t / 2) % 2 == 0 ? 1.2f : -1.2f;
            crl.rot(t, a, 0, 0); cll.rot(t, -a, 0, 0); cra.rot(t, -a * 0.9f, 0, 0.15f); cla.rot(t, a * 0.9f, 0, -0.15f); }
        crl.rot(28, -0.9f, 0, 0).rot(34, 0, 0, 0); cll.rot(28, -0.3f, 0, 0).rot(34, 0, 0, 0);
        cra.rot(28, -1.6f, 0, 0.6f).rot(34, 0, 0, 0); cla.rot(28, -1.6f, 0, -0.6f).rot(34, 0, 0, 0);
        clip(CHARGE, ch);

        // Front Kick: lean back + chamber 0-9, snap kick t10, recover
        clip(KICK, AnimClip.of(22)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(7, 0.5f, 0, 0).rot(9, -0.9f, 0, 0).rot(10, -1.7f, 0, 0, OUT).rot(13, -1.5f, 0, 0).rot(22, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(9, 0.15f, 0, 0).rot(13, 0.2f, 0, 0).rot(22, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(9, -0.35f, 0.15f, 0).rot(10, -0.45f, 0, 0, OUT).rot(14, -0.3f, 0, 0).rot(22, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(9, 0, 0, 1).pos(10, 0, 0, 1.5f).pos(22, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(9, -0.5f, 0, 0.9f).rot(14, -0.4f, 0, 0.8f).rot(22, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(9, -0.5f, 0, -0.9f).rot(14, -0.4f, 0, -0.8f).rot(22, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(9, 0.3f, 0, 0).rot(14, 0.25f, 0, 0).rot(22, 0, 0, 0).done());

        // Head Laser: head rears back charging 0-14, beam 15-35 with recoil jitter, cooling shake 36-43
        AnimClip lz = AnimClip.of(44);
        AnimClip.Bone lh = lz.bone("head").rot(0, 0, 0, 0).rot(10, -0.45f, 0, 0).rot(14, -0.5f, 0, 0).rot(15, 0.05f, 0, 0, OUT);
        for (int t = 17; t <= 35; t += 2) lh.rot(t, t % 4 == 1 ? 0.08f : 0.02f, t % 4 == 1 ? 0.04f : -0.04f, 0);
        lh.rot(37, 0.1f, 0.3f, 0).rot(39, 0.1f, -0.3f, 0).rot(41, 0.05f, 0.15f, 0).rot(44, 0, 0, 0);
        lh.scale(0, 1, 1, 1).scale(14, 1.1f, 1.1f, 1.1f).scale(16, 1, 1, 1);
        lz.bone("body").rot(0, 0, 0, 0).rot(14, -0.25f, 0, 0).rot(16, 0.05f, 0, 0).rot(35, 0.05f, 0, 0).rot(44, 0, 0, 0)
            .pos(0, 0, 0, 0).pos(15, 0, 0, 0).pos(17, 0, 0, 0.8f).pos(35, 0, 0, 0.8f).pos(44, 0, 0, 0);
        lz.bone("rightArm").rot(0, 0, 0, 0).rot(14, 0.3f, 0, 0.6f).rot(35, 0.3f, 0, 0.6f).rot(44, 0, 0, 0);
        lz.bone("leftArm").rot(0, 0, 0, 0).rot(14, 0.3f, 0, -0.6f).rot(35, 0.3f, 0, -0.6f).rot(44, 0, 0, 0);
        lz.bone("rightLeg").rot(0, 0, 0, 0).rot(14, -0.3f, 0, 0.1f).rot(35, -0.3f, 0, 0.1f).rot(44, 0, 0, 0);
        lz.bone("leftLeg").rot(0, 0, 0, 0).rot(14, 0.3f, 0, -0.1f).rot(35, 0.3f, 0, -0.1f).rot(44, 0, 0, 0);
        clip(LASER, lz);

        // Overhead Club t13, Club Sweep t11
        clip(CLUB, MachineClips.overhead(30, 13, -2.9f, 0.5f, 1.0f, "club"));
        clip(SWEEP, MachineClips.sweep(28, 11, 1.0f, "club"));

        // Club Combo: forehand sweep t10, backhand t20, overhead finisher t32
        clip(COMBO, AnimClip.of(46)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, -1.4f, 1.3f, 0.2f).rot(10, -1.5f, -1.0f, 0, OUT).rot(17, -1.3f, -1.5f, 0).rot(20, -1.5f, 1.1f, 0.2f, OUT)
                .rot(29, -2.9f, 0.2f, 0.15f).rot(31, -3.0f, 0.2f, 0.15f).rot(32, -0.15f, 0, 0, IN).rot(38, -0.2f, 0, 0).rot(46, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.6f, -0.4f, -0.3f).rot(20, -0.5f, 0.4f, -0.5f).rot(30, -2.5f, -0.5f, -0.2f).rot(32, -0.5f, -0.6f, 0, IN).rot(38, -0.5f, -0.6f, 0).rot(46, 0, 0, 0)
            .bone("club").rot(0, 0, 0, 0).rot(20, 0, 0, 0).rot(30, -0.5f, 0, 0).rot(32, 0.5f, 0, 0, IN).rot(38, 0.5f, 0, 0).rot(42, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(8, 0.1f, 0.8f, 0).rot(10, 0.15f, -0.8f, 0, OUT).rot(17, 0.1f, -1.0f, 0).rot(20, 0.15f, 0.7f, 0, OUT)
                .rot(30, -0.35f, 0.1f, 0).rot(32, 0.5f, 0, 0, IN).rot(38, 0.45f, 0, 0).rot(46, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0.5f, 0.5f).pos(10, 0, 0.5f, -1.5f, OUT).pos(18, 0, 0.5f, -0.5f).pos(20, 0, 0.5f, -1.5f, OUT)
                .pos(30, 0, -1, 0).pos(32, 0, 1.8f, -2, IN).pos(38, 0, 1.5f, -1.5f).pos(46, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0, -0.5f, 0).rot(10, 0.1f, 0.5f, 0).rot(18, 0, 0.6f, 0).rot(20, 0.1f, -0.4f, 0).rot(30, -0.3f, 0, 0).rot(32, 0.3f, 0, 0).rot(46, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.4f, 0, 0).rot(15, 0, 0, 0).rot(20, -0.4f, 0, 0).rot(28, 0.2f, 0, 0).rot(32, -0.6f, 0, 0.1f, IN).rot(38, -0.6f, 0, 0.1f).rot(46, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0).rot(20, 0.3f, 0, 0).rot(32, 0.45f, 0, -0.1f, IN).rot(38, 0.4f, 0, -0.1f).rot(46, 0, 0, 0).done());

        // Jump Slam: crouch, launch t9, weapon/fists brought down on landing ~t21
        clip(JUMP_SLAM, MachineClips.press(36, 9, 21, 0.25f));

        clip(LIGHTNING, MachineClips.pound(46, 26));

        // Stomp (large): knee raised high 0-12, stamp t13, ground shockwave
        clip(STOMP, MachineClips.stomp(30, 13));

        // Double Fist Slam (large): t14
        clip(FIST_SLAM, MachineClips.pound(34, 14));

        // Orb Volley (large): arms levelled like cannons, alternating recoil at t12..32
        AnimClip ov = AnimClip.of(40);
        AnimClip.Bone ovr = ov.bone("rightArm").rot(0, 0, 0, 0).rot(10, -1.55f, 0.1f, 0).pos(0, 0, 0, 0);
        AnimClip.Bone ovl = ov.bone("leftArm").rot(0, 0, 0, 0).rot(10, -1.55f, -0.1f, 0).pos(0, 0, 0, 0);
        AnimClip.Bone ovb = ov.bone("body").rot(0, 0, 0, 0).rot(10, 0.1f, 0, 0);
        for (int f = 12; f <= 32; f += 4) { boolean r = (f / 4) % 2 == 1;
            AnimClip.Bone a = r ? ovr : ovl;
            a.pos(f, 0, 0, 2.5f, OUT).rot(f, -1.8f, 0, 0, OUT).pos(f + 3, 0, 0, 0).rot(f + 3, -1.55f, 0, 0);
            ovb.rot(f, 0.05f, r ? 0.15f : -0.15f, 0, OUT); }
        ovr.rot(40, 0, 0, 0).pos(40, 0, 0, 0); ovl.rot(40, 0, 0, 0).pos(40, 0, 0, 0); ovb.rot(40, 0, 0, 0);
        ov.bone("head").rot(0, 0, 0, 0).rot(10, 0.1f, 0, 0).rot(34, 0.1f, 0, 0).rot(40, 0, 0, 0);
        ov.bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0.1f).rot(34, -0.3f, 0, 0.1f).rot(40, 0, 0, 0);
        ov.bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.3f, 0, -0.1f).rot(34, 0.3f, 0, -0.1f).rot(40, 0, 0, 0);
        clip(ORB_VOLLEY, ov);

        clip(SHIELD_BASH, MachineClips.shieldBash(26, 10));
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        var vv = e.v; boolean gun = vv.weapon() == com.nier.automata.unit.machine.MachineVariant.Weapon.GUN;
        bone("hat").visible = gun; bone("ears").visible = vv.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.BRONZE;
        bone("jester").visible = vv.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.CLOWN;
        bone("antlers").visible = vv.skin() == com.nier.automata.unit.machine.MachineVariant.Skin.GRAVE;
        Weapon w = e.v.weapon();
        bone("club").visible = w == Weapon.CLUB || w == Weapon.SHIELD;
        bone("shield").visible = w == Weapon.SHIELD;
        float c = Mth.cos(ls * 0.6662f) * 1.2f * la;
        bone("rightLeg").xRot = c; bone("leftLeg").xRot = -c;
        bone("rightArm").xRot = -c * 0.7f - (bone("club").visible ? 0.3f : 0); bone("leftArm").xRot = c * 0.7f;
        bone("rightArm").zRot = 0.08f + Mth.cos(age * 0.07f) * 0.04f; bone("leftArm").zRot = -0.08f - Mth.cos(age * 0.07f) * 0.04f;
        ModelPart body = bone("body");
        body.xRot = 0.08f + la * 0.15f;  // slight hunch, more when walking
        body.y += -Mth.abs(Mth.cos(ls * 0.6662f)) * 0.8f * la + Mth.sin(age * 0.08f) * 0.25f;
        bone("head").yRot = headYaw * Mth.DEG_TO_RAD; bone("head").xRot = headPitch * Mth.DEG_TO_RAD - body.xRot;
    }
}
