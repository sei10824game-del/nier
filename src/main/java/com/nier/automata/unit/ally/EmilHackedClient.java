package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.EmilHackedUnit.*;

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

/** Emil Hacked: original design - Emil's round head on a hulking dark frame with red circuit seams, huge gauntlets. */
public final class EmilHackedClient {
    static final String ID = "ally_emil_hacked";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition body = m.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 8, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 24).addBox(-8, -16, -5, 16, 16, 10)
                .texOffs(52, 24).addBox(-6, -4, -4, 12, 5, 8), PartPose.ZERO);
        EmilClient.emilHead(torso, -15);
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 52).addBox(-5, -2, -3, 5, 14, 6)
                .texOffs(24, 52).addBox(-6, 12, -4, 7, 7, 8), PartPose.offset(-8, -14, 0));
        ra.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.ZERO);
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 52).addBox(0, -2, -3, 5, 14, 6)
                .texOffs(24, 52).addBox(-1, 12, -4, 7, 7, 8), PartPose.offset(8, -14, 0));
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(56, 52).addBox(-3, 0, -3, 6, 16, 6), PartPose.offset(-4, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(56, 52).addBox(-3, 0, -3, 6, 16, 6), PartPose.offset(4, 0, 0));
        body.addOrReplaceChild("pod", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose cockR = Clips.P().r("rightArm", -0.6f, 0.6f, 0.6f).p("rightArm", 0, 0, 4).r("torso", 0, 0.6f, 0).r("head", 0, -0.4f, 0).r("leftArm", -0.9f, -0.3f, -0.2f)
                .p("body", 0, 1.5f, 1.5f).r("rightLeg", 0.4f, 0, 0).r("leftLeg", -0.4f, 0, 0);
        Pose punchR = Clips.P().r("rightArm", -1.6f, -0.2f, 0).p("rightArm", 0, 0, -4).r("torso", 0.2f, -0.6f, 0).r("head", 0, 0.4f, 0).r("leftArm", 0.3f, 0, -0.3f)
                .p("body", 0, 1.5f, -3).r("rightLeg", -0.5f, 0, 0).r("leftLeg", 0.5f, 0, 0);
        Pose cockL = Clips.P().r("leftArm", -0.6f, -0.6f, -0.6f).p("leftArm", 0, 0, 4).r("torso", 0, -0.7f, 0).r("head", 0, 0.4f, 0).r("rightArm", -0.9f, 0.3f, 0.2f)
                .p("body", 0, 2f, 0).r("rightLeg", -0.4f, 0, 0).r("leftLeg", 0.4f, 0, 0);
        Pose punchL = Clips.P().r("leftArm", -1.7f, 0.2f, 0).p("leftArm", 0, 0, -5).r("torso", 0.3f, 0.7f, 0).r("head", 0, -0.4f, 0).r("rightArm", 0.3f, 0, 0.3f)
                .p("body", 0, 2f, -4).r("rightLeg", 0.5f, 0, 0).r("leftLeg", -0.6f, 0, 0);
        Pose leap = Clips.P().r("rightArm", -3.0f, 0, -0.3f).r("leftArm", -3.0f, 0, 0.3f).r("torso", -0.3f, 0, 0).r("head", -0.3f, 0, 0)
                .r("rightLeg", -0.8f, 0, 0).r("leftLeg", -0.8f, 0, 0).p("body", 0, -2, 0);
        Pose slam = Clips.P().r("rightArm", -0.5f, 0, 0.3f).r("leftArm", -0.5f, 0, -0.3f).r("torso", 0.9f, 0, 0).r("head", 0.2f, 0, 0)
                .r("rightLeg", -1.2f, 0, 0.2f).r("leftLeg", -1.2f, 0, -0.2f).p("body", 0, 6, 0).p("rightLeg", 0, -3, -1).p("leftLeg", 0, -3, -1);
        Pose roar = Clips.P().r("rightArm", -0.8f, 0, 1.0f).r("leftArm", -0.8f, 0, -1.0f).r("torso", -0.25f, 0, 0).r("head", -0.3f, 0, 0).p("body", 0, 1, 1);
        Pose aimH = Clips.P().r("head", 0.1f, 0, 0).r("torso", 0.25f, 0, 0).r("rightArm", -0.8f, 0, 0.5f).r("leftArm", -0.8f, 0, -0.5f).p("body", 0, 2, 0)
                .r("rightLeg", 0.4f, 0, 0.1f).r("leftLeg", -0.5f, 0, -0.1f);
        Clips.Seq barrage = Clips.seq(36).key(6, roar);
        for (int t = 10; t <= 28; t += 3) barrage.key(t, roar.with(Clips.P().r("head", -0.45f, (t % 2 == 0 ? 0.2f : -0.2f), 0).p("body", 0, 1, 2)), Ease.OUT).key(t + 1.5f, roar);
        Clips.Seq laser = Clips.seq(64).key(8, roar).key(22, roar.with(Clips.P().r("head", -0.5f, 0, 0).r("torso", -0.4f, 0, 0))).key(26, aimH, Ease.OUT);
        for (int t = 30; t <= 56; t += 4) laser.key(t, aimH.with(Clips.P().p("body", 0, 2, 1))).key(t + 2, aimH);
        return Map.of(
            HAYMAKER, Clips.seq(30).key(6, cockR).key(10, punchR, Ease.OUT).key(14, punchR).key(17, cockL).key(20, punchL, Ease.OUT).key(25, punchL).build(),
            SLAM, Clips.seq(36).key(6, Clips.crouch()).key(10, leap, Ease.OUT).key(17, leap).key(20, slam, Ease.IN).key(28, slam).build(),
            BARRAGE, barrage.build(),
            MEGA_LASER, laser.build());
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, EmilHackedClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<EmilHackedUnit> t) {
        AllyClient.renderer(e, t, ID, 1.0f, u -> AllyClient.tex(ID), r -> new AllyClient.Model<>(r, clips()));
    }
}
