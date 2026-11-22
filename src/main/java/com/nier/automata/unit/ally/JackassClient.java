package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.JackassUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Demolitions researcher: original design - welding goggles, tool vest with charges, tube launcher on the right shoulder. */
public final class JackassClient {
    static final String ID = "ally_jackass";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, -1.3f);
        p.head().addOrReplaceChild("hair", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.5f, -4.5f, 9, 5, 9), PartPose.ZERO);
        p.head().addOrReplaceChild("goggles", CubeListBuilder.create().texOffs(84, 16).addBox(-4.4f, -8, -4.6f, 8.8f, 2, 1), PartPose.ZERO);
        p.torso().addOrReplaceChild("vest", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, -12, -2.6f, 9, 9, 5.2f), PartPose.ZERO);
        p.torso().addOrReplaceChild("launcher", CubeListBuilder.create().texOffs(0, 80).addBox(-1.5f, -1.5f, -9, 3, 3, 16), PartPose.offset(-5, -14, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose windUp = Clips.P().r("rightArm", -2.8f, 0.3f, 0.3f).r("torso", -0.25f, 0.5f, 0).r("head", -0.1f, -0.4f, 0).p("body", 0, 1.2f, 1.5f)
                .r("leftArm", -0.8f, -0.3f, -0.3f).r("rightLeg", 0.4f, 0, 0).r("leftLeg", -0.4f, 0, 0);
        Pose throwP = Clips.P().r("rightArm", -1.2f, -0.4f, 0.1f).r("torso", 0.35f, -0.5f, 0).r("head", 0.1f, 0.3f, 0).p("body", 0, 1.2f, -2.5f)
                .r("leftArm", 0.5f, 0, -0.3f).r("rightLeg", -0.6f, 0, 0).r("leftLeg", 0.5f, 0, 0);
        // shoulder the tube (launcher pivots down to level), brace, fire with big recoil kick at t=22
        Pose shoulder = Clips.P().r("launcher", 0.15f, 0, 0).r("rightArm", -2.6f, 0.2f, 0).r("leftArm", -1.6f, 0.6f, 0).r("head", 0, 0.2f, 0.1f)
                .r("torso", 0.05f, 0.1f, 0).p("body", 0, 2, 0).r("rightLeg", 0.5f, 0, 0.1f).r("leftLeg", -0.6f, 0, -0.1f);
        Pose recoil = shoulder.with(Clips.P().r("launcher", -0.25f, 0, 0).p("launcher", 0, -1, 3).r("torso", -0.3f, 0.1f, 0).p("body", 0, 1.5f, 3).r("head", -0.2f, 0.2f, 0.1f));
        Pose plant = Clips.crouch().with(Clips.P().r("rightArm", -0.9f, 0, 0.1f).r("leftArm", -0.9f, 0, -0.1f).r("head", 0.5f, 0, 0).p("body", 0, 5, -1));
        Pose kickWind = Clips.P().r("rightLeg", -0.6f, 0, 0).p("rightLeg", 0, -2, 1).r("torso", -0.2f, 0, 0).r("rightArm", 0.4f, 0, 0.5f).r("leftArm", -0.6f, 0, -0.5f).p("body", 0, -0.5f, 1);
        Pose kickHit = Clips.P().r("rightLeg", -1.7f, 0, 0).r("torso", -0.45f, 0, 0).r("head", 0.2f, 0, 0).r("rightArm", 0.8f, 0, 0.6f).r("leftArm", 0.4f, 0, -0.7f)
                .p("body", 0, -0.5f, -1).r("leftLeg", 0.2f, 0, 0);
        return Map.of(
            CLUSTER, Clips.seq(30).key(8, windUp).key(13, windUp.with(Clips.P().r("rightArm", -3.0f, 0.4f, 0.3f))).key(16, throwP, Ease.OUT).key(22, throwP).build(),
            ROCKET, Clips.seq(36).key(8, shoulder).key(20, shoulder.with(Clips.P().p("body", 0, 2.4f, 0))).key(22, recoil, Ease.OUT).key(27, shoulder).build(),
            CHARGE, Clips.seq(44).key(6, plant).key(10, plant.with(Clips.P().r("rightArm", -0.5f, 0, 0.2f))).key(14, Clips.backstep(), Ease.OUT)
                    .key(22, Clips.guard()).key(34, Clips.guard().with(Clips.P().r("head", 0.1f, 0.3f, 0).r("leftArm", -1.4f, 0, -0.3f))).key(38, Clips.crouch(), Ease.OUT).build(),
            KICK, Clips.seq(16).key(5, kickWind).key(8, kickHit, Ease.OUT).key(11, kickHit).build());
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, JackassClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<JackassUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), (ModelPart r) -> new AllyClient.Model<>(r, clips(), "body/torso/launcher"));
    }
}
