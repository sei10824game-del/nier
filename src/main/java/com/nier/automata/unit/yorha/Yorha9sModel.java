package com.nier.automata.unit.yorha;

import static com.nier.automata.unit.yorha.Yorha9sUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** Scanner android S: hooded jacket, spiky fringe, cyan visor, one-hand sword, greatsword on back, spear, slate pod. */
public class Yorha9sModel extends AndroidModel<Yorha9sUnit> {
    public Yorha9sModel(ModelPart root) {
        super(root);
        bind(paths("torso/hood", "torso/jacketTail", "torso/backBlade", "torso/leftArm/heldGreat", "torso/rightArm/heldSpear"));
        clip(COMBO, combo()); clip(DUAL, dual()); clip(SPEAR, spear()); clip(HACK_STUN, hack(40, 30)); clip(HACK_DESTROY, hack(60, 50));
        clip(POD_GUN, podGunClip()); clip(POD_MISSILE, missile()); clip(EVADE, evadeClip());
    }

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition(); PartDefinition r = m.getRoot();
        base(r);
        PartDefinition torso = r.getChild("torso"), head = torso.getChild("head");
        head.addOrReplaceChild("hairCap", CubeListBuilder.create().texOffs(24, 32).addBox(-4.5f, -8.5f, -4.5f, 9, 3, 9)
                .texOffs(60, 32).addBox(-1, -10, -3, 2, 2, 3).texOffs(72, 32).addBox(-4.5f, -5.2f, -4.6f, 9, 2, 1), PartPose.ZERO);
        torso.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(0, 32).addBox(-4.5f, -1, -1, 9, 4, 2), PartPose.offset(0, -12, 3));
        torso.addOrReplaceChild("jacketTail", CubeListBuilder.create().texOffs(92, 32).addBox(-4, 0, -0.5f, 8, 6, 1), PartPose.offset(0, 0, 2.2f));
        torso.addOrReplaceChild("backBlade", CubeListBuilder.create().texOffs(84, 48).addBox(-2, -12, 0, 4, 20, 1), PartPose.offsetAndRotation(0, -6, 2.6f, 0, 0, -0.55f));
        torso.getChild("rightArm").getChild("rightWeapon").addOrReplaceChild("blade", sword(0, 48, 12), PartPose.ZERO);
        torso.getChild("leftArm").addOrReplaceChild("heldGreat", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5f, -0.5f, -1, 1, 1, 4)
                .texOffs(76, 48).addBox(-0.5f, -2.5f, -3, 1, 5, 2).texOffs(32, 54).addBox(-0.5f, -2, -21, 1, 4, 18), PartPose.offsetAndRotation(0.5f, 10, 0, 0.35f, 0, 0));
        torso.getChild("rightArm").addOrReplaceChild("heldSpear", CubeListBuilder.create().texOffs(0, 80).addBox(-0.5f, -0.5f, -22, 1, 1, 28)
                .texOffs(60, 80).addBox(-0.5f, -1.5f, -27, 1, 3, 5), PartPose.offsetAndRotation(-0.5f, 10, 0, 0.1f, 0, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    @Override protected void idle(Yorha9sUnit e, float ls, float la, float age, float hy, float hp) {
        super.idle(e, ls, la, age, hy, hp);
        for (String b : new String[]{"heldGreat", "heldSpear"}) bone(b).xScale = bone(b).yScale = bone(b).zScale = 0;
        bone("jacketTail").xRot = la * 0.5f + Mth.sin(age * 0.09f) * 0.04f;
        bone("hood").xRot = -la * 0.1f;
    }

    private static void hide(AnimClip c, String bone, int len) { c.bone(bone).scale(0, 0, 0, 0).scale(len, 0, 0, 0); }
    private static void show(AnimClip c, String bone, int len) { c.bone(bone).scale(0, 1, 1, 1).scale(len, 1, 1, 1); }

    // slashes 5 / 11, lunge 16, thrust 18
    private static AnimClip combo() {
        return AnimClip.of(24)
            .bone("rightArm").rot(0, 0, 0, 0).rot(3, -1.6f, 1.1f, 0.8f).rot(5, -1.3f, -1.0f, -0.5f, Ease.OUT).rot(9, -2.5f, -0.4f, -0.3f)
                .rot(11, -0.5f, 0.4f, 0.6f, Ease.OUT).rot(15, -0.4f, 0, 0.3f).rot(18, -1.6f, 0, 0, Ease.OUT).rot(21, -1.5f, 0, 0).rot(24, 0, 0, 0)
            .bone("rightWeapon").rot(0, 0.35f, 0, 0).rot(5, 0.2f, 0, -0.4f).rot(11, 0.3f, 0, 0.5f).rot(15, 1.6f, 0, 0).rot(18, 1.57f, 0, 0).rot(24, 0.35f, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(3, 0, 0.6f, 0).rot(5, 0.15f, -0.5f, 0, Ease.OUT).rot(9, -0.05f, -0.3f, 0).rot(11, 0.2f, 0.4f, 0, Ease.OUT)
                .rot(15, 0, 0.6f, 0).rot(18, 0.3f, -0.4f, 0, Ease.OUT).rot(24, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(5, 0, 0.4f, 0).rot(11, 0, -0.3f, 0).rot(18, -0.2f, 0.4f, 0).rot(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(5, 0.5f, 0, -0.7f).rot(11, -0.4f, 0, -0.3f).rot(18, 0.9f, 0, -0.6f).rot(24, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(5, -0.4f, 0, 0).rot(11, 0.3f, 0, 0).rot(18, -1.0f, 0, 0).rot(24, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(5, 0.3f, 0, 0).rot(11, -0.3f, 0, 0).rot(18, 0.7f, 0, 0).rot(24, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(11, 0, 0.5f, -1).pos(15, 0, 1, 1).pos(18, 0, 2, -6, Ease.OUT).pos(24, 0, 0, 0)
            .bone("jacketTail").rot(0, 0, 0, 0).rot(18, 0.9f, 0, 0).rot(24, 0, 0, 0).done();
    }

    // rising launcher 7, spins 17 / 24 (sword + left greatsword)
    private static AnimClip dual() {
        AnimClip c = AnimClip.of(34);
        hide(c, "backBlade", 34); show(c, "heldGreat", 34);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(5, 0.5f, 0, 0.4f).rot(7, -2.8f, 0, 0.3f, Ease.OUT).rot(12, -1.6f, 0, 1.3f).rot(24, -1.6f, 0, 1.4f).rot(29, -1.5f, 0, 1.2f).rot(34, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(5, 0.4f, 0, -0.3f).rot(7, -1.0f, 0, -0.6f).rot(12, -1.6f, 0, -1.3f).rot(24, -1.6f, 0, -1.4f).rot(29, -1.5f, 0, -1.2f).rot(34, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(5, 0.3f, 0, 0).rot(7, -0.3f, 0, 0, Ease.OUT).rot(12, 0.1f, 0.8f, 0)
                .rot(17, 0.1f, -3.14f, 0, Ease.LINEAR).rot(24, 0.1f, -9.42f, 0, Ease.LINEAR).rot(28, 0.05f, -12.56f, 0).rot(28.01f, 0.05f, 0, 0, Ease.SNAP).rot(34, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(7, -0.4f, 0, 0).rot(12, 0, -0.3f, 0).rot(34, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(5, -0.6f, 0, 0).rot(7, 0.2f, 0, 0).rot(12, -0.2f, 0, 0.3f).rot(28, -0.2f, 0, 0.3f).rot(34, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(5, 0.4f, 0, 0).rot(7, -0.3f, 0, 0).rot(12, 0.2f, 0, -0.3f).rot(28, 0.2f, 0, -0.3f).rot(34, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(5, 0, 2, 0).pos(7, 0, -3, -1, Ease.OUT).pos(12, 0, 0, 0).pos(24, 0, -1, 0).pos(34, 0, 0, 0)
            .bone("jacketTail").rot(0, 0, 0, 0).rot(12, 1.2f, 0, 0).rot(28, 1.2f, 0, 0).rot(34, 0, 0, 0).done();
    }

    // dash 8, jabs 10/12/14, thrust 19
    private static AnimClip spear() {
        AnimClip c = AnimClip.of(28);
        show(c, "heldSpear", 28);
        AnimClip.Bone ra = c.bone("rightArm").rot(0, 0, 0, 0).rot(6, -0.6f, 0.4f, 0.3f).rot(8, -0.8f, 0.3f, 0.2f);
        for (int t = 10; t <= 14; t += 2) ra.rot(t, -1.5f, 0, 0, Ease.OUT).rot(t + 1, -1.0f, 0.2f, 0.2f);
        ra.rot(17, -0.6f, 0.6f, 0.3f).rot(19, -1.6f, -0.1f, 0, Ease.OUT).rot(23, -1.55f, 0, 0).rot(28, 0, 0, 0);
        return c.bone("heldSpear").pos(0, 0, 0, 4).pos(8, 0, 0, 4).pos(10, 0, 0, -2, Ease.OUT).pos(11, 0, 0, 3).pos(12, 0, 0, -2, Ease.OUT).pos(13, 0, 0, 3)
                .pos(14, 0, 0, -2, Ease.OUT).pos(17, 0, 0, 6).pos(19, 0, 0, -6, Ease.OUT).pos(28, 0, 0, 4)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, -0.9f, -0.5f, 0).rot(10, -1.3f, -0.4f, 0).rot(19, -1.4f, -0.5f, 0).rot(28, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, -0.1f, 0.6f, 0).rot(10, 0.3f, 0.3f, 0).rot(17, 0.1f, 0.7f, 0).rot(19, 0.45f, -0.2f, 0, Ease.OUT).rot(28, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, 0, -0.5f, 0).rot(19, -0.3f, 0.2f, 0).rot(28, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, 0.5f, 0, 0).rot(10, 0.8f, 0, 0).rot(19, 0.9f, 0, 0).rot(28, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, -0.4f, 0, 0).rot(10, -1.0f, 0, 0).rot(19, -1.1f, 0, 0).rot(28, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(6, 0, 1.5f, 1).pos(10, 0, 2.5f, -4, Ease.OUT).pos(19, 0, 3, -6, Ease.OUT).pos(28, 0, 0, 0)
            .bone("jacketTail").rot(0, 0, 0, 0).rot(10, 1.1f, 0, 0).rot(19, 1.0f, 0, 0).rot(28, 0, 0, 0).done();
    }

    /** Hack channel (len ticks, release at 'hit'): left palm forward, right hand to temple, trembling, then a flick / yank. */
    private static AnimClip hack(int len, int hit) {
        AnimClip c = AnimClip.of(len);
        AnimClip.Bone la = c.bone("leftArm").rot(0, 0, 0, 0).rot(6, -1.55f, -0.2f, 0);
        for (int t = 8; t < hit - 1; t += 2) la.rot(t, -1.55f + (t % 4 == 0 ? 0.04f : -0.04f), -0.2f, (t % 4 == 0 ? 0.03f : -0.03f));
        la.rot(hit - 2, -1.2f, -0.1f, 0).rot(hit, -1.8f, -0.3f, 0, Ease.OUT).rot(hit + 4, -1.7f, -0.3f, 0).rot(len, 0, 0, 0);
        AnimClip.Bone pod = c.bone("pod").pos(0, 0, 0, 0).pos(6, 3, 0, -4);
        for (int t = 8; t < hit; t += 4) pod.pos(t, 3, -1 + (t % 8 == 0 ? 1 : 0), -4);
        pod.pos(hit, 3, -2, -6, Ease.OUT).pos(len, 0, 0, 0);
        boolean big = len > 40;
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(6, -2.4f, 0, -0.9f).rot(hit - 2, -2.5f, 0, -0.95f)
                .rot(hit, big ? -1.7f : -2.2f, big ? 0.4f : 0, big ? 0.3f : -0.8f, Ease.OUT).rot(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, 0.15f, 0, 0.25f).rot(hit - 2, 0.25f, 0, 0.3f).rot(hit, -0.2f, 0, 0, Ease.OUT).rot(len, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, -0.05f, -0.3f, 0).rot(hit - 2, 0.1f, -0.35f, 0).rot(hit, big ? -0.25f : 0.2f, -0.1f, 0, Ease.OUT).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, 0.35f, 0, 0.1f).rot(len - 4, 0.35f, 0, 0.1f).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, -0.3f, 0, -0.1f).rot(len - 4, -0.3f, 0, -0.1f).rot(len, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(6, 0, 1, 1).pos(hit, 0, big ? 0 : 1, big ? 2 : -1, Ease.OUT).pos(len, 0, 0, 0).done();
    }

    // missiles at 10 / 14 / 18 / 22
    private static AnimClip missile() {
        AnimClip c = AnimClip.of(40);
        AnimClip.Bone pod = c.bone("pod").pos(0, 0, 0, 0).pos(8, 5, -10, -2, Ease.OUT).rot(0, 0, 0, 0).rot(8, -0.8f, 0, 0);
        AnimClip.Bone gun = c.bone("podGun").pos(0, 0, 0, 0);
        for (int t = 10; t <= 22; t += 4) { pod.rot(t, -1.0f, 0, (t % 8 == 2) ? 0.2f : -0.2f, Ease.SNAP).rot(t + 2, -0.8f, 0, 0);
            gun.pos(t, 0, 0, 2, Ease.SNAP).pos(t + 2, 0, 0, 0); }
        pod.pos(30, 5, -10, -2).pos(40, 0, 0, 0).rot(30, -0.8f, 0, 0).rot(40, 0, 0, 0);
        return c.bone("leftArm").rot(0, 0, 0, 0).rot(8, -2.9f, 0.2f, -0.2f).rot(10, -1.6f, 0, 0, Ease.OUT).rot(30, -1.5f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, 0.3f, 0, 0.4f).rot(30, 0.3f, 0, 0.4f).rot(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(8, -0.15f, -0.3f, 0).rot(10, 0.05f, -0.3f, 0).rot(30, 0.05f, -0.3f, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, -0.4f, 0.3f, 0).rot(12, 0, 0.3f, 0).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0).rot(30, 0.3f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(30, -0.3f, 0, 0).rot(40, 0, 0, 0).done();
    }
}
