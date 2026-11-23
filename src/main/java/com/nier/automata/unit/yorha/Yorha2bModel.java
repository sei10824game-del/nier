package com.nier.automata.unit.yorha;

import static com.nier.automata.unit.yorha.Yorha2bUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** Battle android B: braided hair, visor band, armoured tassets, short sword in hand, greatsword on the back, ochre pod. */
public class Yorha2bModel extends AndroidModel<Yorha2bUnit> {
    public Yorha2bModel(ModelPart root) {
        super(root);
        bind(paths("torso/head/braid", "torso/coatFront", "torso/coatBack", "torso/backBlade",
                "torso/rightArm/heldGreat", "torso/leftArm/leftGreat"));
        clip(LIGHT, light()); clip(HEAVY, heavy()); clip(AERIAL, aerial()); clip(TREATY, treaty());
        clip(POD_GUN, podGunClip()); clip(POD_LASER, laser()); clip(POD_HAMMER, hammer()); clip(EVADE, evadeClip());
        clip(SELF_DESTRUCT, selfDestruct());
    }

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition(); PartDefinition r = m.getRoot();
        base(r);
        PartDefinition torso = r.getChild("torso"), head = torso.getChild("head");
        head.addOrReplaceChild("hairCap", CubeListBuilder.create().texOffs(8, 32).addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
                .texOffs(44, 32).addBox(-4.5f, -5.2f, -4.6f, 9, 2, 1), PartPose.ZERO);      // hair cap + visor band
        head.addOrReplaceChild("braid", CubeListBuilder.create().texOffs(0, 32).addBox(-1, 0, 0, 2, 12, 2), PartPose.offsetAndRotation(0, -4, 4, 0.15f, 0, 0));
        torso.addOrReplaceChild("coatFront", CubeListBuilder.create().texOffs(64, 32).addBox(-4.5f, 0, -0.5f, 9, 7, 1), PartPose.offset(0, 0, -2.2f));
        torso.addOrReplaceChild("coatBack", CubeListBuilder.create().texOffs(84, 32).addBox(-4.5f, 0, -0.5f, 9, 8, 1), PartPose.offset(0, 0, 2.2f));
        torso.addOrReplaceChild("backBlade", CubeListBuilder.create().texOffs(84, 48).addBox(-2, -14, 0, 4, 24, 1), PartPose.offsetAndRotation(0, -6, 2.6f, 0, 0, 0.6f));
        torso.getChild("rightArm").getChild("rightWeapon").addOrReplaceChild("blade", sword(0, 48, 14), PartPose.ZERO);
        CubeListBuilder great = CubeListBuilder.create().texOffs(32, 48).addBox(-0.5f, -0.5f, -1, 1, 1, 4)
                .texOffs(76, 48).addBox(-0.5f, -2.5f, -3, 1, 5, 2).texOffs(32, 54).addBox(-0.5f, -2, -23, 1, 4, 20);
        torso.getChild("rightArm").addOrReplaceChild("heldGreat", great, PartPose.offsetAndRotation(-0.5f, 10, 0, 0.35f, 0, 0));
        torso.getChild("leftArm").addOrReplaceChild("leftGreat", great, PartPose.offsetAndRotation(0.5f, 10, 0, 0.35f, 0, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    @Override protected void idle(Yorha2bUnit e, float ls, float la, float age, float hy, float hp) {
        super.idle(e, ls, la, age, hy, hp);
        bone("heldGreat").xScale = bone("heldGreat").yScale = bone("heldGreat").zScale = 0;
        bone("leftGreat").xScale = bone("leftGreat").yScale = bone("leftGreat").zScale = 0;
        float sw = Mth.cos(ls * 0.6662f) * la;
        bone("coatFront").xRot = -Math.abs(sw) * 0.5f; bone("coatBack").xRot = Math.abs(sw) * 0.4f + la * 0.3f;
        bone("braid").xRot = 0.15f + la * 0.5f + Mth.sin(age * 0.1f) * 0.05f;
        if (e.getForm() == 1) bone("head").zRot = 0.12f + Mth.sin(age * 0.3f) * 0.03f;   // damaged, tilted head
    }

    /** Hide back greatsword and materialise a held one (right or left). */
    private static AnimClip.Bone great(AnimClip c, int len, String held) {
        c.bone("backBlade").scale(0, 0, 0, 0).scale(len, 0, 0, 0);
        return c.bone(held).scale(0, 1, 1, 1).scale(len, 1, 1, 1);
    }

    // hits 6 / 12 / 20
    private static AnimClip light() {
        return AnimClip.of(26)
            .bone("rightArm").rot(0, 0, 0, 0).rot(4, -2.6f, 0.2f, 0.7f).rot(6, -0.6f, -0.3f, -0.5f, Ease.OUT).rot(9, -1.3f, -0.9f, -0.2f)
                .rot(12, -1.4f, 0.8f, 0.6f, Ease.OUT).rot(17, -2.3f, 0.7f, 0.9f).rot(20, -0.8f, -0.9f, -0.7f, Ease.OUT).rot(23, -0.6f, -0.8f, -0.5f).rot(26, 0, 0, 0)
            .bone("rightWeapon").rot(0, 0.35f, 0, 0).rot(6, 0.1f, 0, 0).rot(12, 0.6f, 0, 1.2f).rot(20, 0.2f, 0, 0).rot(26, 0.35f, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(4, -0.1f, 0.5f, 0).rot(6, 0.25f, -0.4f, 0, Ease.OUT).rot(9, 0.1f, -0.6f, 0).rot(12, 0.2f, 0.5f, 0, Ease.OUT)
                .rot(17, 0.05f, 0.8f, 0).rot(20, 0.35f, -0.7f, 0, Ease.OUT).rot(26, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(4, 0, -0.3f, 0).rot(12, 0, -0.3f, 0).rot(20, -0.2f, 0.5f, 0).rot(26, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, 0.6f, 0, -0.6f).rot(12, -0.5f, 0, -0.4f).rot(20, 0.9f, 0, -0.8f).rot(26, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, -0.5f, 0, 0).rot(12, 0.3f, 0, 0).rot(20, -0.9f, 0, 0.1f).rot(26, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, 0.4f, 0, 0).rot(12, -0.4f, 0, 0).rot(20, 0.6f, 0, -0.1f).rot(26, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(6, 0, 0.5f, -1).pos(12, 0, 0.5f, -1.5f).pos(17, 0, 0, 0).pos(20, 0, 2, -5, Ease.OUT).pos(26, 0, 0, 0)
            .bone("coatBack").rot(0, 0, 0, 0).rot(20, 0.7f, 0, 0).rot(26, 0, 0, 0).done();
    }

    // overhead cleave hit 14, full spin hit 26
    private static AnimClip heavy() {
        AnimClip c = AnimClip.of(36);
        great(c, 36, "heldGreat");
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(10, -3.0f, 0, 0.2f, Ease.OUT).rot(14, -0.5f, 0, 0, Ease.IN).rot(18, -0.4f, 0, 0)
                .rot(22, -1.5f, 1.2f, 0.6f).rot(26, -1.5f, -1.4f, 0, Ease.LINEAR).rot(30, -1.2f, -1.2f, 0).rot(36, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -2.9f, 0, -0.5f, Ease.OUT).rot(14, -0.6f, 0, -0.4f, Ease.IN).rot(18, -0.5f, 0, -0.3f)
                .rot(22, -1.3f, 0.9f, -0.2f).rot(26, -1.0f, -0.8f, -0.3f).rot(36, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(10, -0.35f, 0, 0).rot(14, 0.55f, 0, 0, Ease.IN).rot(18, 0.5f, 0, 0).rot(22, 0.15f, 1.0f, 0)
                .rot(26, 0.2f, -6.28f, 0, Ease.LINEAR).rot(30, 0.1f, -6.6f, 0).rot(32, 0.05f, -6.28f, 0).rot(32.01f, 0.05f, 0, 0, Ease.SNAP).rot(36, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(14, 0.2f, 0, 0).rot(22, 0, -0.5f, 0).rot(36, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.2f, 0, 0).rot(14, -0.8f, 0, 0.1f).rot(22, -0.4f, 0, 0.3f).rot(26, -0.4f, 0, 0.4f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, -0.1f, 0, 0).rot(14, 0.7f, 0, -0.1f).rot(22, 0.3f, 0, -0.3f).rot(36, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(4, 0, 0, -2).pos(10, 0, -1, -2).pos(14, 0, 3, -4, Ease.IN).pos(20, 0, 3, -4).pos(26, 0, 2, -4).pos(36, 0, 0, 0)
            .bone("coatBack").rot(0, 0, 0, 0).rot(14, 0.6f, 0, 0).rot(26, 1.0f, 0, 0).rot(36, 0, 0, 0)
            .bone("coatFront").rot(0, 0, 0, 0).rot(26, -0.8f, 0, 0).rot(36, 0, 0, 0).done();
    }

    // jump 2, air cuts 9/15, plunge 20, landing 26
    private static AnimClip aerial() {
        return AnimClip.of(34)
            .bone("rightArm").rot(0, 0, 0, 0).rot(3, 0.6f, 0, 0.3f).rot(7, -2.7f, 0.3f, 0.5f).rot(9, -0.7f, -0.5f, -0.6f, Ease.OUT)
                .rot(13, -1.5f, 1.0f, 0.8f).rot(15, -1.3f, -1.0f, -0.6f, Ease.OUT).rot(19, -3.1f, 0, 0).rot(26, -0.1f, 0, 0, Ease.IN).rot(34, 0, 0, 0)
            .bone("rightWeapon").rot(0, 0.35f, 0, 0).rot(19, 0.35f, 0, 0).rot(21, 1.5f, 0, 0).rot(28, 1.5f, 0, 0).rot(34, 0.35f, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(3, 0.6f, 0, -0.3f).rot(9, -0.3f, 0, -1.2f).rot(15, 0.4f, 0, -1.0f).rot(19, -3.0f, 0, 0).rot(26, -0.3f, 0, -0.2f, Ease.IN).rot(34, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(2, 0.4f, 0, 0).rot(5, -0.3f, 0, 0).rot(9, 0.3f, -0.6f, 0, Ease.OUT).rot(15, 0.3f, 0.6f, 0, Ease.OUT)
                .rot(19, -0.4f, 0, 0).rot(24, 0.6f, 0, 0, Ease.IN).rot(26, 0.7f, 0, 0).rot(34, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(5, -0.4f, 0, 0).rot(19, 0.4f, 0, 0).rot(26, -0.3f, 0, 0).rot(34, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(2, -0.6f, 0, 0).rot(5, -1.3f, 0, 0).rot(12, -0.8f, 0, 0.2f).rot(19, -1.4f, 0, 0).rot(26, -1.2f, 0, 0).rot(34, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(2, 0.6f, 0, 0).rot(5, 0.5f, 0, 0).rot(12, -0.3f, 0, -0.2f).rot(19, 0.2f, 0, 0).rot(26, 0.9f, 0, 0).rot(34, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(2, 0, 2, 0).pos(4, 0, -2, 0).pos(26, 0, 4, 0, Ease.IN).pos(30, 0, 3, 0).pos(34, 0, 0, 0)
            .bone("braid").rot(0, 0.15f, 0, 0).rot(5, 1.2f, 0, 0).rot(20, -0.6f, 0, 0).rot(26, 1.1f, 0, 0).rot(34, 0.15f, 0, 0)
            .bone("coatBack").rot(0, 0, 0, 0).rot(5, 0.9f, 0, 0).rot(20, -0.2f, 0, 0).rot(26, 0.9f, 0, 0).rot(34, 0, 0, 0).done();
    }

    // small 6, great sweep 13, small 21, great thrust 30
    private static AnimClip treaty() {
        AnimClip c = AnimClip.of(40);
        great(c, 40, "leftGreat");
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(4, -2.4f, 0.3f, 0.6f).rot(6, -0.7f, -0.5f, -0.5f, Ease.OUT).rot(13, -0.2f, 0, 0.6f)
                .rot(19, -1.6f, 1.0f, 0.6f).rot(21, -1.4f, -1.0f, -0.4f, Ease.OUT).rot(30, 0.4f, 0, 0.5f).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, -0.3f, 0, -0.6f).rot(10, -1.5f, 1.4f, -0.3f).rot(13, -1.5f, -1.2f, -0.3f, Ease.OUT)
                .rot(21, 0.4f, 0, -0.4f).rot(27, -1.3f, 0, -0.2f).rot(30, -1.6f, 0, 0, Ease.OUT).rot(34, -1.5f, 0, 0).rot(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(4, 0, 0.5f, 0).rot(6, 0.2f, -0.4f, 0, Ease.OUT).rot(10, 0, -0.9f, 0).rot(13, 0.2f, 0.9f, 0, Ease.OUT)
                .rot(19, 0.05f, 0.7f, 0).rot(21, 0.25f, -0.5f, 0, Ease.OUT).rot(27, -0.1f, 0.8f, 0).rot(30, 0.35f, -0.6f, 0, Ease.OUT).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(13, 0, -0.6f, 0).rot(21, 0, 0.4f, 0).rot(30, -0.2f, 0.5f, 0).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(6, -0.4f, 0, 0).rot(13, 0.4f, 0, 0.2f).rot(21, -0.5f, 0, 0).rot(30, 0.7f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(6, 0.3f, 0, 0).rot(13, -0.5f, 0, -0.2f).rot(21, 0.4f, 0, 0).rot(30, -1.0f, 0, 0).rot(40, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(13, 0, 1, -1).pos(21, 0, 0, -1).pos(27, 0, 1, 1).pos(30, 0, 2.5f, -6, Ease.OUT).pos(34, 0, 2, -6).pos(40, 0, 0, 0).done();
    }

    // charge 0-20, beam 20-44
    private static AnimClip laser() {
        AnimClip c = AnimClip.of(56);
        c.bone("pod").pos(0, 0, 0, 0).pos(8, 6, -8, -6, Ease.OUT).pos(48, 6, -8, -6).pos(56, 0, 0, 0)
            .rot(0, 0, 0, 0).rot(8, 0, 0, 0).rot(18, 0.15f, 0, 3.14f).rot(20, 0.1f, 0, 3.14f).rot(32, 0.1f, 0.4f, 3.14f).rot(44, 0.1f, -0.4f, 3.14f).rot(50, 0, 0, 6.28f).rot(50.01f, 0, 0, 0, Ease.SNAP).rot(56, 0, 0, 0)
            .scale(0, 1, 1, 1).scale(18, 1.15f, 1.15f, 1.15f).scale(20, 1, 1, 1);
        AnimClip.Bone gun = c.bone("podGun").pos(0, 0, 0, 0).pos(19, 0, 0, 1.5f).pos(20, 0, 0, -1, Ease.SNAP);
        for (int t = 22; t <= 44; t += 2) gun.pos(t, 0, 0, (t % 4 == 0) ? 0.3f : -0.3f);
        gun.pos(48, 0, 0, 0);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(6, -2.8f, 0, 0.3f).rot(18, -2.7f, 0, 0.35f).rot(21, -1.6f, 0.2f, 0, Ease.OUT).rot(44, -1.55f, 0.2f, 0).rot(56, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(6, 0.2f, 0, -0.5f).rot(44, 0.2f, 0, -0.5f).rot(56, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, -0.15f, 0.2f, 0).rot(21, 0.1f, 0.35f, 0).rot(44, 0.1f, 0.35f, 0).rot(56, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, -0.3f, -0.2f, 0).rot(21, 0, -0.35f, 0).rot(56, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(21, -0.3f, 0, 0.1f).rot(44, -0.3f, 0, 0.1f).rot(56, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(21, 0.35f, 0, -0.1f).rot(44, 0.35f, 0, -0.1f).rot(56, 0, 0, 0).done();
    }

    // pod rises 0-14, slam 16
    private static AnimClip hammer() {
        return AnimClip.of(30)
            .bone("pod").pos(0, 0, 0, 0).pos(12, 8, -14, -10, Ease.OUT).pos(14, 8, -15, -12).pos(16, 9, 22, -22, Ease.IN).pos(22, 9, 21, -22).pos(30, 0, 0, 0)
                .rot(0, 0, 0, 0).rot(12, -1.2f, 0, 0).rot(14, -1.5f, 0, 0).rot(16, 1.6f, 0, 0, Ease.IN).rot(22, 1.5f, 0, 0).rot(30, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(14, 1.6f, 1.6f, 1.6f).scale(16, 1.8f, 1.2f, 1.8f).scale(20, 1.4f, 1.4f, 1.4f).scale(30, 1, 1, 1)
            .bone("rightArm").rot(0, 0, 0, 0).rot(12, -3.0f, 0, 0.2f).rot(16, -1.0f, 0, 0, Ease.IN).rot(22, -0.9f, 0, 0).rot(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(12, 0.5f, 0, -0.6f).rot(16, 0.6f, 0, -0.4f).rot(30, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(12, -0.3f, 0.2f, 0).rot(16, 0.45f, 0, 0, Ease.IN).rot(22, 0.4f, 0, 0).rot(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0).rot(16, 0.3f, 0, 0).rot(30, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(16, -0.7f, 0, 0).rot(22, -0.7f, 0, 0).rot(30, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(16, 0.5f, 0, 0).rot(22, 0.5f, 0, 0).rot(30, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(16, 0, 2, 0, Ease.IN).pos(22, 0, 2, 0).pos(30, 0, 0, 0).done();
    }

    // build-up 0-44 (trembling crouch), detonation 45, staggered recovery
    private static AnimClip selfDestruct() {
        AnimClip c = AnimClip.of(60).blend(4);
        AnimClip.Bone torso = c.bone("torso").rot(0, 0, 0, 0).rot(8, 0.7f, 0, 0);
        for (int t = 10; t <= 42; t += 2) torso.rot(t, 0.75f, 0, (t % 4 == 0 ? 0.06f : -0.06f) * (1 + t / 20f));
        torso.rot(45, -0.6f, 0, 0, Ease.OUT).rot(50, -0.5f, 0, 0).rot(60, 0.1f, 0, 0);
        return c.bone("rightArm").rot(0, 0, 0, 0).rot(8, -1.2f, -0.6f, -0.6f).rot(42, -1.3f, -0.7f, -0.7f).rot(45, -2.4f, 0, 1.5f, Ease.OUT).rot(52, -2.2f, 0, 1.3f).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, -1.2f, 0.6f, 0.6f).rot(42, -1.3f, 0.7f, 0.7f).rot(45, -2.4f, 0, -1.5f, Ease.OUT).rot(52, -2.2f, 0, -1.3f).rot(60, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0.5f, 0, 0).rot(42, 0.6f, 0, 0).rot(45, -0.8f, 0, 0, Ease.OUT).rot(52, -0.7f, 0, 0).rot(60, 0.2f, 0, 0.15f)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, -1.2f, 0, 0.1f).rot(42, -1.2f, 0, 0.1f).rot(45, 0.2f, 0, 0.3f).rot(60, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, 0.6f, 0, -0.1f).rot(42, 0.6f, 0, -0.1f).rot(45, 0.3f, 0, -0.3f).rot(60, 0, 0, 0)
            .bone("root").pos(0, 0, 0, 0).pos(8, 0, 5, 0).pos(42, 0, 5, 0).pos(45, 0, -3, 2, Ease.OUT).pos(50, 0, 0, 3).pos(60, 0, 0, 0)
            .bone("pod").pos(0, 0, 0, 0).pos(10, 11, -6, -8).pos(42, 11, -8, -8).pos(45, 0, -20, 20, Ease.OUT).pos(60, 0, 0, 0)
                .rot(0, 0, 0, 0).rot(42, 0, 12f, 0).rot(45, 1.5f, 14f, 0).rot(52, 0, 12.56f, 0).rot(52.01f, 0, 0, 0, Ease.SNAP).rot(60, 0, 0, 0)
            .bone("braid").rot(0, 0.15f, 0, 0).rot(45, 1.4f, 0, 0).rot(60, 0.15f, 0, 0)
            .bone("coatBack").rot(0, 0, 0, 0).rot(45, 1.2f, 0, 0).rot(60, 0, 0, 0)
            .bone("coatFront").rot(0, 0, 0, 0).rot(45, -1.0f, 0, 0).rot(60, 0, 0, 0).done();
    }
}
