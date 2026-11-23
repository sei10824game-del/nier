package com.nier.automata.unit.yorha;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Shared android body (128x128 texture): torso(head, arms(weapons)), legs, floating support pod.
 * Base UVs use y < 32; unit-specific parts use y >= 32. Model space: -z forward, -y up, right arm at -x.
 */
public abstract class AndroidModel<T extends AndroidUnit> extends NierModel<T> {
    static final String[] BASE = {"torso", "torso/head", "torso/rightArm", "torso/leftArm", "rightLeg", "leftLeg",
            "pod", "pod/podGun", "torso/rightArm/rightWeapon", "torso/leftArm/leftWeapon"};

    protected AndroidModel(ModelPart root) { super(root); }

    static void base(PartDefinition r) {
        PartDefinition torso = r.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(32, 0).addBox(-4, -12, -2, 8, 12, 4), PartPose.offset(0, 12, 0));
        torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, -12, 0));
        torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(56, 0).addBox(-2, -2, -2, 3, 12, 4), PartPose.offset(-5, -10, 0))
                .addOrReplaceChild("rightWeapon", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5f, 10, 0, 0.35f, 0, 0));
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(72, 0).addBox(-1, -2, -2, 3, 12, 4), PartPose.offset(5, -10, 0))
                .addOrReplaceChild("leftWeapon", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5f, 10, 0, 0.35f, 0, 0));
        r.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(88, 0).addBox(-2, 0, -2, 4, 12, 4), PartPose.offset(-1.9f, 12, 0));
        r.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(104, 0).addBox(-2, 0, -2, 4, 12, 4), PartPose.offset(1.9f, 12, 0));
        r.addOrReplaceChild("pod", CubeListBuilder.create().texOffs(0, 16).addBox(-3, -3, -4, 6, 6, 8).texOffs(44, 16).addBox(-4, -1, -2, 8, 1, 4),
                PartPose.offset(-11, -4, 3))
            .addOrReplaceChild("podGun", CubeListBuilder.create().texOffs(28, 16).addBox(-1, -1, -6, 2, 2, 6), PartPose.offset(0, 1, -3));
    }

    /** Sword along -z from the hand: grip, guard, blade (texture block 30x18 at u,v). */
    static CubeListBuilder sword(int u, int v, int len) {
        return CubeListBuilder.create().texOffs(u, v).addBox(-0.5f, -0.5f, -1, 1, 1, 3)
                .texOffs(u + 8, v).addBox(-0.5f, -1.5f, -2, 1, 3, 1)
                .texOffs(u, v + 4).addBox(-0.5f, -1, -2 - len, 1, 2, len);
    }

    static String[] paths(String... extra) {
        String[] a = new String[BASE.length + extra.length];
        System.arraycopy(BASE, 0, a, 0, BASE.length); System.arraycopy(extra, 0, a, BASE.length, extra.length);
        return a;
    }

    @Override protected void idle(T e, float ls, float la, float age, float hy, float hp) {
        ModelPart head = bone("head"), ra = bone("rightArm"), la_ = bone("leftArm"), torso = bone("torso"), pod = bone("pod");
        head.yRot = hy * Mth.DEG_TO_RAD; head.xRot = hp * Mth.DEG_TO_RAD;
        float sw = Mth.cos(ls * 0.6662f), breathe = Mth.sin(age * 0.08f);
        ra.xRot = -sw * 0.9f * la; la_.xRot = sw * 0.9f * la;
        ra.zRot = 0.06f + breathe * 0.03f; la_.zRot = -0.06f - breathe * 0.03f;
        bone("rightLeg").xRot = sw * 1.2f * la; bone("leftLeg").xRot = -sw * 1.2f * la;
        torso.yRot = sw * 0.12f * la; torso.xRot = 0.05f * la;
        pod.y += Mth.sin(age * 0.1f) * 1.2f; pod.yRot = hy * Mth.DEG_TO_RAD * 0.6f; pod.zRot = Mth.sin(age * 0.07f) * 0.06f;
    }

    /** Evade counter (22 ticks): backflip-step with i-frames (t0-8), crouched recovery, dash, counter slash at t=14. */
    static AnimClip evadeClip() {
        return AnimClip.of(22).blend(2)
            .bone("root").pos(0, 0, 0, 0).pos(3, 0, -5, 3, AnimClip.Ease.OUT).pos(7, 0, 0, 5).pos(10, 0, 2, 4).pos(13, 0, 0, -2, AnimClip.Ease.OUT).pos(22, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(3, -0.5f, 0, 0).rot(7, 0.3f, 0, 0).rot(10, 0.45f, 0.7f, 0).rot(14, 0.35f, -0.8f, 0, AnimClip.Ease.OUT).rot(22, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(3, 0.3f, 0, 0).rot(10, -0.2f, -0.5f, 0).rot(14, -0.2f, 0.5f, 0).rot(22, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(3, -0.4f, 0, 1.2f).rot(10, -1.4f, 0.9f, 0.9f).rot(14, -1.2f, -1.0f, -0.6f, AnimClip.Ease.OUT).rot(17, -0.9f, -1.2f, -0.4f).rot(22, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(3, -0.4f, 0, -1.2f).rot(10, 0.6f, 0, -0.5f).rot(14, 0.8f, 0, -0.9f).rot(22, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(3, -0.9f, 0, 0.2f).rot(7, -0.3f, 0, 0).rot(10, -0.9f, 0, 0).rot(14, 0.6f, 0, 0).rot(22, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(3, -0.6f, 0, -0.2f).rot(7, 0.2f, 0, 0).rot(10, 0.5f, 0, 0).rot(14, -0.7f, 0, 0).rot(22, 0, 0, 0)
            .done();
    }

    /** Pod gun burst (40 ticks): pod swings forward, aims, recoils every 2 ticks t=6..34; unit points to mark. */
    static AnimClip podGunClip() {
        AnimClip c = AnimClip.of(40);
        AnimClip.Bone pod = c.bone("pod").pos(0, 0, 0, 0).pos(5, 4, -4, -6, AnimClip.Ease.OUT).pos(36, 4, -4, -6).pos(40, 0, 0, 0);
        AnimClip.Bone gun = c.bone("podGun").rot(0, 0, 0, 0).rot(5, 0.1f, 0, 0);
        for (int t = 6; t <= 34; t += 2) { gun.pos(t, 0, 0, 1.2f, AnimClip.Ease.SNAP).pos(t + 1, 0, 0, 0); gun.rot(t, 0.1f, 0, t * 0.6f); }
        gun.rot(40, 0, 0, 0);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(5, -1.5f, 0.3f, 0).rot(35, -1.45f, 0.2f, 0).rot(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(5, 0, 0.35f, 0).rot(35, 0, 0.3f, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(5, 0, -0.3f, 0).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(5, 0.3f, 0, -0.3f).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(5, -0.25f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(5, 0.25f, 0, 0).rot(40, 0, 0, 0).done();
    }
}
