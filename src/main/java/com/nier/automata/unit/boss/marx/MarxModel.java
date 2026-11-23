package com.nier.automata.unit.boss.marx;

import static com.nier.automata.unit.boss.marx.MarxUnit.*;

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

/** Box design: tracked excavator chassis, rotating turret with cab and red eye, long boom ending in a toothed saw wheel. */
public class MarxModel extends NierModel<MarxUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_marx");
    static final float BOOM_REST = 0.3f;

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, MarxModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, MarxModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 2.0f, BossClient.tex("boss_marx"), new MarxModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition ch = mesh.getRoot().addOrReplaceChild("chassis", CubeListBuilder.create().texOffs(0, 0).addBox(-9, -10, -11, 18, 6, 22)
                .texOffs(0, 28).addBox(-12, -4, -12, 24, 2, 24).texOffs(0, 28).addBox(-12, -2, -12, 24, 2, 24)
                .texOffs(0, 104).addBox(-10, -6, -10, 20, 2, 20), PartPose.offset(0, 24, 0));   // stepped grey base
        PartDefinition tu = ch.addOrReplaceChild("turret", CubeListBuilder.create().texOffs(0, 58).addBox(-7, -8, -6, 14, 8, 12), PartPose.offset(0, -10, 0));
        tu.addOrReplaceChild("towerA", CubeListBuilder.create().texOffs(64, 104).addBox(2, -24, 2, 4, 16, 4), PartPose.ZERO);
        tu.addOrReplaceChild("towerB", CubeListBuilder.create().texOffs(64, 104).addBox(-6, -24, 2, 4, 16, 4), PartPose.ZERO);
        tu.addOrReplaceChild("conveyor", CubeListBuilder.create().texOffs(76, 40).addBox(-2, -2, 0, 4, 4, 22), PartPose.offset(0, -4, 6));
        tu.addOrReplaceChild("cab", CubeListBuilder.create().texOffs(52, 58).addBox(-3, -6, -3, 6, 6, 6), PartPose.offset(-3, -8, 1));
        PartDefinition boom = tu.addOrReplaceChild("boom", CubeListBuilder.create().texOffs(0, 80).addBox(-2, -2, -20, 4, 4, 20),
                PartPose.offsetAndRotation(2, -5, -5, BOOM_REST, 0, 0));
        boom.addOrReplaceChild("saw", CubeListBuilder.create().texOffs(56, 80).addBox(-1.5f, -8, -8, 3, 16, 16)
                .texOffs(96, 80).addBox(-2, -10, -1, 4, 2, 2).texOffs(96, 80).addBox(-2, 8, -1, 4, 2, 2)
                .texOffs(96, 84).addBox(-2, -1, -10, 4, 2, 2).texOffs(96, 84).addBox(-2, -1, 8, 4, 2, 2), PartPose.offset(0, 0, -20));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public MarxModel(ModelPart root) {
        super(root);
        bind("chassis", "chassis/turret", "chassis/turret/cab", "chassis/turret/boom", "chassis/turret/boom/saw");

        // slow, telegraphed sweep: turret winds to the right (0-24), swings across to the left (24-44)
        clip(SWEEP, AnimClip.of(60).blend(6)
            .bone("turret").rot(0, 0, 0, 0).rot(20, 0, 1.3f, 0).rot(24, 0, 1.35f, 0).rot(44, 0, -1.3f, 0, Ease.LINEAR).rot(50, 0, -1.4f, 0).rot(60, 0, 0, 0)
            .bone("boom").rot(0, 0, 0, 0).rot(20, 0.25f, 0, 0).rot(24, 0.35f, 0, 0).rot(44, 0.35f, 0, 0).rot(60, 0, 0, 0)
            .bone("saw").rot(0, 0, 0, 0).rot(24, -6f, 0, 0, Ease.IN).rot(44, -30f, 0, 0, Ease.LINEAR).rot(60, -34f, 0, 0, Ease.OUT)
            .bone("cab").rot(0, 0, 0, 0).rot(20, 0, -0.3f, 0).rot(44, 0, 0.3f, 0).rot(60, 0, 0, 0)
            .bone("chassis").pos(0, 0, 0, 0).pos(24, 0, 0, 0).pos(30, 0.6f, 0, 0).pos(36, -0.6f, 0, 0).pos(44, 0, 0, 0).done());

        // boom pulls back and lifts (0-18), rams forward (22), grinds, retracts
        AnimClip th = AnimClip.of(40).blend(4);
        th.bone("boom").rot(0, 0, 0, 0).rot(18, -0.5f, 0, 0).rot(22, 0.1f, 0, 0, Ease.IN).rot(30, 0.1f, 0, 0).rot(40, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(18, 0, 0, 6).pos(22, 0, 0, -4, Ease.IN).pos(30, 0, 0, -4).pos(40, 0, 0, 0);
        AnimClip.Bone ch = th.bone("chassis").pos(0, 0, 0, 0).pos(18, 0, 0, 1);
        for (int k = 22; k <= 30; k += 2) ch.pos(k, 0, k % 4 == 2 ? -0.4f : 0, -0.6f, Ease.LINEAR);
        ch.pos(40, 0, 0, 0);
        th.bone("saw").rot(0, 0, 0, 0).rot(22, -10f, 0, 0, Ease.IN).rot(30, -24f, 0, 0, Ease.LINEAR).rot(40, -27f, 0, 0, Ease.OUT)
          .bone("turret").rot(0, 0, 0, 0).rot(18, 0.08f, 0, 0).rot(22, -0.05f, 0, 0).rot(40, 0, 0, 0)
          .bone("cab").rot(0, 0, 0, 0).rot(18, 0.2f, 0, 0).rot(22, -0.1f, 0, 0).rot(40, 0, 0, 0);
        clip(THRUST, th);

        // cab recoils on each shot (12,18,24,30); boom raised out of the way
        AnimClip v = AnimClip.of(40).blend(4);
        AnimClip.Bone cab = v.bone("cab").rot(0, 0, 0, 0).pos(0, 0, 0, 0);
        for (int k = 12; k <= 30; k += 6) cab.rot(k - 2, -0.15f, 0, 0).rot(k, 0.15f, 0, 0, Ease.OUT).pos(k - 2, 0, 0, 0).pos(k, 0, 0, 1.5f, Ease.OUT);
        cab.rot(40, 0, 0, 0).pos(40, 0, 0, 0);
        v.bone("boom").rot(0, 0, 0, 0).rot(10, -0.8f, 0, 0).rot(32, -0.8f, 0, 0).rot(40, 0, 0, 0)
         .bone("turret").rot(0, 0, 0, 0).rot(10, 0, 0.2f, 0).rot(20, 0, -0.2f, 0).rot(30, 0, 0.15f, 0).rot(40, 0, 0, 0);
        clip(VOLLEY, v);
    }

    @Override protected void idle(MarxUnit e, float ls, float la, float age, float yaw, float pitch) {
        bone("saw").xRot = -age * 0.15f;
        bone("chassis").y += Mth.sin(ls * 1.5f) * la * 0.4f;
        bone("turret").yRot = Mth.clamp(yaw, -30, 30) * Mth.DEG_TO_RAD * 0.5f;
        bone("boom").xRot = Mth.sin(age * 0.05f) * 0.04f;
    }
}
