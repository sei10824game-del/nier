package com.nier.automata.unit.yorha;

import static com.nier.automata.unit.yorha.A2Unit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** Attacker android A: long auburn hair + headband, scarf wrap, heavy gauntlets, blade, back greatsword, spear. */
public class A2Model extends AndroidModel<A2Unit> {
    public A2Model(ModelPart root) {
        super(root);
        bind(paths("torso/head/hairBack", "torso/scarf", "torso/backBlade", "torso/rightArm/heldGreat", "torso/rightArm/heldSpear"));
        clip(BLADE, blade()); clip(GREAT, great()); clip(SPEAR, spear()); clip(FISTS, fists()); clip(DASH, dash());
        clip(POD_GUN, podGunClip()); clip(EVADE, evadeClip()); clip(BERSERK, berserk());
    }

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition(); PartDefinition r = m.getRoot();
        base(r);
        PartDefinition torso = r.getChild("torso"), head = torso.getChild("head");
        PartDefinition ra = torso.getChild("rightArm"), la = torso.getChild("leftArm");
        head.addOrReplaceChild("hairCap", CubeListBuilder.create().texOffs(0, 32).addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
                .texOffs(56, 32).addBox(-4.5f, -6.6f, -4.7f, 9, 1, 1), PartPose.ZERO);
        head.addOrReplaceChild("hairBack", CubeListBuilder.create().texOffs(36, 32).addBox(-4.5f, 0, -0.5f, 9, 14, 1), PartPose.offsetAndRotation(0, -6, 4.2f, 0.1f, 0, 0));
        torso.addOrReplaceChild("scarf", CubeListBuilder.create().texOffs(76, 32).addBox(-1, 0, 0, 2, 8, 1), PartPose.offsetAndRotation(2, -11, 2.2f, 0.2f, 0, 0.1f));
        la.addOrReplaceChild("gauntlet", CubeListBuilder.create().texOffs(84, 32).addBox(-1.5f, 4, -2.5f, 4, 7, 5), PartPose.ZERO);
        ra.addOrReplaceChild("rightGauntlet", CubeListBuilder.create().texOffs(102, 32).addBox(-2.5f, 4, -2.5f, 4, 7, 5), PartPose.ZERO);
        torso.addOrReplaceChild("backBlade", CubeListBuilder.create().texOffs(84, 48).addBox(-2, -14, 0, 4, 24, 1), PartPose.offsetAndRotation(0, -6, 2.6f, 0, 0, 0.5f));
        ra.getChild("rightWeapon").addOrReplaceChild("blade", sword(0, 48, 15), PartPose.ZERO);
        ra.addOrReplaceChild("heldGreat", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5f, -0.5f, -1, 1, 1, 4)
                .texOffs(76, 48).addBox(-0.5f, -2.5f, -3, 1, 5, 2).texOffs(32, 54).addBox(-0.5f, -2, -23, 1, 4, 20), PartPose.offsetAndRotation(-0.5f, 10, 0, 0.35f, 0, 0));
        ra.addOrReplaceChild("heldSpear", CubeListBuilder.create().texOffs(0, 80).addBox(-0.5f, -0.5f, -22, 1, 1, 28)
                .texOffs(60, 80).addBox(-0.5f, -1.5f, -27, 1, 3, 5), PartPose.offsetAndRotation(-0.5f, 10, 0, 0.1f, 0, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    @Override protected void idle(A2Unit e, float ls, float la, float age, float hy, float hp) {
        super.idle(e, ls, la, age, hy, hp);
        for (String b : new String[]{"heldGreat", "heldSpear"}) bone(b).xScale = bone(b).yScale = bone(b).zScale = 0;
        bone("hairBack").xRot = 0.1f + la * 0.6f + Mth.sin(age * 0.07f) * 0.04f;
        bone("scarf").xRot = 0.2f + la * 0.8f + Mth.sin(age * 0.13f) * 0.1f;
        if (e.getForm() == 1) {  // berserk: hunched, predatory stance, twitching
            bone("torso").xRot += 0.3f; bone("head").xRot -= 0.25f;
            bone("rightArm").zRot += 0.3f; bone("leftArm").zRot -= 0.3f;
            bone("head").zRot = Mth.sin(age * 0.9f) * 0.03f;
        }
    }

    private static void show(AnimClip c, String bone, int len) { c.bone(bone).scale(0, 1, 1, 1).scale(len, 1, 1, 1); }
    private static void hideBlade(AnimClip c, int len) { c.bone("rightWeapon").scale(0, 0, 0, 0).scale(len, 0, 0, 0); }

    // slashes 4 / 9 / 14, spinning kick 20
    private static AnimClip blade() {
        return AnimClip.of(24).blend(2)
            .bone("rightArm").rot(0, 0, 0, 0).rot(2, -1.8f, 1.0f, 0.8f).rot(4, -1.2f, -1.1f, -0.5f, Ease.OUT).rot(7, -2.7f, 0, 0.4f)
                .rot(9, -0.5f, 0, -0.3f, Ease.OUT).rot(12, -1.5f, -1.0f, -0.4f).rot(14, -1.6f, 1.1f, 0.7f, Ease.OUT).rot(18, 0.4f, 0, 0.9f).rot(24, 0, 0, 0)
            .bone("rightWeapon").rot(0, 0.35f, 0, 0).rot(4, 0.2f, 0, -0.5f).rot(9, 0.1f, 0, 0).rot(14, 0.3f, 0, 0.6f).rot(24, 0.35f, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(2, 0, 0.6f, 0).rot(4, 0.2f, -0.6f, 0, Ease.OUT).rot(7, -0.15f, -0.2f, 0).rot(9, 0.35f, 0, 0, Ease.OUT)
                .rot(12, 0.1f, -0.7f, 0).rot(14, 0.2f, 0.7f, 0, Ease.OUT).rot(17, -0.2f, 1.6f, 0).rot(20, -0.35f, 3.6f, 0, Ease.OUT).rot(23, -0.1f, 6.28f, 0)
                .rot(23.01f, -0.1f, 0, 0, Ease.SNAP).rot(24, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(4, 0, 0.4f, 0).rot(9, 0.2f, 0, 0).rot(14, 0, -0.4f, 0).rot(20, 0.2f, -0.6f, 0).rot(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(4, 0.6f, 0, -0.7f).rot(9, 0.4f, 0, -0.4f).rot(14, 0.7f, 0, -0.8f).rot(20, -0.4f, 0, -1.4f).rot(24, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(4, -0.4f, 0, 0).rot(9, -0.6f, 0, 0).rot(14, 0.3f, 0, 0).rot(17, -0.5f, 0, 0.3f).rot(20, -1.7f, 0, 0.6f, Ease.OUT).rot(24, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(4, 0.4f, 0, 0).rot(9, 0.5f, 0, 0).rot(14, -0.3f, 0, 0).rot(20, 0.2f, 0, -0.1f).rot(24, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(4, 0, 0.5f, -1).pos(9, 0, 1.5f, -2).pos(14, 0, 0.5f, -2).pos(20, 0, -1, -2).pos(24, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(9, 0.6f, 0, 0).rot(20, 1.2f, 0, -0.4f).rot(24, 0.1f, 0, 0).done();
    }

    // spin-up AoE 12, lunge 22, overhead cleave 25
    private static AnimClip great() {
        AnimClip c = AnimClip.of(36);
        c.bone("backBlade").scale(0, 0, 0, 0).scale(36, 0, 0, 0);
        show(c, "heldGreat", 36); hideBlade(c, 36);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(6, -1.4f, 1.3f, 0.9f).rot(12, -1.5f, -1.3f, 0, Ease.LINEAR).rot(16, -0.8f, -0.6f, 0)
                .rot(22, -3.0f, 0, 0.1f, Ease.OUT).rot(25, -0.4f, 0, 0, Ease.IN).rot(30, -0.35f, 0, 0).rot(36, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, -1.1f, 1.0f, 0.2f).rot(12, -1.2f, -0.9f, -0.2f).rot(22, -2.9f, 0, -0.4f, Ease.OUT).rot(25, -0.5f, 0, -0.3f, Ease.IN).rot(36, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, 0.1f, 1.2f, 0).rot(12, 0.15f, -5.08f, 0, Ease.LINEAR).rot(15, 0.1f, -6.28f, 0).rot(15.01f, 0.1f, 0, 0, Ease.SNAP)
                .rot(22, -0.4f, 0, 0).rot(25, 0.6f, 0, 0, Ease.IN).rot(30, 0.55f, 0, 0).rot(36, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, 0, -0.8f, 0).rot(12, 0, 0.3f, 0).rot(22, -0.3f, 0, 0).rot(25, 0.25f, 0, 0).rot(36, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, -0.2f, 0, 0.3f).rot(12, -0.2f, 0, 0.4f).rot(22, 0.2f, 0, 0).rot(25, -0.9f, 0, 0.1f).rot(30, -0.9f, 0, 0.1f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, 0.2f, 0, -0.3f).rot(12, 0.2f, 0, -0.4f).rot(22, -0.2f, 0, 0).rot(25, 0.8f, 0, -0.1f).rot(30, 0.8f, 0, -0.1f).rot(36, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(6, 0, 1, 0).pos(12, 0, 1.5f, 0).pos(22, 0, -1, -2).pos(25, 0, 3, -5, Ease.IN).pos(30, 0, 3, -5).pos(36, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(12, 1.3f, 0, 0.5f).rot(22, 0.3f, 0, 0).rot(25, 0.9f, 0, 0).rot(36, 0.1f, 0, 0).done();
    }

    // whirl hits 8/12/16/20, thrust 25
    private static AnimClip spear() {
        AnimClip c = AnimClip.of(30);
        show(c, "heldSpear", 30); hideBlade(c, 30);
        return c.bone("heldSpear").rot(0, 0.1f, 0, 0).rot(6, 1.57f, 0, 0).rot(20, 1.57f, 0, 25.1f, Ease.LINEAR).rot(20.01f, 1.57f, 0, 0, Ease.SNAP)
                .rot(23, 0.1f, 0, 0).rot(25, 0.0f, 0, 0).pos(0, 0, 0, 4).pos(23, 0, 0, 6).pos(25, 0, 0, -6, Ease.OUT).pos(30, 0, 0, 4)
            .bone("rightArm").rot(0, 0, 0, 0).rot(6, -1.57f, 0, 0).rot(20, -1.5f, 0.1f, 0).rot(23, -0.7f, 0.5f, 0.3f).rot(25, -1.6f, 0, 0, Ease.OUT).rot(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, -0.4f, 0, -1.3f).rot(20, -0.4f, 0, -1.3f).rot(23, -1.2f, -0.5f, 0).rot(25, -1.4f, -0.5f, 0).rot(30, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, 0.1f, 0.3f, 0).rot(8, 0.1f, -0.3f, 0).rot(12, 0.1f, 0.3f, 0).rot(16, 0.1f, -0.3f, 0).rot(20, 0.1f, 0.3f, 0)
                .rot(23, 0, 0.7f, 0).rot(25, 0.4f, -0.2f, 0, Ease.OUT).rot(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0, 0.2f, 0).rot(16, 0, -0.2f, 0).rot(25, -0.3f, 0.2f, 0).rot(30, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, -0.2f, 0, 0.25f).rot(20, -0.2f, 0, 0.25f).rot(25, 0.8f, 0, 0).rot(30, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, 0.2f, 0, -0.25f).rot(20, 0.2f, 0, -0.25f).rot(25, -1.0f, 0, 0).rot(30, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(6, 0, 1.5f, 0).pos(20, 0, 1.5f, 0).pos(25, 0, 2.5f, -6, Ease.OUT).pos(30, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(10, 0.8f, 0, 0.4f).rot(18, 0.8f, 0, -0.4f).rot(25, 1.0f, 0, 0).rot(30, 0.1f, 0, 0).done();
    }

    // jab 4, cross 8, hook 12, uppercut 19
    private static AnimClip fists() {
        AnimClip c = AnimClip.of(26).blend(2);
        hideBlade(c, 26);
        return c.bone("leftArm").rot(0, 0, 0, 0).rot(2, -0.9f, 0, -0.2f).rot(4, -1.7f, -0.2f, 0, Ease.OUT).rot(6, -0.9f, 0, -0.2f)
                .rot(10, -1.0f, 0, -0.3f).rot(12, -1.5f, -1.2f, -0.6f, Ease.OUT).rot(15, -0.9f, 0, -0.3f).rot(26, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(4, -0.9f, 0, 0.2f).rot(6, -0.8f, 0, 0.3f).rot(8, -1.7f, 0.3f, 0, Ease.OUT).rot(10, -0.9f, 0, 0.2f)
                .rot(16, 0.3f, 0, 0.3f).rot(19, -2.9f, 0, -0.1f, Ease.OUT).rot(22, -2.8f, 0, 0).rot(26, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(2, 0.1f, 0.3f, 0).rot(4, 0.15f, -0.4f, 0, Ease.OUT).rot(8, 0.15f, 0.5f, 0, Ease.OUT)
                .rot(12, 0.1f, -0.8f, 0, Ease.OUT).rot(16, 0.45f, 0.4f, 0).rot(19, -0.35f, -0.2f, 0, Ease.OUT).rot(26, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(4, 0.1f, 0.3f, 0).rot(8, 0.1f, -0.4f, 0).rot(12, 0.1f, 0.6f, 0).rot(16, 0.3f, 0, 0).rot(19, -0.4f, 0, 0).rot(26, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(4, 0.3f, 0, 0).rot(8, -0.3f, 0, 0).rot(16, -1.0f, 0, 0).rot(19, 0.2f, 0, 0).rot(26, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(4, -0.4f, 0, 0).rot(8, 0.2f, 0, 0).rot(16, 0.6f, 0, 0).rot(19, -0.3f, 0, 0).rot(26, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(4, 0, 0.5f, -1).pos(8, 0, 0.5f, -2).pos(12, 0, 0.5f, -2).pos(16, 0, 4, -2).pos(19, 0, -4, -3, Ease.OUT).pos(23, 0, -1, -3).pos(26, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(16, 0.2f, 0, 0).rot(19, 1.1f, 0, 0).rot(26, 0.1f, 0, 0).done();
    }

    // burst at 3, cutting through 6-8
    private static AnimClip dash() {
        return AnimClip.of(16).blend(2)
            .bone("rightArm").rot(0, 0, 0, 0).rot(3, 0.4f, -0.8f, 1.2f).rot(6, -1.4f, -1.4f, -0.2f, Ease.OUT).rot(9, -1.2f, -1.6f, -0.3f).rot(16, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(3, 0.9f, 0, -0.4f).rot(9, 0.9f, 0, -0.6f).rot(16, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(3, 0.6f, 0.6f, 0).rot(6, 0.5f, -0.6f, 0, Ease.OUT).rot(9, 0.4f, -0.7f, 0).rot(16, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(3, -0.5f, -0.4f, 0).rot(9, -0.4f, 0.5f, 0).rot(16, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(3, 0.9f, 0, 0).rot(6, -0.8f, 0, 0).rot(9, -0.7f, 0, 0).rot(16, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(3, -0.7f, 0, 0).rot(6, 0.9f, 0, 0).rot(9, 0.8f, 0, 0).rot(16, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(3, 0, 3, 2).pos(6, 0, 2, -3, Ease.OUT).pos(16, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(4, 1.4f, 0, 0).rot(10, 1.2f, 0, 0).rot(16, 0.1f, 0, 0)
            .bone("scarf").rot(0, 0.2f, 0, 0).rot(4, 1.5f, 0, 0).rot(12, 1.3f, 0, 0).rot(16, 0.2f, 0, 0).done();
    }

    // crouch and clench 0-12, roar / glow at 14
    private static AnimClip berserk() {
        AnimClip c = AnimClip.of(30).blend(3);
        AnimClip.Bone torso = c.bone("torso").rot(0, 0, 0, 0).rot(10, 0.8f, 0, 0);
        for (int t = 2; t < 12; t += 2) torso.rot(t, 0.08f * t, 0, t % 4 == 0 ? 0.05f : -0.05f);
        torso.rot(14, -0.6f, 0, 0, Ease.OUT).rot(22, -0.5f, 0, 0).rot(30, 0.3f, 0, 0);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(10, -0.9f, -0.5f, -0.4f).rot(14, -0.3f, 0, 1.4f, Ease.OUT).rot(22, -0.4f, 0, 1.3f).rot(30, 0, 0, 0.3f)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.9f, 0.5f, 0.4f).rot(14, -0.3f, 0, -1.4f, Ease.OUT).rot(22, -0.4f, 0, -1.3f).rot(30, 0, 0, -0.3f)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0.6f, 0, 0).rot(14, -0.9f, 0, 0, Ease.OUT).rot(22, -0.8f, 0, 0).rot(30, -0.25f, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.8f, 0, 0.2f).rot(14, -0.1f, 0, 0.35f).rot(30, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.5f, 0, -0.2f).rot(14, 0.1f, 0, -0.35f).rot(30, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(10, 0, 4, 0).pos(14, 0, -1, 0, Ease.OUT).pos(22, 0, 0, 0).pos(30, 0, 0, 0)
            .bone("hairBack").rot(0, 0.1f, 0, 0).rot(14, 1.3f, 0, 0).rot(22, 1.0f, 0, 0).rot(30, 0.1f, 0, 0)
            .bone("scarf").rot(0, 0.2f, 0, 0).rot(14, 1.5f, 0, 0.5f).rot(30, 0.2f, 0, 0).done();
    }
}
