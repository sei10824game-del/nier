package com.nier.automata.unit.boss.engels;

import static com.nier.automata.unit.boss.engels.EngelsUnit.*;

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

/** Box design: rust-stained factory block torso with chimneys and lit windows, visor head, two bucket-wheel arms, pillar legs. */
public class EngelsModel extends NierModel<EngelsUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_engels");
    static final ResourceLocation TEX = BossClient.tex("boss_engels"), TEX_HOT = BossClient.tex("boss_engels_hot");

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, EngelsModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EngelsModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 2.0f, (EngelsUnit u) -> u.getForm() == 1 ? TEX_HOT : TEX, new EngelsModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 8, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-8, -20, -6, 16, 20, 12)
                .texOffs(88, 0).addBox(-6, -26, 2, 3, 6, 3).texOffs(88, 9).addBox(3, -26, 2, 3, 6, 3)
                .texOffs(0, 106).addBox(-10, -22, -8, 20, 2, 16).texOffs(0, 106).addBox(-9, -9, -7, 18, 2, 14), PartPose.ZERO);   // top deck + ring layer
        torso.addOrReplaceChild("boomR", CubeListBuilder.create().texOffs(72, 50).addBox(-18, -1, -1, 18, 2, 2), PartPose.offsetAndRotation(-7, -23, 0, 0, 0, 0.5f));
        torso.addOrReplaceChild("boomL", CubeListBuilder.create().texOffs(72, 72).addBox(0, -1, -1, 18, 2, 2), PartPose.offsetAndRotation(7, -23, 0, 0, 0, -0.5f));
        torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 0).addBox(-4, -5, -4, 8, 5, 8), PartPose.offset(0, -11, -5));
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-6, -2, -3, 6, 22, 6), PartPose.offset(-8, -16, 0));
        PartDefinition la = torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(72, 84).addBox(0, -2, -3, 6, 22, 6), PartPose.offset(8, -16, 0));
        ra.addOrReplaceChild("rightWheel", wheel(24, 32), PartPose.offset(-3, 22, 0));
        la.addOrReplaceChild("leftWheel", wheel(24, 56), PartPose.offset(3, 22, 0));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 80).addBox(-3.5f, 0, -4, 7, 16, 8), PartPose.offset(-4.5f, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(32, 80).addBox(-3.5f, 0, -4, 7, 16, 8), PartPose.offset(4.5f, 0, 0));
        for (String side : new String[]{"rightLeg", "leftLeg"}) body.getChild(side).addOrReplaceChild("foot", CubeListBuilder.create().texOffs(84, 112).addBox(-5, 16, -6, 10, 3, 12), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }
    /** Bucket wheel spinning about X with four buckets on its rim. */
    private static CubeListBuilder wheel(int u, int v) {
        return CubeListBuilder.create().texOffs(u, v).addBox(-2, -6, -6, 4, 12, 12)
                .texOffs(56, 32).addBox(-2.5f, -8, -1.5f, 5, 2, 3).texOffs(56, 32).addBox(-2.5f, 6, -1.5f, 5, 2, 3)
                .texOffs(56, 40).addBox(-2.5f, -1.5f, -8, 5, 3, 2).texOffs(56, 40).addBox(-2.5f, -1.5f, 6, 5, 3, 2);
    }

    public EngelsModel(ModelPart root) {
        super(root);
        bind("body", "body/torso", "body/torso/head", "body/torso/rightArm", "body/torso/leftArm",
             "body/torso/rightArm/rightWheel", "body/torso/leftArm/leftWheel", "body/rightLeg", "body/leftLeg");

        // heave right wheel overhead (0-18), hang, slam at 24 with torso following through
        clip(SLAM, AnimClip.of(40).blend(5)
            .bone("torso").rot(0, 0, 0, 0).rot(18, -0.35f, 0.4f, 0.1f).rot(21, -0.4f, 0.45f, 0.1f).rot(24, 0.55f, -0.2f, 0, Ease.IN).rot(32, 0.5f, -0.2f, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(18, 0.2f, -0.3f, 0).rot(24, -0.3f, 0, 0).rot(40, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(18, -3.0f, 0, 0.3f).rot(21, -3.1f, 0, 0.3f).rot(24, -1.2f, 0, 0, Ease.IN).rot(32, -1.15f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(18, 0.4f, 0, -0.3f).rot(24, -0.3f, 0, -0.2f).rot(40, 0, 0, 0)
            .bone("rightWheel").rot(0, 0, 0, 0).rot(24, -6f, 0, 0, Ease.IN).rot(40, -7f, 0, 0, Ease.OUT)
            .bone("body").pos(0, 0, 0, 0).pos(18, 0, -1, 1).pos(24, 0, 2, -2, Ease.IN).pos(32, 0, 2, -2).pos(40, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(18, 0.2f, 0, 0).rot(24, -0.5f, 0, 0).rot(32, -0.5f, 0, 0).rot(40, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(18, -0.2f, 0, 0).rot(24, 0.4f, 0, 0).rot(32, 0.4f, 0, 0).rot(40, 0, 0, 0).done());

        clip(DOUBLE_SLAM, AnimClip.of(48).blend(5)
            .bone("torso").rot(0, 0, 0, 0).rot(22, -0.5f, 0, 0).rot(26, -0.55f, 0, 0).rot(30, 0.7f, 0, 0, Ease.IN).rot(40, 0.65f, 0, 0).rot(48, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(22, 0.3f, 0, 0).rot(30, -0.4f, 0, 0).rot(48, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(22, -3.0f, 0, -0.2f).rot(26, -3.1f, 0, -0.25f).rot(30, -1.3f, 0, 0.1f, Ease.IN).rot(40, -1.25f, 0, 0.1f).rot(48, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(22, -3.0f, 0, 0.2f).rot(26, -3.1f, 0, 0.25f).rot(30, -1.3f, 0, -0.1f, Ease.IN).rot(40, -1.25f, 0, -0.1f).rot(48, 0, 0, 0)
            .bone("rightWheel").rot(0, 0, 0, 0).rot(30, -8f, 0, 0, Ease.IN).rot(48, -9f, 0, 0)
            .bone("leftWheel").rot(0, 0, 0, 0).rot(30, -8f, 0, 0, Ease.IN).rot(48, -9f, 0, 0)
            .bone("body").pos(0, 0, 0, 0).pos(22, 0, -2, 1).pos(30, 0, 3, -2, Ease.IN).pos(40, 0, 3, -2).pos(48, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(30, -0.6f, 0, 0.15f).rot(40, -0.6f, 0, 0.15f).rot(48, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(30, -0.6f, 0, -0.15f).rot(40, -0.6f, 0, -0.15f).rot(48, 0, 0, 0).done());

        // wind up to the right, horizontal swing across (22..30)
        clip(SWEEP, AnimClip.of(44).blend(5)
            .bone("torso").rot(0, 0, 0, 0).rot(18, 0.1f, 1.0f, 0).rot(22, 0.2f, 0.6f, 0).rot(30, 0.2f, -1.1f, 0, Ease.OUT).rot(36, 0.15f, -1.2f, 0).rot(44, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(18, 0, -0.7f, 0).rot(30, 0, 0.6f, 0).rot(44, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(18, -1.2f, 0.4f, 1.3f).rot(22, -1.5f, 0.2f, 1.0f).rot(30, -1.5f, -0.4f, -0.4f, Ease.OUT).rot(36, -1.4f, -0.5f, -0.5f).rot(44, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(18, 0.3f, 0, -0.6f).rot(30, -0.6f, 0, -0.9f).rot(44, 0, 0, 0)
            .bone("rightWheel").rot(0, 0, 0, 0).rot(30, -5f, 0, 0).rot(44, -6f, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(18, 0.3f, 0, 0.1f).rot(30, -0.4f, 0, 0).rot(44, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(18, -0.3f, 0, 0).rot(30, 0.4f, 0, -0.1f).rot(44, 0, 0, 0).done());

        // thrust wheel forward (16) and grind: rapid wheel spin + recoil shudder
        AnimClip g = AnimClip.of(50).blend(5);
        AnimClip.Bone ra = g.bone("rightArm").rot(0, 0, 0, 0).rot(12, -0.7f, 0, 0.5f).rot(16, -1.6f, 0, -0.1f, Ease.OUT);
        AnimClip.Bone to = g.bone("torso").rot(0, 0, 0, 0).rot(12, -0.2f, 0.5f, 0).rot(16, 0.3f, -0.1f, 0, Ease.OUT);
        for (int k = 18; k <= 42; k += 4) { ra.rot(k, -1.5f, 0, -0.05f).rot(k + 2, -1.65f, 0, -0.1f); to.rot(k, 0.28f, -0.08f, 0).rot(k + 2, 0.33f, -0.12f, 0); }
        ra.rot(50, 0, 0, 0); to.rot(50, 0, 0, 0);
        g.bone("rightWheel").rot(0, 0, 0, 0).rot(16, -3f, 0, 0, Ease.IN).rot(42, -40f, 0, 0, Ease.LINEAR).rot(50, -42f, 0, 0, Ease.OUT)
         .bone("leftArm").rot(0, 0, 0, 0).rot(16, 0.4f, 0, -0.4f).rot(42, 0.4f, 0, -0.4f).rot(50, 0, 0, 0)
         .bone("head").rot(0, 0, 0, 0).rot(16, 0.3f, 0.2f, 0).rot(42, 0.3f, 0.2f, 0).rot(50, 0, 0, 0)
         .bone("rightLeg").rot(0, 0, 0, 0).rot(16, -0.5f, 0, 0).rot(42, -0.5f, 0, 0).rot(50, 0, 0, 0)
         .bone("leftLeg").rot(0, 0, 0, 0).rot(16, 0.4f, 0, 0).rot(42, 0.4f, 0, 0).rot(50, 0, 0, 0);
        clip(GRINDER, g);

        // chest heaves on each volley (12/20/28), head tilts up to aim
        AnimClip b = AnimClip.of(40).blend(4);
        AnimClip.Bone bt = b.bone("torso").rot(0, 0, 0, 0), bh = b.bone("head").rot(0, 0, 0, 0), bb = b.bone("body").pos(0, 0, 0, 0);
        for (int f : new int[]{12, 20, 28}) { bt.rot(f - 3, -0.3f, 0, 0).rot(f, 0.12f, 0, 0, Ease.OUT); bh.rot(f - 3, -0.4f, 0, 0).rot(f, -0.1f, 0, 0); bb.pos(f - 3, 0, 0, 1).pos(f, 0, 0, -1, Ease.OUT); }
        bt.rot(40, 0, 0, 0); bh.rot(40, 0, 0, 0); bb.pos(40, 0, 0, 0);
        b.bone("rightArm").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0.7f).rot(32, -0.3f, 0, 0.7f).rot(40, 0, 0, 0)
         .bone("leftArm").rot(0, 0, 0, 0).rot(8, -0.3f, 0, -0.7f).rot(32, -0.3f, 0, -0.7f).rot(40, 0, 0, 0)
         .bone("rightLeg").rot(0, 0, 0, 0).rot(8, 0.2f, 0, 0).rot(32, 0.2f, 0, 0).rot(40, 0, 0, 0)
         .bone("leftLeg").rot(0, 0, 0, 0).rot(8, -0.2f, 0, 0).rot(32, -0.2f, 0, 0).rot(40, 0, 0, 0);
        clip(BARRAGE, b);

        // shudder, rear up, arms flare outward at ignition (30)
        AnimClip o = AnimClip.of(50).blend(5);
        AnimClip.Bone ot = o.bone("torso").rot(0, 0, 0, 0);
        for (int k = 2; k < 26; k += 2) ot.rot(k, 0.05f * (k % 4 == 0 ? 1 : -1), 0, 0.04f * (k % 4 == 0 ? -1 : 1), Ease.LINEAR);
        ot.rot(28, -0.5f, 0, 0).rot(31, 0.2f, 0, 0, Ease.OUT).rot(50, 0, 0, 0);
        o.bone("rightArm").rot(0, 0, 0, 0).rot(28, -0.8f, 0, 1.2f).rot(31, -0.2f, 0, 0.6f, Ease.OUT).rot(50, 0, 0, 0)
         .bone("leftArm").rot(0, 0, 0, 0).rot(28, -0.8f, 0, -1.2f).rot(31, -0.2f, 0, -0.6f, Ease.OUT).rot(50, 0, 0, 0)
         .bone("rightWheel").rot(0, 0, 0, 0).rot(50, -20f, 0, 0, Ease.IN)
         .bone("leftWheel").rot(0, 0, 0, 0).rot(50, 20f, 0, 0, Ease.IN)
         .bone("head").rot(0, 0, 0, 0).rot(28, -0.6f, 0, 0).rot(31, 0.1f, 0, 0).rot(50, 0, 0, 0)
         .bone("body").pos(0, 0, 0, 0).pos(28, 0, -2, 0).pos(31, 0, 1, 0, Ease.OUT).pos(50, 0, 0, 0);
        clip(OVERHEAT, o);

        // burning wheel arm swept from right (-60 deg) to left (+60 deg) over 16..36
        clip(FLAME_SWEEP, AnimClip.of(50).blend(5)
            .bone("torso").rot(0, 0, 0, 0).rot(14, 0.2f, 1.0f, 0).rot(16, 0.25f, 0.9f, 0).rot(36, 0.25f, -0.9f, 0, Ease.LINEAR).rot(42, 0.2f, -1.0f, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(14, -1.5f, 0, 0.8f).rot(16, -1.6f, 0, 0.6f).rot(36, -1.6f, 0, -0.6f, Ease.LINEAR).rot(42, -1.4f, 0, -0.7f).rot(50, 0, 0, 0)
            .bone("rightWheel").rot(0, 0, 0, 0).rot(50, -25f, 0, 0, Ease.LINEAR)
            .bone("leftArm").rot(0, 0, 0, 0).rot(16, 0.3f, 0, -0.5f).rot(36, -0.5f, 0, -0.8f).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(16, 0.2f, -0.6f, 0).rot(36, 0.2f, 0.6f, 0).rot(50, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(16, 0.3f, 0, 0).rot(36, -0.3f, 0, 0).rot(50, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(16, -0.3f, 0, 0).rot(36, 0.3f, 0, 0).rot(50, 0, 0, 0).done());
    }

    @Override protected void idle(EngelsUnit e, float ls, float la, float age, float yaw, float pitch) {
        bone("head").yRot = Mth.clamp(yaw, -40, 40) * Mth.DEG_TO_RAD;
        float s = Mth.cos(ls * 0.4f) * la;
        bone("rightLeg").xRot = s * 0.6f; bone("leftLeg").xRot = -s * 0.6f;
        bone("rightArm").xRot = -s * 0.3f - 0.15f; bone("leftArm").xRot = s * 0.3f - 0.15f;
        bone("torso").zRot = s * 0.06f; bone("body").y += Math.abs(s) * 1.5f;
        float spin = age * (e.getForm() == 1 ? 0.25f : 0.08f);
        bone("rightWheel").xRot = -spin; bone("leftWheel").xRot = -spin;
        bone("torso").xRot = Mth.sin(age * 0.05f) * 0.02f;
    }
}
