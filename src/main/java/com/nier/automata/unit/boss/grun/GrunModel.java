package com.nier.automata.unit.boss.grun;

import static com.nier.automata.unit.boss.grun.GrunUnit.*;

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

/** Box design: barnacled sea-green armoured head on a submerged neck, EMP core on its brow, four laser masts, a hinged maw and two huge arms. */
public class GrunModel extends NierModel<GrunUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_grun");
    static final ResourceLocation TEX = BossClient.tex("boss_grun"), TEX_CORE = BossClient.tex("boss_grun_core");
    static final String[] LASERS = {"laser0", "laser1", "laser2", "laser3"};

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, GrunModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, GrunModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 4.0f, (GrunUnit u) -> u.getForm() == 0 ? TEX : TEX_CORE, new GrunModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition base = mesh.getRoot().addOrReplaceChild("base", CubeListBuilder.create().texOffs(68, 48).addBox(-8, -20, -6, 16, 20, 12), PartPose.offset(0, 24, 0));
        PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-10, -14, -8, 20, 14, 16), PartPose.offset(0, -20, 0));
        head.addOrReplaceChild("core", CubeListBuilder.create().texOffs(72, 0).addBox(-3, -3, -1, 6, 6, 1), PartPose.offset(0, -9, -8));
        float[][] lp = {{-8, -5}, {8, -5}, {-8, 5}, {8, 5}};
        for (int i = 0; i < 4; i++)
            head.addOrReplaceChild(LASERS[i], CubeListBuilder.create().texOffs(72, 8).addBox(-1.5f, -5, -1.5f, 3, 5, 3), PartPose.offset(lp[i][0], -14, lp[i][1]));
        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 30).addBox(-8, 0, -12, 16, 3, 12), PartPose.offset(0, -1, 4));
        jaw.addOrReplaceChild("maw", CubeListBuilder.create().texOffs(56, 30).addBox(-6, -1.5f, -11, 12, 1, 9), PartPose.ZERO);
        PartDefinition ra = head.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 48).addBox(-2, -2, -2, 4, 24, 4), PartPose.offsetAndRotation(-10, -10, 0, -0.4f, 0, 0.3f));
        PartDefinition la = head.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 76).addBox(-2, -2, -2, 4, 24, 4), PartPose.offsetAndRotation(10, -10, 0, -0.4f, 0, -0.3f));
        ra.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(28, 48).addBox(-5, 0, -5, 10, 6, 10), PartPose.offset(0, 22, 0));
        la.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(28, 76).addBox(-5, 0, -5, 10, 6, 10), PartPose.offset(0, 22, 0));
        head.addOrReplaceChild("rightArm2", CubeListBuilder.create().texOffs(0, 48).addBox(-2, -2, -2, 4, 24, 4), PartPose.offsetAndRotation(-10, -4, 3, 0.1f, 0, 0.25f));
        head.addOrReplaceChild("leftArm2", CubeListBuilder.create().texOffs(0, 76).addBox(-2, -2, -2, 4, 24, 4), PartPose.offsetAndRotation(10, -4, 3, 0.1f, 0, -0.25f));
        head.addOrReplaceChild("stack", CubeListBuilder.create().texOffs(72, 8).addBox(-1.5f, -18, -1.5f, 3, 4, 3), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }

    public GrunModel(ModelPart root) {
        super(root);
        bind("base", "base/head", "base/head/core", "base/head/laser0", "base/head/laser1", "base/head/laser2", "base/head/laser3",
             "base/head/jaw", "base/head/jaw/maw", "base/head/rightArm", "base/head/leftArm", "base/head/rightArm/rightHand", "base/head/leftArm/leftHand");

        // right arm rears up behind the head (0-24), hammers down at 30, head nods into it
        clip(SMASH, AnimClip.of(50).blend(6)
            .bone("rightArm").rot(0, 0, 0, 0).rot(22, -2.2f, 0.2f, -0.2f).rot(26, -2.4f, 0.2f, -0.25f).rot(30, 0.2f, 0, 0.1f, Ease.IN).rot(40, 0.15f, 0, 0.1f).rot(50, 0, 0, 0)
            .bone("rightHand").rot(0, 0, 0, 0).rot(22, -0.6f, 0, 0).rot(30, 0.5f, 0, 0, Ease.IN).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(22, -0.2f, 0.25f, 0.06f).rot(30, 0.18f, -0.1f, 0, Ease.IN).rot(40, 0.15f, -0.1f, 0).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(22, 0.3f, 0, 0.2f).rot(30, -0.2f, 0, -0.1f).rot(50, 0, 0, 0)
            .bone("jaw").rot(0, 0, 0, 0).rot(26, 0.2f, 0, 0).rot(30, 0, 0, 0).rot(50, 0, 0, 0)
            .bone("base").pos(0, 0, 0, 0).pos(30, 0, 0.6f, -0.5f, Ease.IN).pos(34, 0, -0.3f, 0).pos(50, 0, 0, 0).done());

        // left arm swings across the front from left to right (26..34)
        clip(SWEEP, AnimClip.of(50).blend(6)
            .bone("leftArm").rot(0, 0, 0, 0).rot(20, -0.8f, -0.9f, -1.2f).rot(26, -1.0f, -0.6f, -1.0f).rot(34, -1.1f, 1.0f, 0.5f, Ease.OUT).rot(40, -1.0f, 1.1f, 0.5f).rot(50, 0, 0, 0)
            .bone("leftHand").rot(0, 0, 0, 0).rot(20, 0, 0, -0.4f).rot(34, 0, 0, 0.5f).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(20, 0, -0.35f, -0.05f).rot(34, 0.05f, 0.4f, 0.05f, Ease.OUT).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(20, 0.3f, 0, 0.3f).rot(34, -0.3f, 0, 0.5f).rot(50, 0, 0, 0)
            .bone("base").rot(0, 0, 0, 0).rot(20, 0, -0.1f, 0).rot(34, 0, 0.12f, 0).rot(50, 0, 0, 0).done());

        // masts swivel and lower toward the foe while charging, converge 20..50
        AnimClip q = AnimClip.of(60).blend(5)
            .bone("head").rot(0, 0, 0, 0).rot(18, -0.15f, 0, 0).rot(20, 0.05f, 0, 0, Ease.OUT).rot(50, 0.05f, 0, 0).rot(60, 0, 0, 0)
            .bone("core").scale(0, 1, 1, 1).scale(18, 1.4f, 1.4f, 1.4f).scale(50, 1.3f, 1.3f, 1.3f).scale(60, 1, 1, 1)
            .bone("rightArm").rot(0, 0, 0, 0).rot(18, 0.3f, 0, 0.4f).rot(50, 0.3f, 0, 0.4f).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(18, 0.3f, 0, -0.4f).rot(50, 0.3f, 0, -0.4f).rot(60, 0, 0, 0).done();
        for (int i = 0; i < 4; i++) {
            float x = i < 2 ? -0.7f : -0.3f, z = i % 2 == 0 ? 0.3f : -0.3f;
            q.bone(LASERS[i]).rot(0, 0, 0, 0).rot(18, x, 0, z).rot(20, x * 0.9f, 0, z * 0.9f, Ease.OUT).rot(50, x * 0.6f, 0, z * 0.3f).rot(60, 0, 0, 0)
             .pos(0, 0, 0, 0).pos(18, 0, -1.5f, 0).pos(20, 0, 0.5f, 0, Ease.OUT).pos(60, 0, 0, 0);
        }
        clip(QUAD_LASER, q);

        // brow core pulses on each volley (10/18/26), head bobs back with recoil
        AnimClip em = AnimClip.of(40).blend(4);
        AnimClip.Bone c = em.bone("core").scale(0, 1, 1, 1), h = em.bone("head").rot(0, 0, 0, 0);
        for (int f : new int[]{10, 18, 26}) { c.scale(f - 3, 1.5f, 1.5f, 1.5f).scale(f, 0.8f, 0.8f, 1).scale(f + 3, 1, 1, 1); h.rot(f - 3, 0.08f, 0, 0).rot(f, -0.1f, 0, 0, Ease.OUT); }
        c.scale(40, 1, 1, 1); h.rot(40, 0, 0, 0);
        em.bone("jaw").rot(0, 0, 0, 0).rot(10, 0.15f, 0, 0).rot(26, 0.15f, 0, 0).rot(40, 0, 0, 0);
        clip(EMP_SHOTS, em);

        // each mast blows off in turn (10/16/22/28) - head jerks with every blast - then the core is ripped open (34)
        AnimClip br = AnimClip.of(50).blend(5);
        AnimClip.Bone bh = br.bone("head").rot(0, 0, 0, 0);
        for (int i = 0; i < 4; i++) {
            int f = 10 + i * 6;
            br.bone(LASERS[i]).pos(0, 0, 0, 0).pos(f, 0, 0, 0).pos(f + 4, i % 2 == 0 ? -6 : 6, -10, i < 2 ? -3 : 3, Ease.OUT)
              .rot(0, 0, 0, 0).rot(f, 0, 0, 0).rot(f + 4, 1.5f, 0, i % 2 == 0 ? -1.2f : 1.2f).scale(0, 1, 1, 1).scale(f + 3, 1, 1, 1).scale(f + 5, 0, 0, 0).scale(50, 0, 0, 0);
            bh.rot(f, 0, 0, 0).rot(f + 1, -0.08f, 0, i % 2 == 0 ? 0.06f : -0.06f, Ease.OUT);
        }
        bh.rot(30, 0.1f, 0, 0).rot(34, -0.35f, 0, 0, Ease.OUT).rot(50, 0, 0, 0);
        br.bone("core").scale(0, 1, 1, 1).scale(32, 1, 1, 1).scale(34, 2, 2, 2).scale(50, 1.3f, 1.3f, 1.3f)
          .bone("rightArm").rot(0, 0, 0, 0).rot(34, -1.2f, 0, 0.9f).rot(50, 0, 0, 0)
          .bone("leftArm").rot(0, 0, 0, 0).rot(34, -1.2f, 0, -0.9f).rot(50, 0, 0, 0);
        clip(BREACH, br);

        // core flares per sphere (14..26 every 4), arms brace
        AnimClip cs = AnimClip.of(40).blend(4);
        AnimClip.Bone cc = cs.bone("core").scale(0, 1, 1, 1);
        for (int f = 14; f <= 26; f += 4) cc.scale(f - 2, 1.8f, 1.8f, 1.5f).scale(f, 1.1f, 1.1f, 1);
        cc.scale(40, 1, 1, 1);
        cs.bone("head").rot(0, 0, 0, 0).rot(12, -0.12f, 0, 0).rot(28, -0.12f, 0, 0).rot(40, 0, 0, 0)
          .bone("rightArm").rot(0, 0, 0, 0).rot(12, 0.3f, 0.3f, 0.3f).rot(28, 0.3f, 0.3f, 0.3f).rot(40, 0, 0, 0)
          .bone("leftArm").rot(0, 0, 0, 0).rot(12, 0.3f, -0.3f, -0.3f).rot(28, 0.3f, -0.3f, -0.3f).rot(40, 0, 0, 0);
        clip(CORE_SPHERES, cs);

        // roar: head rears back, jaw unhinges (40), arms flung wide
        clip(MAW_OPEN, AnimClip.of(60).blend(5)
            .bone("head").rot(0, 0, 0, 0).rot(20, -0.2f, 0, 0).rot(36, -0.45f, 0, 0).rot(40, 0.1f, 0, 0, Ease.OUT).rot(60, 0, 0, 0)
            .bone("jaw").rot(0, 0, 0, 0).rot(20, 0.2f, 0, 0).rot(36, 0.4f, 0, 0).rot(40, 0.9f, 0, 0, Ease.OUT).rot(60, 0.6f, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(36, -1.6f, 0, 1.0f).rot(40, -0.8f, 0, 1.3f, Ease.OUT).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(36, -1.6f, 0, -1.0f).rot(40, -0.8f, 0, -1.3f, Ease.OUT).rot(60, 0, 0, 0)
            .bone("base").pos(0, 0, 0, 0).pos(36, 0, -1, 0).pos(40, 0, 0.5f, 0, Ease.OUT).pos(60, 0, 0, 0).done());

        // launch tubes on the back: head pitched forward, arms shield, shudders per salvo
        AnimClip ms = AnimClip.of(60).blend(5);
        AnimClip.Bone mh = ms.bone("head").rot(0, 0, 0, 0).rot(8, 0.25f, 0, 0);
        for (int f = 10; f <= 40; f += 3) mh.rot(f, 0.25f, 0, f % 2 == 0 ? 0.03f : -0.03f, Ease.LINEAR);
        mh.rot(48, 0.25f, 0, 0).rot(60, 0, 0, 0);
        ms.bone("rightArm").rot(0, 0, 0, 0).rot(8, -0.9f, -0.5f, 0.2f).rot(44, -0.9f, -0.5f, 0.2f).rot(60, 0, 0, 0)
          .bone("leftArm").rot(0, 0, 0, 0).rot(8, -0.9f, 0.5f, -0.2f).rot(44, -0.9f, 0.5f, -0.2f).rot(60, 0, 0, 0)
          .bone("jaw").rot(0, 0, 0, 0).rot(10, -0.2f, 0, 0).rot(44, -0.2f, 0, 0).rot(60, 0, 0, 0);
        clip(MISSILES, ms);

        // maw gapes wide, glows (0-24), beam 24..50 with head tracking down
        clip(MAW_CANNON, AnimClip.of(60).blend(5)
            .bone("jaw").rot(0, 0, 0, 0).rot(22, 0.6f, 0, 0).rot(24, 0.75f, 0, 0, Ease.OUT).rot(50, 0.75f, 0, 0).rot(60, 0, 0, 0)
            .bone("maw").scale(0, 1, 1, 1).scale(22, 1.2f, 2.5f, 1.1f).scale(50, 1.2f, 2.5f, 1.1f).scale(60, 1, 1, 1)
            .bone("head").rot(0, 0, 0, 0).rot(22, -0.3f, 0, 0).rot(24, 0.05f, 0, 0, Ease.OUT).rot(50, 0.15f, 0, 0).rot(60, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(22, 0.5f, 0, 0.5f).rot(50, 0.5f, 0, 0.5f).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(22, 0.5f, 0, -0.5f).rot(50, 0.5f, 0, -0.5f).rot(60, 0, 0, 0)
            .bone("base").pos(0, 0, 0, 0).pos(24, 0, 0, 1, Ease.OUT).pos(50, 0, 0, 0.6f).pos(60, 0, 0, 0).done());
    }

    @Override protected void idle(GrunUnit e, float ls, float la, float age, float yaw, float pitch) {
        int f = e.getForm();
        for (String l : LASERS) bone(l).visible = f == 0 || e.getSkillId() == BREACH;
        bone("maw").visible = f == 2 || e.getSkillId() == MAW_CANNON;
        bone("base").y += Mth.sin(age * 0.04f) * 0.8f;
        bone("head").yRot = Mth.clamp(yaw, -30, 30) * Mth.DEG_TO_RAD * 0.4f;
        bone("head").zRot = Mth.sin(age * 0.03f) * 0.02f;
        bone("rightArm").xRot += Mth.sin(age * 0.05f) * 0.05f; bone("leftArm").xRot += Mth.sin(age * 0.05f + 1) * 0.05f;
        if (f == 2) bone("jaw").xRot = 0.5f + Mth.sin(age * 0.2f) * 0.05f;
        float pulse = f == 0 ? 1f : 1.15f + Mth.sin(age * 0.3f) * 0.1f;
        bone("core").xScale = bone("core").yScale = pulse;
    }
}
