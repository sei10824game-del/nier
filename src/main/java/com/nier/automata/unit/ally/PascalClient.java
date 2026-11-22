package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.PascalUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.Map;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * Village-leader machine: original design - squat barrel body in pale teal plating, wide dome head with two round
 * amber lenses, stubby piston limbs and a patterned cloth poncho. Same bone names as the humanoid rig.
 */
public final class PascalClient {
    static final String ID = "ally_pascal";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition body = m.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 15, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 16).addBox(-5, -11, -4, 10, 11, 8), PartPose.ZERO);
        torso.addOrReplaceChild("poncho", CubeListBuilder.create().texOffs(0, 64).addBox(-6, -11.5f, -5, 12, 6, 10), PartPose.ZERO);
        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5, -7, -5, 10, 6, 10)
                .texOffs(40, 0).addBox(-4, -9, -4, 8, 2, 8), PartPose.offset(0, -11, 0));
        head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(72, 0).addBox(-4, -5.5f, -5.6f, 3, 3, 1).texOffs(72, 0).addBox(1, -5.5f, -5.6f, 3, 3, 1), PartPose.ZERO);
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(36, 16).addBox(-2, -1, -1.5f, 3, 9, 3)
                .texOffs(48, 16).addBox(-2.5f, 8, -2, 4, 3, 4), PartPose.offset(-6, -9, 0));
        ra.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.ZERO);
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(36, 16).addBox(-1, -1, -1.5f, 3, 9, 3)
                .texOffs(48, 16).addBox(-1.5f, 8, -2, 4, 3, 4), PartPose.offset(6, -9, 0));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(36, 32).addBox(-1.5f, 0, -1.5f, 3, 9, 3)
                .texOffs(48, 32).addBox(-2, 7, -3, 4, 2, 5), PartPose.offset(-2.5f, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(36, 32).addBox(-1.5f, 0, -1.5f, 3, 9, 3)
                .texOffs(48, 32).addBox(-2, 7, -3, 4, 2, 5), PartPose.offset(2.5f, 0, 0));
        body.addOrReplaceChild("pod", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        // pound: both fists hoisted over the dome, body rises on toes, then crashes down
        Pose hoist = Clips.P().r("rightArm", -3.0f, 0, -0.3f).r("leftArm", -3.0f, 0, 0.3f).r("torso", -0.35f, 0, 0).r("head", -0.25f, 0, 0)
                .p("body", 0, -2, 1).r("rightLeg", 0.2f, 0, 0.1f).r("leftLeg", 0.2f, 0, -0.1f);
        Pose smash = Clips.P().r("rightArm", -1.0f, 0, -0.25f).r("leftArm", -1.0f, 0, 0.25f).r("torso", 0.6f, 0, 0).r("head", 0.15f, 0, 0)
                .p("body", 0, 2.5f, -2).r("rightLeg", -0.6f, 0, 0.1f).r("leftLeg", 0.4f, 0, -0.1f);
        // tackle: shoulder lowered, crouched sprint, impact
        Pose brace = Clips.P().r("torso", 0.6f, -0.6f, 0).r("head", -0.4f, 0.4f, 0).r("rightArm", 0.3f, 0, 0.6f).r("leftArm", -1.2f, 0, -0.2f)
                .p("body", 0, 2, 1.5f).r("rightLeg", 0.5f, 0, 0).r("leftLeg", -0.7f, 0, 0);
        Pose ram = brace.with(Clips.P().r("torso", 0.8f, -0.8f, 0).p("body", 0, 2, -3).r("rightLeg", -0.9f, 0, 0).r("leftLeg", 0.8f, 0, 0));
        // rescue: arms open, head tilts up, glow at t=24
        Pose open = Clips.P().r("rightArm", -1.0f, 0, 0.9f).r("leftArm", -1.0f, 0, -0.9f).r("head", -0.35f, 0, 0).r("torso", -0.15f, 0, 0);
        Pose hold = Clips.P().r("rightArm", -1.4f, -0.5f, 0).r("leftArm", -1.4f, 0.5f, 0).r("head", 0.3f, 0, 0).r("torso", 0.2f, 0, 0).p("body", 0, 1.5f, 0);
        return Map.of(
            POUND, Clips.seq(28).key(10, hoist).key(13, hoist.with(Clips.P().p("body", 0, -3, 1.5f))).key(16, smash, Ease.IN).key(22, smash).build(),
            TACKLE, Clips.seq(26).key(8, brace).key(11, ram, Ease.OUT).key(17, ram).key(21, brace).build(),
            RESCUE, Clips.seq(40).key(8, hold).key(18, hold.with(Clips.P().r("torso", 0.3f, 0, 0))).key(24, open, Ease.OUT).key(34, open).build());
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, PascalClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<PascalUnit> t) {
        AllyClient.renderer(e, t, ID, 0.6f, u -> AllyClient.tex(ID), r -> new AllyClient.Model<>(r, clips()));
    }
}
