package com.nier.automata.unit.boss.adam;

import static com.nier.automata.unit.boss.adam.AdamUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.boss.BossClient;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Original box design: slim figure in a long ash-grey coat, black gloves, glowing red visor band; form 1 draws a long blade. */
public class AdamModel extends NierModel<AdamUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_adam");
    static final float P = Mth.PI;

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, AdamModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, AdamModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 1.05f, BossClient.tex("boss_adam"), new AdamModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 12, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4, -12, -2, 8, 12, 4), PartPose.ZERO);
        torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, -12, 0));
        torso.addOrReplaceChild("coat", CubeListBuilder.create().texOffs(16, 34).addBox(-4, 0, 0, 8, 10, 1), PartPose.offset(0, 0, 1.5f));
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3, -2, -2, 4, 12, 4), PartPose.offset(-5, -10, 0));
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 32).addBox(-1, -2, -2, 4, 12, 4), PartPose.offset(5, -10, 0));
        ra.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(56, 0).addBox(-0.5f, 0, -1, 1, 16, 2)
                .texOffs(34, 48).addBox(-2, -1, -1, 4, 1, 2), PartPose.offsetAndRotation(-1, 9, 0, -P / 2, 0, 0));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 12, 4), PartPose.offset(-2, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 32).addBox(-2, 0, -2, 4, 12, 4), PartPose.offset(2, 0, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public AdamModel(ModelPart root) {
        super(root);
        bind("body", "body/torso", "body/torso/head", "body/torso/coat", "body/torso/rightArm", "body/torso/leftArm",
             "body/torso/rightArm/sword", "body/rightLeg", "body/leftLeg");

        clip(PHASE_STRIKE, AnimClip.of(24)
            .bone("body").pos(0, 0, 0, 0).pos(5, 0, 3, 0).pos(6, 0, -2, 0, Ease.SNAP).pos(9, 0, 1, -1).pos(12, 0, 2, -3, Ease.OUT).pos(24, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(5, 0.5f, 0.3f, 0).rot(9, -0.2f, 0.9f, 0).rot(12, 0.5f, -0.6f, 0, Ease.OUT).rot(16, 0.4f, -0.7f, 0).rot(24, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(5, 0.3f, -0.3f, 0).rot(9, 0, -0.6f, 0).rot(12, -0.1f, 0.5f, 0).rot(24, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(5, 0.4f, 0, 0.3f).rot(9, -0.6f, 0.4f, 0.9f).rot(12, -1.7f, -0.3f, -0.1f, Ease.OUT).rot(16, -1.6f, -0.2f, 0).rot(24, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(5, 0.3f, 0, -0.4f).rot(12, 0.6f, 0, -0.5f).rot(24, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(6, 0.9f, 0, 0).rot(12, 1.2f, 0, 0.2f).rot(18, 0.4f, 0, 0).rot(24, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(5, -0.6f, 0, 0).rot(9, 0.5f, 0, 0).rot(12, 0.6f, 0, 0).rot(24, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(5, 0.3f, 0, 0).rot(9, -0.7f, 0, 0).rot(12, -0.8f, 0, 0).rot(24, 0, 0, 0).done());

        clip(SPIKE_LINE, AnimClip.of(40)
            .bone("body").pos(0, 0, 0, 0).pos(10, 0, -1, 1).pos(12, 0, 4, -1, Ease.IN).pos(30, 0, 4, -1).pos(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(10, -0.35f, 0.3f, 0).rot(12, 0.9f, 0, 0, Ease.IN).rot(30, 0.85f, 0, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(12, -0.6f, 0, 0).rot(30, -0.6f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -2.9f, 0, -0.3f).rot(12, -1.2f, 0, 0, Ease.IN).rot(30, -1.1f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.4f, 0, -0.9f).rot(12, 0.4f, 0, -1.0f).rot(30, 0.4f, 0, -1.0f).rot(40, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(12, 0.3f, 0, 0).rot(14, 1.0f, 0, 0).rot(30, 0.8f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(12, -1.1f, 0, 0).rot(30, -1.1f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(12, 0.9f, 0, 0).rot(30, 0.9f, 0, 0).rot(40, 0, 0, 0).done());

        clip(CUBE_VOLLEY, AnimClip.of(36)
            .bone("torso").rot(0, 0, 0, 0).rot(10, 0, -0.7f, 0).rot(14, 0.1f, 0.5f, 0, Ease.OUT).rot(20, 0, 0.7f, 0).rot(24, 0.1f, -0.5f, 0, Ease.OUT).rot(36, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0, 0.6f, 0).rot(14, 0, -0.4f, 0).rot(20, 0, -0.6f, 0).rot(24, 0, 0.4f, 0).rot(36, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 1.3f).rot(14, -1.6f, 0.5f, -0.2f, Ease.OUT).rot(20, -0.4f, 0, 0.3f).rot(36, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(14, -0.2f, 0, -0.3f).rot(20, -0.6f, 0, -1.3f).rot(24, -1.6f, -0.5f, 0.2f, Ease.OUT).rot(30, -1.4f, -0.4f, 0.1f).rot(36, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(14, 0.3f, 0, -0.3f).rot(24, 0.3f, 0, 0.3f).rot(36, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(14, -0.3f, 0, 0.1f).rot(36, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(14, 0.3f, 0, -0.1f).rot(36, 0, 0, 0).done());

        clip(SINGULARITY, AnimClip.of(50)
            .bone("body").pos(0, 0, 0, 0).pos(18, 0, -2, 0).pos(22, 0, 0, -1, Ease.OUT).pos(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(18, -0.4f, 0, 0).rot(22, 0.3f, 0, 0, Ease.OUT).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(18, -0.5f, 0, 0).rot(22, 0.1f, 0, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(18, -2.8f, 0, 0.5f).rot(22, -1.5f, 0, 0.1f, Ease.OUT).rot(40, -1.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(18, -2.8f, 0, -0.5f).rot(22, -1.5f, 0, -0.1f, Ease.OUT).rot(40, -1.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(18, 0.2f, 0, 0).rot(22, 0.9f, 0, 0).rot(50, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(22, 0.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(22, -0.5f, 0, 0).rot(50, 0, 0, 0).done());

        clip(BEAM, AnimClip.of(44)
            .bone("body").pos(0, 0, 0, 0).pos(16, 0, 1, 2).pos(18, 0, 1, 0, Ease.OUT).pos(36, 0, 1, 1).pos(44, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(16, -0.2f, 1.0f, 0).rot(18, 0.1f, -0.2f, 0, Ease.OUT).rot(36, 0.1f, -0.3f, 0).rot(44, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(16, 0, -0.9f, 0).rot(18, 0, 0.2f, 0).rot(44, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(16, -0.8f, 0.6f, 0.8f).rot(18, -1.6f, 0.2f, 0, Ease.OUT).rot(36, -1.55f, 0.2f, 0).rot(44, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(16, -1.2f, -0.8f, 0).rot(18, -1.5f, -0.3f, 0.1f).rot(36, -1.5f, -0.3f, 0.1f).rot(44, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(18, 0.7f, 0, 0).rot(20, 0.5f, 0, 0).rot(24, 0.7f, 0, 0).rot(28, 0.5f, 0, 0).rot(36, 0.6f, 0, 0).rot(44, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(18, 0.5f, 0, 0).rot(36, 0.5f, 0, 0).rot(44, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(18, -0.6f, 0, 0).rot(36, -0.6f, 0, 0).rot(44, 0, 0, 0).done());

        clip(AWAKEN, AnimClip.of(50)
            .bone("body").pos(0, 0, 0, 0).pos(28, 0, -4, 0).pos(30, 0, -3, 0, Ease.OUT).pos(50, 0, -2, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(10, 0.6f, 0, 0).rot(28, -0.5f, 0, 0).rot(31, 0.1f, 0, 0, Ease.OUT).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0.7f, 0, 0).rot(28, -0.8f, 0, 0).rot(31, 0, 0, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, 0, 0, 0.3f).rot(28, -0.3f, 0, 1.4f).rot(31, 0, 0, 2.2f, Ease.OUT).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, 0, 0, -0.3f).rot(28, -0.3f, 0, -1.4f).rot(31, 0, 0, -2.2f, Ease.OUT).rot(50, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(28, 0.6f, 0, 0).rot(31, 1.3f, 0, 0).rot(50, 0.2f, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, 0.2f, 0, 0.1f).rot(28, 0.3f, 0, 0.2f).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.2f, 0, -0.1f).rot(28, 0.3f, 0, -0.2f).rot(50, 0, 0, 0).done());

        clip(BLADE_WALTZ, AnimClip.of(40)
            .bone("body").pos(0, 0, 0, 0).pos(6, 0, 2, 0).pos(10, 0, 1, -3, Ease.OUT).pos(16, 0, 1, 0).pos(20, 0, 1, -3, Ease.OUT).pos(26, 0, 3, 0).pos(30, 0, 1, -1, Ease.OUT).pos(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(6, 0.2f, 1.2f, 0).rot(10, 0.3f, -0.9f, 0, Ease.OUT).rot(16, 0.1f, -1.3f, 0).rot(20, 0.3f, 1.0f, 0, Ease.OUT)
                .rot(26, 0.2f, 1.6f, 0).rot(30, 0.3f, -2.6f, 0, Ease.OUT).rot(34, 0.2f, -2.9f, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(6, 0, -1.0f, 0).rot(10, 0, 0.7f, 0).rot(16, 0, 1.1f, 0).rot(20, 0, -0.8f, 0).rot(26, 0, -1.3f, 0).rot(30, 0, 1.5f, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(6, -2.4f, 0, 0.8f).rot(10, -1.0f, -0.6f, -0.5f, Ease.OUT).rot(16, -1.4f, -0.9f, -0.6f).rot(20, -1.7f, 0.7f, 0.8f, Ease.OUT)
                .rot(26, -1.5f, 1.0f, 1.0f).rot(30, -1.6f, -1.2f, -0.3f, Ease.OUT).rot(34, -1.5f, -1.3f, -0.3f).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, 0.6f, 0, -0.7f).rot(20, -0.4f, 0, -1.2f).rot(30, 0.5f, 0, -1.4f).rot(40, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(10, 0.8f, 0, -0.5f).rot(20, 0.8f, 0, 0.5f).rot(30, 1.3f, 0, -0.6f).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(10, -0.8f, 0, 0).rot(16, 0.2f, 0, 0).rot(20, 0.6f, 0, 0).rot(30, -0.6f, 0, 0.3f).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(10, 0.6f, 0, 0).rot(16, -0.2f, 0, 0).rot(20, -0.8f, 0, 0).rot(30, 0.5f, 0, -0.3f).rot(40, 0, 0, 0).done());

        clip(SPIKE_CROWN, AnimClip.of(40)
            .bone("body").pos(0, 0, 0, 0).pos(12, 0, -3, 0).pos(14, 0, 2, 0, Ease.IN).pos(22, 0, -1, 0).pos(24, 0, 2, 0, Ease.IN).pos(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(12, -0.4f, 0, 0).rot(14, 0.5f, 0, 0, Ease.IN).rot(22, -0.2f, 0, 0).rot(24, 0.6f, 0, 0, Ease.IN).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0).rot(14, 0.3f, 0, 0).rot(24, 0.4f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(12, -3.0f, 0, -0.4f).rot(14, -0.4f, 0, 0.9f, Ease.IN).rot(22, -2.6f, 0, 0).rot(24, -0.2f, 0, 1.2f, Ease.IN).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(12, -3.0f, 0, 0.4f).rot(14, -0.4f, 0, -0.9f, Ease.IN).rot(22, -2.6f, 0, 0).rot(24, -0.2f, 0, -1.2f, Ease.IN).rot(40, 0, 0, 0)
            .bone("coat").rot(0, 0, 0, 0).rot(14, 1.1f, 0, 0).rot(24, 1.2f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(14, 0, 0, 0.4f).rot(24, 0, 0, 0.5f).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(14, 0, 0, -0.4f).rot(24, 0, 0, -0.5f).rot(40, 0, 0, 0).done());
    }

    @Override protected void idle(AdamUnit e, float ls, float la, float age, float yaw, float pitch) {
        boolean f1 = e.getForm() == 1;
        bone("sword").visible = f1;
        bone("head").yRot = yaw * Mth.DEG_TO_RAD; bone("head").xRot = pitch * Mth.DEG_TO_RAD;
        float s = Mth.cos(ls * 0.6662f) * la;
        bone("rightLeg").xRot = s * 1.2f; bone("leftLeg").xRot = -s * 1.2f;
        bone("rightArm").xRot = -s * 0.8f + (f1 ? -0.3f : 0); bone("leftArm").xRot = s * 0.8f;
        bone("rightArm").zRot = 0.05f + Mth.cos(age * 0.09f) * 0.04f; bone("leftArm").zRot = -0.05f - Mth.cos(age * 0.09f) * 0.04f;
        bone("coat").xRot = 0.1f + la * 0.9f + Mth.sin(age * 0.12f) * 0.05f;
        bone("torso").xRot = la * 0.15f;
        if (f1) { bone("body").y -= 3 + Mth.sin(age * 0.1f) * 1.2f; bone("rightLeg").xRot += 0.3f; bone("leftLeg").xRot += 0.15f; }
    }
}
