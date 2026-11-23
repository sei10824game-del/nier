package com.nier.automata.unit.boss.hegel;

import static com.nier.automata.unit.boss.hegel.HegelUnit.*;

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

/** Box design: floating armoured core with a red lens, ringed by eight linked spherical units (each with one eye). */
public class HegelModel extends NierModel<HegelUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_hegel");
    static final float R = 13f;

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, HegelModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, HegelModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 2.2f, BossClient.tex("boss_hegel"), new HegelModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition core = mesh.getRoot().addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-6, -6, -6, 12, 12, 12)
                .texOffs(48, 0).addBox(-2, -2, -7, 4, 4, 1), PartPose.offset(0, 10, 0));
        PartDefinition ring = mesh.getRoot().addOrReplaceChild("ring", CubeListBuilder.create(), PartPose.offset(0, 10, 0));
        for (int i = 0; i < 8; i++) {
            float a = i * Mth.PI / 4;
            ring.addOrReplaceChild("seg" + i, CubeListBuilder.create().texOffs(0, 24).addBox(-4, -4, -4, 8, 8, 8)
                    .texOffs(32, 24).addBox(-1, -1, -4.5f, 2, 2, 1), PartPose.offsetAndRotation(Mth.sin(a) * R, 0, -Mth.cos(a) * R, 0, -a, 0));
        }
        core.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 40).addBox(-8, -1, -8, 16, 2, 16), PartPose.offset(0, 6, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    private static final String[] SEG = {"seg0", "seg1", "seg2", "seg3", "seg4", "seg5", "seg6", "seg7"};

    public HegelModel(ModelPart root) {
        super(root);
        bind("core", "core/plate", "ring", "ring/seg0", "ring/seg1", "ring/seg2", "ring/seg3", "ring/seg4", "ring/seg5", "ring/seg6", "ring/seg7");

        // lens charges (core pulls back), then the whole structure yaws through the sweep 14..40
        clip(SWEEP_LASER, AnimClip.of(50)
            .bone("core").rot(0, 0, 0, 0).rot(12, -0.25f, 0, 0).rot(14, 0.1f, 0.87f, 0, Ease.OUT).rot(40, 0.1f, -0.87f, 0, Ease.LINEAR).rot(50, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(12, 0, -1, 2).pos(14, 0, 0, -1, Ease.OUT).pos(40, 0, 0, -1).pos(50, 0, 0, 0)
            .bone("ring").rot(0, 0, 0, 0).rot(12, 0.15f, 0, 0).rot(14, 0, 0.87f, 0).rot(40, 0, -0.87f, 0, Ease.LINEAR).rot(50, 0, 0, 0)
            .bone("plate").rot(0, 0, 0, 0).rot(12, 0.2f, 0, 0).rot(40, 0.2f, 0, 0).rot(50, 0, 0, 0).done());

        // core rises and the ring tilts like a dish, firing pillars down at 24
        AnimClip lat = AnimClip.of(44)
            .bone("core").pos(0, 0, 0, 0).pos(20, 0, -5, 0).pos(24, 0, -3, 0, Ease.OUT).pos(44, 0, 0, 0).rot(0, 0, 0, 0).rot(20, 0.4f, 0, 0).rot(24, 0.6f, 0, 0).rot(44, 0, 0, 0)
            .bone("ring").rot(0, 0, 0, 0).rot(20, 0, 1.2f, 0.3f).rot(24, 0, 1.6f, 0.35f, Ease.OUT).rot(44, 0, 3.14f, 0).pos(0, 0, 0, 0).pos(20, 0, -3, 0).pos(44, 0, 0, 0).done();
        for (String s : SEG) lat.bone(s).rot(0, 0, 0, 0).rot(20, 0.9f, 0, 0).rot(24, -0.3f, 0, 0, Ease.OUT).rot(44, 0, 0, 0);
        clip(LATTICE, lat);

        // segments turn outward (eyes face out) and fire; ring spins with the beams 18..32
        AnimClip rl = AnimClip.of(40)
            .bone("ring").rot(0, 0, 0, 0).rot(16, 0, 0, 0).rot(18, 0, -0.1f, 0).rot(32, 0, -0.95f, 0, Ease.LINEAR).rot(40, 0, -1.1f, 0)
            .bone("core").pos(0, 0, 0, 0).pos(16, 0, -2, 0).pos(18, 0, 1, 0, Ease.OUT).pos(32, 0, 1, 0).pos(40, 0, 0, 0).done();
        for (int i = 0; i < 8; i++) rl.bone(SEG[i]).rot(0, 0, 0, 0).rot(14, 0, 3.14f, 0).rot(32, 0, 3.14f, 0).rot(40, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(16, 0, 0, 0).pos(18, Mth.sin(i * Mth.PI / 4) * 3, 0, -Mth.cos(i * Mth.PI / 4) * 3, Ease.OUT).pos(32, Mth.sin(i * Mth.PI / 4) * 3, 0, -Mth.cos(i * Mth.PI / 4) * 3).pos(40, 0, 0, 0);
        clip(RING_LASERS, rl);

        // links strain, shudder, then the units fly apart at 30
        AnimClip sep = AnimClip.of(50)
            .bone("core").pos(0, 0, 0, 0).pos(28, 0, 2, 0).pos(30, 0, -3, 0, Ease.OUT).pos(50, 0, 0, 0).rot(0, 0, 0, 0).rot(10, 0.1f, 0, 0.1f).rot(14, -0.1f, 0, -0.1f).rot(18, 0.1f, 0, 0.1f).rot(22, -0.1f, 0, -0.1f).rot(30, 0, 0, 0).rot(50, 0, 0, 0)
            .bone("ring").rot(0, 0, 0, 0).rot(28, 0, 0.5f, 0).rot(50, 0, 6.28f, 0, Ease.OUT).done();
        for (int i = 0; i < 8; i++) {
            float sx = Mth.sin(i * Mth.PI / 4), sz = -Mth.cos(i * Mth.PI / 4);
            sep.bone(SEG[i]).pos(0, 0, 0, 0).pos(28, -sx, 0, -sz).pos(31, sx * 8, (i % 2 == 0 ? -4 : 3), sz * 8, Ease.OUT).pos(50, sx * 5, (i % 2 == 0 ? -2 : 2), sz * 5)
                    .rot(0, 0, 0, 0).rot(31, 0.6f, 0, 0).rot(50, 0, 0, 0);
        }
        clip(SEPARATE, sep);

        // three spheres in turn lift high (wind-up) then dive forward/down at 20, 26, 32
        AnimClip sl = AnimClip.of(44)
            .bone("ring").rot(0, 0, 0, 0).rot(6, 0, 0, 0).rot(38, 0, 0, 0).rot(44, 0, 0, 0)
            .bone("core").rot(0, 0, 0, 0).rot(18, -0.2f, 0, 0).rot(20, 0.2f, 0, 0).rot(26, 0.2f, 0, 0).rot(32, 0.25f, 0, 0).rot(44, 0, 0, 0).done();
        int[] who = {0, 1, 7};
        for (int j = 0; j < 3; j++) {
            int hit = 20 + j * 6; String s = SEG[who[j]];
            sl.bone(s).pos(0, 0, 0, 0).pos(hit - 8, 0, -14, 4).pos(hit, 0, 12, -26, Ease.IN).pos(hit + 4, 0, 10, -24).pos(44, 0, 0, 0)
              .rot(0, 0, 0, 0).rot(hit - 8, -1.2f, 0, 0).rot(hit, 1.4f, 0, 0, Ease.IN).rot(44, 0, 0, 0);
        }
        clip(SPHERE_SLAM, sl);

        // spheres contract into the core, then burst outward (16); three rise as homing launchers (30/34/38)
        AnimClip sb = AnimClip.of(50)
            .bone("core").pos(0, 0, 0, 0).pos(14, 0, 2, 0).pos(16, 0, -2, 0, Ease.OUT).pos(50, 0, 0, 0)
            .bone("ring").rot(0, 0, 0, 0).rot(14, 0, 2.0f, 0).rot(16, 0, 2.3f, 0).rot(50, 0, 3.14f, 0).done();
        for (int i = 0; i < 8; i++) {
            float sx = Mth.sin(i * Mth.PI / 4), sz = -Mth.cos(i * Mth.PI / 4);
            AnimClip.Bone b = sb.bone(SEG[i]).pos(0, 0, 0, 0).pos(14, -sx * 6, 0, -sz * 6).pos(16, sx * 12, 0, sz * 12, Ease.OUT).pos(24, sx * 6, 0, sz * 6);
            if (i < 3) b.pos(30 + i * 4 - 2, sx * 4, 2, sz * 4).pos(30 + i * 4, 0, -16, 0, Ease.OUT).pos(44, 0, -8, 0);
            b.pos(50, 0, 0, 0);
        }
        clip(SPHERE_BURST, sb);
    }

    @Override protected void idle(HegelUnit e, float ls, float la, float age, float yaw, float pitch) {
        boolean sep = e.getForm() == 1;
        bone("core").y += Mth.sin(age * 0.08f) * 1.2f;
        bone("core").yRot = Mth.clamp(yaw, -45, 45) * Mth.DEG_TO_RAD * 0.5f;
        bone("ring").y += Mth.sin(age * 0.08f + 0.6f) * 1.2f;
        bone("ring").yRot = age * (sep ? 0.05f : 0.015f);
        for (int i = 0; i < 8; i++) {
            ModelPart p = bone(SEG[i]);
            float a = i * Mth.PI / 4;
            p.y += Mth.sin(age * 0.15f + i) * (sep ? 3f : 0.6f);
            if (sep) { p.x += Mth.sin(a) * 5; p.z -= Mth.cos(a) * 5; p.xRot += age * 0.1f; }
        }
    }
}
