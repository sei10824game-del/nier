package com.nier.automata.unit.boss.eve;

import static com.nier.automata.unit.boss.eve.EveUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.boss.BossClient;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Original box design: heavy-set brawler, charcoal plating, oversized gauntlets and pauldrons; rage form glows red. */
public class EveModel extends NierModel<EveUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_eve");
    static final ResourceLocation TEX = BossClient.tex("boss_eve"), TEX_RAGE = BossClient.tex("boss_eve_rage");

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, EveModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EveModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 1.1f, (EveUnit u) -> u.getForm() == 1 ? TEX_RAGE : TEX, new EveModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 12, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(16, 16).addBox(-5, -12, -3, 10, 12, 6), PartPose.ZERO);
        torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, -12, -0.5f));
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 34).addBox(-4, -2, -2.5f, 5, 13, 5)
                .texOffs(40, 56).addBox(-5, -3.5f, -3, 6, 3, 6), PartPose.offset(-6, -10, 0));
        PartDefinition la = torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(20, 34).addBox(-1, -2, -2.5f, 5, 13, 5)
                .texOffs(40, 65).addBox(-1, -3.5f, -3, 6, 3, 6), PartPose.offset(6, -10, 0));
        ra.addOrReplaceChild("rightFist", CubeListBuilder.create().texOffs(40, 34).addBox(-3, 0, -3, 6, 5, 6), PartPose.offset(-1.5f, 10, 0));
        la.addOrReplaceChild("leftFist", CubeListBuilder.create().texOffs(40, 45).addBox(-3, 0, -3, 6, 5, 6), PartPose.offset(1.5f, 10, 0));
        body.addOrReplaceChild("rods", CubeListBuilder.create().texOffs(0, 80).addBox(-12, -0.5f, -0.5f, 24, 1, 1)
                .texOffs(64, 0).addBox(-0.5f, -0.5f, -12, 1, 1, 24), PartPose.offset(0, -8, 0));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.5f, 0, -2.5f, 5, 12, 5), PartPose.offset(-2.5f, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(20, 52).addBox(-2.5f, 0, -2.5f, 5, 12, 5), PartPose.offset(2.5f, 0, 0));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public EveModel(ModelPart root) {
        super(root);
        bind("body", "body/torso", "body/torso/head", "body/torso/rightArm", "body/torso/leftArm", "body/torso/rightArm/rightFist",
             "body/torso/leftArm/leftFist", "body/rods", "body/rightLeg", "body/leftLeg");

        // three-hit combo: right jab (8), left hook (15), overhead double hammer (24)
        clip(FISTS, AnimClip.of(30)
            .bone("body").pos(0, 0, 0, 0).pos(6, 0, 1, 1).pos(8, 0, 1, -3, Ease.OUT).pos(13, 0, 1, 0).pos(15, 0, 1, -3, Ease.OUT).pos(21, 0, -1, 1).pos(24, 0, 3, -4, Ease.IN).pos(30, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, 0, 0.6f, 0).rot(8, 0.2f, -0.5f, 0, Ease.OUT).rot(13, 0, -0.7f, 0).rot(15, 0.2f, 0.6f, 0, Ease.OUT)
                .rot(21, -0.5f, 0, 0).rot(24, 0.8f, 0, 0, Ease.IN).rot(27, 0.7f, 0, 0).rot(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, 0, -0.4f, 0).rot(8, 0.1f, 0.3f, 0).rot(15, 0.1f, -0.3f, 0).rot(21, -0.3f, 0, 0).rot(24, -0.5f, 0, 0).rot(30, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(6, 0.5f, 0, 0.4f).rot(8, -1.7f, -0.2f, 0, Ease.OUT).rot(13, -0.3f, 0, 0.3f).rot(15, 0.3f, 0, 0.2f)
                .rot(21, -2.9f, 0, -0.4f).rot(24, -1.0f, 0, -0.2f, Ease.IN).rot(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, 0.4f, 0, -0.3f).rot(13, -0.6f, 0.3f, -1.3f).rot(15, -1.6f, -0.9f, -0.4f, Ease.OUT)
                .rot(21, -2.9f, 0, 0.4f).rot(24, -1.0f, 0, 0.2f, Ease.IN).rot(30, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, 0.5f, 0, 0).rot(15, -0.4f, 0, 0).rot(24, -0.9f, 0, 0.2f).rot(30, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, -0.6f, 0, 0).rot(15, 0.5f, 0, 0).rot(24, 0.6f, 0, -0.2f).rot(30, 0, 0, 0).done());

        // hop (6), fists raised at apex, smash at 18
        clip(POUND, AnimClip.of(34)
            .bone("body").pos(0, 0, 0, 0).pos(5, 0, 3, 0).pos(12, 0, -2, 0).pos(18, 0, 5, -1, Ease.IN).pos(26, 0, 5, -1).pos(34, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(5, 0.4f, 0, 0).rot(14, -0.6f, 0, 0).rot(18, 1.0f, 0, 0, Ease.IN).rot(26, 0.9f, 0, 0).rot(34, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(14, -0.5f, 0, 0).rot(18, -0.7f, 0, 0).rot(34, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(5, 0.6f, 0, 0.5f).rot(14, -3.1f, 0, -0.2f).rot(18, -0.9f, 0, 0.1f, Ease.IN).rot(26, -0.9f, 0, 0.1f).rot(34, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(5, 0.6f, 0, -0.5f).rot(14, -3.1f, 0, 0.2f).rot(18, -0.9f, 0, -0.1f, Ease.IN).rot(26, -0.9f, 0, -0.1f).rot(34, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(5, -0.8f, 0, 0).rot(12, 0.2f, 0, 0.2f).rot(18, -1.2f, 0, 0.3f).rot(26, -1.2f, 0, 0.3f).rot(34, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(5, -0.8f, 0, 0).rot(12, 0.2f, 0, -0.2f).rot(18, 0.8f, 0, -0.3f).rot(26, 0.8f, 0, -0.3f).rot(34, 0, 0, 0).done());

        // sprinting shoulder charge (8..20)
        clip(RUSH, AnimClip.of(30)
            .bone("body").pos(0, 0, 0, 0).pos(7, 0, 3, 2).pos(9, 0, 2, -1, Ease.OUT).pos(20, 0, 2, -1).pos(24, 0, 1, 1).pos(30, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(7, 0.3f, 0.5f, 0).rot(9, 0.7f, -0.5f, 0, Ease.OUT).rot(20, 0.75f, -0.5f, 0).rot(24, -0.2f, 0, 0).rot(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(9, -0.6f, 0.4f, 0).rot(20, -0.6f, 0.4f, 0).rot(30, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(7, 0.5f, 0, 0.4f).rot(9, -0.9f, 0.3f, 0.5f, Ease.OUT).rot(20, -0.9f, 0.3f, 0.5f).rot(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(7, 0.8f, 0, -0.3f).rot(9, 0.9f, 0, -0.5f).rot(20, 0.9f, 0, -0.5f).rot(30, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(9, -1.0f, 0, 0).rot(11, 0.9f, 0, 0).rot(13, -1.0f, 0, 0).rot(15, 0.9f, 0, 0).rot(17, -1.0f, 0, 0).rot(19, 0.9f, 0, 0).rot(24, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(9, 0.9f, 0, 0).rot(11, -1.0f, 0, 0).rot(13, 0.9f, 0, 0).rot(15, -1.0f, 0, 0).rot(17, 0.9f, 0, 0).rot(19, -1.0f, 0, 0).rot(24, 0, 0, 0).done());

        // steel pipes whirl around the body (rods visible only during this skill)
        clip(PIPE_STORM, AnimClip.of(50)
            .bone("rods").rot(0, 0, 0, 0).rot(10, 0, 1.5f, 0, Ease.IN).rot(40, 0, 1.5f + 20f, 0, Ease.LINEAR).rot(50, 0, 24f, 0, Ease.OUT)
                .pos(0, 0, 0, 0).pos(10, 0, 2, 0).pos(25, 0, -2, 0).pos(40, 0, 2, 0).pos(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(8, 0.2f, 0.8f, 0).rot(14, 0.2f, -0.8f, 0).rot(20, 0.2f, 0.8f, 0).rot(26, 0.2f, -0.8f, 0).rot(32, 0.2f, 0.8f, 0).rot(40, 0.2f, -0.8f, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, -1.4f, 0, 1.3f).rot(40, -1.4f, 0, 1.3f).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, -1.4f, 0, -1.3f).rot(40, -1.4f, 0, -1.3f).rot(50, 0, 0, 0)
            .bone("rightFist").rot(0, 0, 0, 0).rot(8, 0, 0, 0).rot(10, 0, 3f, 0).rot(40, 0, 30f, 0, Ease.LINEAR).rot(50, 0, 31.4f, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, 0.2f, -0.6f, 0).rot(14, 0.2f, 0.6f, 0).rot(20, 0.2f, -0.6f, 0).rot(26, 0.2f, 0.6f, 0).rot(40, 0, 0, 0).rot(50, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(8, -0.4f, 0, 0.3f).rot(40, -0.4f, 0, 0.3f).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(8, 0.4f, 0, -0.3f).rot(40, 0.4f, 0, -0.3f).rot(50, 0, 0, 0).done());

        // crouch (0-8), launch (8), tuck in air, dive (26), impact (29)
        clip(LEAP, AnimClip.of(40)
            .bone("body").pos(0, 0, 0, 0).pos(7, 0, 4, 0).pos(9, 0, -1, 0, Ease.OUT).pos(26, 0, 0, 0).pos(29, 0, 5, -1, Ease.IN).pos(36, 0, 5, -1).pos(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(7, 0.7f, 0, 0).rot(10, -0.4f, 0, 0).rot(18, 0.6f, 0, 0).rot(26, -0.6f, 0, 0).rot(29, 1.1f, 0, 0, Ease.IN).rot(36, 1.0f, 0, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(7, -0.4f, 0, 0).rot(18, -0.3f, 0, 0).rot(29, -0.8f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(7, 0.9f, 0, 0.3f).rot(10, -3.0f, 0, 0.2f).rot(18, -1.5f, 0, 0.9f).rot(26, -3.1f, 0, -0.3f).rot(29, -0.6f, 0, 0.2f, Ease.IN).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(7, 0.9f, 0, -0.3f).rot(10, -3.0f, 0, -0.2f).rot(18, -1.5f, 0, -0.9f).rot(26, -3.1f, 0, 0.3f).rot(29, -0.6f, 0, -0.2f, Ease.IN).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(7, -1.2f, 0, 0).rot(10, 0.5f, 0, 0).rot(18, -1.4f, 0, 0).rot(29, -1.3f, 0, 0.3f).rot(36, -1.3f, 0, 0.3f).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(7, -1.2f, 0, 0).rot(10, 0.5f, 0, 0).rot(18, -1.4f, 0, 0).rot(29, 0.9f, 0, -0.3f).rot(36, 0.9f, 0, -0.3f).rot(40, 0, 0, 0).done());

        // hunch and tremble, roar (10), burst open (30)
        clip(RAGE, AnimClip.of(50)
            .bone("body").pos(0, 0, 0, 0).pos(10, 0, 3, 0).pos(28, 0, 4, 0).pos(30, 0, -2, 0, Ease.OUT).pos(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(10, 0.9f, 0, 0).rot(14, 0.85f, 0.1f, 0).rot(18, 0.9f, -0.1f, 0).rot(22, 0.85f, 0.1f, 0).rot(28, 0.95f, 0, 0).rot(31, -0.5f, 0, 0, Ease.OUT).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(28, 0.3f, 0, 0).rot(31, -0.8f, 0, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -0.4f, 0, -0.6f).rot(28, -0.6f, 0, -0.7f).rot(31, -0.3f, 0, 1.6f, Ease.OUT).rot(40, -0.2f, 0, 1.5f).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.4f, 0, 0.6f).rot(28, -0.6f, 0, 0.7f).rot(31, -0.3f, 0, -1.6f, Ease.OUT).rot(40, -0.2f, 0, -1.5f).rot(50, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0.2f).rot(28, -0.7f, 0, 0.2f).rot(31, 0, 0, 0.4f).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.4f, 0, -0.2f).rot(28, 0.4f, 0, -0.2f).rot(31, 0, 0, -0.4f).rot(50, 0, 0, 0).done());

        // alternating palm thrusts per ring (12/20/28/36 -> fires at k%8==4)
        AnimClip bar = AnimClip.of(44);
        AnimClip.Bone ra = bar.bone("rightArm").rot(0, 0, 0, 0), la = bar.bone("leftArm").rot(0, 0, 0, 0), to = bar.bone("torso").rot(0, 0, 0, 0);
        for (int i = 0; i < 4; i++) {
            int f = 12 + i * 8; boolean r = i % 2 == 0;
            to.rot(f - 3, 0, r ? 0.6f : -0.6f, 0).rot(f, 0.15f, r ? -0.4f : 0.4f, 0, Ease.OUT);
            (r ? ra : la).rot(f - 3, 0.4f, 0, r ? 0.6f : -0.6f).rot(f, -1.6f, 0, 0, Ease.OUT).rot(f + 4, -1.0f, 0, 0);
            (r ? la : ra).rot(f, -0.5f, 0, r ? -0.5f : 0.5f);
        }
        ra.rot(44, 0, 0, 0); la.rot(44, 0, 0, 0); to.rot(44, 0, 0, 0);
        bar.bone("rightLeg").rot(0, 0, 0, 0).rot(8, -0.5f, 0, 0.2f).rot(38, -0.5f, 0, 0.2f).rot(44, 0, 0, 0)
           .bone("leftLeg").rot(0, 0, 0, 0).rot(8, 0.4f, 0, -0.2f).rot(38, 0.4f, 0, -0.2f).rot(44, 0, 0, 0)
           .bone("body").pos(0, 0, 0, 0).pos(8, 0, 2, 0).pos(38, 0, 2, 0).pos(44, 0, 0, 0);
        clip(BARRAGE, bar);

        // arms spread, beams spin out of the body (14..44): whole torso turns with them
        clip(CHAIN_BEAMS, AnimClip.of(50)
            .bone("body").pos(0, 0, 0, 0).pos(12, 0, -3, 0).pos(44, 0, -3, 0).pos(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0).rot(14, -0.2f, 0, 0).rot(44, -0.2f, -2.7f, 0, Ease.LINEAR).rot(50, 0, -3.14f, 0)
            .bone("head").rot(0, 0, 0, 0).rot(12, -0.7f, 0, 0).rot(44, -0.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(12, -0.2f, 0, 2.6f).rot(14, 0, 0, 1.6f, Ease.OUT).rot(44, 0, 0, 1.6f).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(12, -0.2f, 0, -2.6f).rot(14, 0, 0, -1.6f, Ease.OUT).rot(44, 0, 0, -1.6f).rot(50, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(14, 0.2f, 0, 0.3f).rot(44, 0.2f, 0, 0.3f).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(14, 0.2f, 0, -0.3f).rot(44, 0.2f, 0, -0.3f).rot(50, 0, 0, 0).done());
    }

    @Override protected void idle(EveUnit e, float ls, float la, float age, float yaw, float pitch) {
        bone("rods").visible = e.getSkillId() == PIPE_STORM;
        bone("head").yRot = yaw * Mth.DEG_TO_RAD; bone("head").xRot = pitch * Mth.DEG_TO_RAD;
        float s = Mth.cos(ls * 0.6662f) * la, br = Mth.sin(age * 0.1f);
        bone("rightLeg").xRot = s * 1.1f; bone("leftLeg").xRot = -s * 1.1f;
        bone("rightArm").xRot = -s * 0.7f - 0.2f; bone("leftArm").xRot = s * 0.7f - 0.2f;
        bone("rightArm").zRot = 0.25f + br * 0.04f; bone("leftArm").zRot = -0.25f - br * 0.04f;
        bone("torso").xRot = 0.15f + la * 0.2f + br * 0.02f;
        bone("body").y += Math.abs(s) * 1.0f;
        if (e.getForm() == 1) { bone("torso").xRot += 0.15f; bone("rightArm").zRot += 0.1f; bone("leftArm").zRot -= 0.1f;
            bone("torso").yRot = Mth.sin(age * 0.6f) * 0.03f; }
    }
}
