package com.nier.automata.unit.boss.simone;

import static com.nier.automata.unit.boss.simone.SimoneUnit.*;

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

/** Original box design: tiered bell-shaped metal skirt, slender porcelain torso, single-lens crowned head, long thin arms; fan plates in form 1. */
public class SimoneModel extends NierModel<SimoneUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_simone");

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, SimoneModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, SimoneModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 2.0f, BossClient.tex("boss_simone"), new SimoneModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 10, 0));
        body.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(0, 0).addBox(-6, 0, -6, 12, 4, 12)
                .texOffs(0, 16).addBox(-8, 4, -8, 16, 5, 16).texOffs(0, 37).addBox(-10, 9, -10, 20, 5, 20), PartPose.ZERO);
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(48, 0).addBox(-3, -10, -2, 6, 10, 4), PartPose.ZERO);
        torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(68, 0).addBox(-3, -6, -3, 6, 6, 6)
                .texOffs(92, 0).addBox(-4, -9, -1, 8, 3, 2), PartPose.offset(0, -10, 0));
        torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(80, 16).addBox(-1, -1, -1, 2, 16, 2), PartPose.offset(-4, -9, 0));
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(88, 16).addBox(-1, -1, -1, 2, 16, 2), PartPose.offset(4, -9, 0));
        torso.addOrReplaceChild("wings", CubeListBuilder.create().texOffs(0, 62).addBox(-14, -8, 0, 12, 16, 1)
                .texOffs(0, 62).addBox(2, -8, 0, 12, 16, 1), PartPose.offset(0, -6, 2.5f));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public SimoneModel(ModelPart root) {
        super(root);
        bind("body", "body/skirt", "body/torso", "body/torso/head", "body/torso/rightArm", "body/torso/leftArm", "body/torso/wings");

        // arms gather in (0-10), whole figure whirls 3 turns (10..34), skirt flares outward, unwinds
        clip(PIROUETTE, AnimClip.of(40)
            .bone("body").rot(0, 0, 0, 0).rot(10, 0, -0.6f, 0).rot(34, 0, 18.25f, 0, Ease.LINEAR).rot(40, 0, 18.85f, 0, Ease.OUT).pos(0, 0, 0, 0).pos(10, 0, 1, 0).pos(14, 0, -2, 0).pos(34, 0, -2, 0).pos(40, 0, 0, 0)
            .bone("skirt").scale(0, 1, 1, 1).scale(10, 0.95f, 1, 0.95f).scale(14, 1.35f, 0.85f, 1.35f).scale(34, 1.35f, 0.85f, 1.35f).scale(40, 1, 1, 1)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -2.6f, 0, 0.6f).rot(14, 0, 0, 1.5f, Ease.OUT).rot(34, 0, 0, 1.5f).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -2.6f, 0, -0.6f).rot(14, 0, 0, -1.5f, Ease.OUT).rot(34, 0, 0, -1.5f).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0).rot(14, -0.3f, 0, 0).rot(34, -0.3f, 0, 0).rot(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(10, 0.15f, 0, 0).rot(14, -0.1f, 0, 0).rot(40, 0, 0, 0).done());

        // conductor's sweeps: each ring released with an arm flourish at 15 / 25 / 35
        AnimClip p = AnimClip.of(50);
        AnimClip.Bone pr = p.bone("rightArm").rot(0, 0, 0, 0), pl = p.bone("leftArm").rot(0, 0, 0, 0), pt = p.bone("torso").rot(0, 0, 0, 0), ph = p.bone("head").rot(0, 0, 0, 0);
        for (int f : new int[]{15, 25, 35}) {
            boolean right = f != 25;
            (right ? pr : pl).rot(f - 5, -2.8f, 0, right ? -0.3f : 0.3f).rot(f, -1.0f, 0, right ? 1.4f : -1.4f, Ease.OUT);
            (right ? pl : pr).rot(f, -0.4f, 0, right ? -0.8f : 0.8f);
            pt.rot(f - 5, -0.25f, right ? 0.4f : -0.4f, 0).rot(f, 0.15f, right ? -0.3f : 0.3f, 0, Ease.OUT);
            ph.rot(f - 5, -0.4f, 0, 0).rot(f, 0.1f, 0, 0);
        }
        pr.rot(50, 0, 0, 0); pl.rot(50, 0, 0, 0); pt.rot(50, 0, 0, 0); ph.rot(50, 0, 0, 0);
        p.bone("skirt").rot(0, 0, 0, 0).rot(15, 0, 0.4f, 0).rot(25, 0, -0.4f, 0).rot(35, 0, 0.4f, 0).rot(50, 0, 0, 0);
        clip(PETALS, p);

        // head tilts back, arms frame the lens (charge 0-16), body leans to track while the light burns (16-44)
        clip(SPOTLIGHT, AnimClip.of(50)
            .bone("head").rot(0, 0, 0, 0).rot(14, -0.6f, 0, 0).rot(16, 0.35f, 0, 0, Ease.OUT).rot(44, 0.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(14, -0.35f, 0, 0).rot(16, 0.2f, 0, 0, Ease.OUT).rot(30, 0.25f, 0.2f, 0).rot(44, 0.25f, -0.2f, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(14, -2.7f, 0, 0.5f).rot(16, -2.3f, 0, 0.9f).rot(44, -2.3f, 0, 0.9f).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(14, -2.7f, 0, -0.5f).rot(16, -2.3f, 0, -0.9f).rot(44, -2.3f, 0, -0.9f).rot(50, 0, 0, 0)
            .bone("skirt").scale(0, 1, 1, 1).scale(16, 1.08f, 0.95f, 1.08f).scale(44, 1.08f, 0.95f, 1.08f).scale(50, 1, 1, 1).done());

        // plie (0-8), launch with arms sweeping up, split-leap pose, land in deep curtsey (28)
        clip(JETE, AnimClip.of(40)
            .bone("body").pos(0, 0, 0, 0).pos(7, 0, 3, 0).pos(10, 0, -3, 0, Ease.OUT).pos(26, 0, -1, 0).pos(28, 0, 4, 0, Ease.IN).pos(34, 0, 4, 0).pos(40, 0, 0, 0)
                .rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(20, 0.4f, 0, 0).rot(28, 0.2f, 0, 0).rot(40, 0, 0, 0)
            .bone("skirt").scale(0, 1, 1, 1).scale(7, 1.1f, 0.8f, 1.1f).scale(12, 0.85f, 1.2f, 0.85f).scale(26, 0.9f, 1.1f, 0.9f).scale(28, 1.4f, 0.7f, 1.4f).scale(34, 1.3f, 0.75f, 1.3f).scale(40, 1, 1, 1)
            .bone("rightArm").rot(0, 0, 0, 0).rot(7, 0.3f, 0, 0.4f).rot(12, -3.0f, 0, -0.2f, Ease.OUT).rot(20, -2.0f, 0, 1.2f).rot(28, 0.2f, 0, 1.3f, Ease.IN).rot(40, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(7, 0.3f, 0, -0.4f).rot(12, -3.0f, 0, 0.2f, Ease.OUT).rot(20, -2.0f, 0, -1.2f).rot(28, 0.2f, 0, -1.3f, Ease.IN).rot(40, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(7, 0.3f, 0, 0).rot(12, -0.4f, 0, 0).rot(28, 0.6f, 0, 0, Ease.IN).rot(34, 0.55f, 0, 0).rot(40, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(12, -0.5f, 0, 0).rot(28, 0.4f, 0, 0).rot(40, 0, 0, 0).done());

        // right arm drawn back across the body, whips out horizontally at 14 with torso twist and follow-through
        clip(LASH, AnimClip.of(30)
            .bone("torso").rot(0, 0, 0, 0).rot(10, 0, 0.9f, 0).rot(14, 0.1f, -0.7f, 0, Ease.OUT).rot(20, 0.05f, -0.9f, 0).rot(30, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -1.4f, 0.6f, -1.2f).rot(14, -1.6f, -0.3f, 0.6f, Ease.OUT).rot(20, -1.3f, -0.6f, 0.9f).rot(30, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, 0.3f, 0, -0.9f).rot(14, -0.5f, 0, -1.2f).rot(30, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, 0, -0.6f, 0).rot(14, 0, 0.4f, 0).rot(30, 0, 0, 0)
            .bone("skirt").rot(0, 0, 0, 0).rot(10, 0, 0.3f, 0).rot(14, 0, -0.4f, 0, Ease.OUT).rot(30, 0, 0, 0).done());

        // rises, folds in on herself, then unfurls the fan plates (30)
        clip(CRESCENDO, AnimClip.of(50)
            .bone("body").pos(0, 0, 0, 0).pos(28, 0, -4, 0).pos(30, 0, -5, 0).pos(50, 0, -2, 0).rot(0, 0, 0, 0).rot(28, 0, 3.14f, 0).rot(50, 0, 6.28f, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(20, 0.7f, 0, 0).rot(28, 0.75f, 0, 0).rot(31, -0.4f, 0, 0, Ease.OUT).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(20, 0.6f, 0, 0).rot(31, -0.7f, 0, 0).rot(50, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(20, -0.6f, 0, -0.9f).rot(28, -0.5f, 0, -1.0f).rot(31, -0.2f, 0, 2.6f, Ease.OUT).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(20, -0.6f, 0, 0.9f).rot(28, -0.5f, 0, 1.0f).rot(31, -0.2f, 0, -2.6f, Ease.OUT).rot(50, 0, 0, 0)
            .bone("wings").scale(0, 0, 0, 0).scale(28, 0, 0, 0).scale(32, 1.2f, 1.2f, 1).scale(50, 1, 1, 1)
            .bone("skirt").scale(0, 1, 1, 1).scale(28, 0.8f, 1.1f, 0.8f).scale(31, 1.4f, 0.8f, 1.4f).scale(50, 1, 1, 1).done());

        // slow continuous turn with arms out like a music box, matching the spiral emitters (10..50)
        clip(ARIA, AnimClip.of(60)
            .bone("body").rot(0, 0, 0, 0).rot(10, 0, 0, 0).rot(50, 0, 7.0f, 0, Ease.LINEAR).rot(60, 0, 7.6f, 0, Ease.OUT).pos(0, 0, 0, 0).pos(10, 0, -2, 0).pos(50, 0, -2, 0).pos(60, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 1.4f).rot(30, -0.6f, 0, 1.6f).rot(50, -0.3f, 0, 1.4f).rot(60, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(10, -0.3f, 0, -1.4f).rot(30, -0.6f, 0, -1.6f).rot(50, -0.3f, 0, -1.4f).rot(60, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(10, -0.4f, 0, 0.2f).rot(30, -0.4f, 0, -0.2f).rot(50, -0.4f, 0, 0.2f).rot(60, 0, 0, 0)
            .bone("wings").rot(0, 0, 0, 0).rot(10, 0.3f, 0, 0).rot(50, 0.3f, 0, 0).rot(60, 0, 0, 0)
            .bone("skirt").scale(0, 1, 1, 1).scale(10, 1.2f, 0.9f, 1.2f).scale(50, 1.2f, 0.9f, 1.2f).scale(60, 1, 1, 1).done());

        // both arms raised to the rafters (8), then drawn down slowly as the curtain falls (12..36)
        clip(CURTAIN, AnimClip.of(50)
            .bone("rightArm").rot(0, 0, 0, 0).rot(8, -3.0f, 0, -0.4f).rot(12, -2.9f, 0, -0.3f).rot(36, -0.6f, 0, 0.9f).rot(50, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(8, -3.0f, 0, 0.4f).rot(12, -2.9f, 0, 0.3f).rot(36, -0.6f, 0, -0.9f).rot(50, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(8, -0.7f, 0, 0).rot(36, 0.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("torso").rot(0, 0, 0, 0).rot(8, -0.3f, 0, 0).rot(36, 0.3f, 0, 0).rot(50, 0, 0, 0)
            .bone("wings").rot(0, 0, 0, 0).rot(8, -0.4f, 0, 0).rot(36, 0.4f, 0, 0).rot(50, 0, 0, 0)
            .bone("body").pos(0, 0, 0, 0).pos(8, 0, -2, 0).pos(36, 0, 1, 0).pos(50, 0, 0, 0).done());
    }

    @Override protected void idle(SimoneUnit e, float ls, float la, float age, float yaw, float pitch) {
        boolean f1 = e.getForm() == 1;
        bone("wings").visible = f1 || e.getSkillId() == CRESCENDO;
        bone("head").yRot = yaw * Mth.DEG_TO_RAD; bone("head").xRot = pitch * Mth.DEG_TO_RAD;
        // she glides rather than walks: skirt sways, arms float in a slow port de bras
        bone("skirt").zRot = Mth.sin(ls * 0.5f) * la * 0.1f;
        bone("skirt").yRot = Mth.sin(age * 0.05f) * 0.1f;
        bone("rightArm").zRot = 0.5f + Mth.sin(age * 0.07f) * 0.25f; bone("rightArm").xRot = -0.3f + Mth.cos(age * 0.07f) * 0.2f;
        bone("leftArm").zRot = -0.5f - Mth.sin(age * 0.07f + 1) * 0.25f; bone("leftArm").xRot = -0.3f + Mth.cos(age * 0.07f + 1) * 0.2f;
        bone("torso").zRot = Mth.sin(age * 0.05f) * 0.06f;
        if (f1) { bone("body").y -= 2 + Mth.sin(age * 0.1f); bone("wings").xRot = Mth.sin(age * 0.15f) * 0.15f; }
    }
}
