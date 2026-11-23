package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.OperatorUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.Map;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Operators: original design - long veil-hood, ankle-length dress, headset; one mesh, two textures (6O light / 21O dark). */
public final class OperatorClient {
    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, true, 0);
        p.head().addOrReplaceChild("veil", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.6f, -4.5f, 9, 9, 9), PartPose.ZERO);
        p.head().addOrReplaceChild("headset", CubeListBuilder.create().texOffs(48, 0).addBox(-5, -6, -1, 1, 3, 2).texOffs(48, 0).addBox(4, -6, -1, 1, 3, 2)
                .texOffs(54, 0).addBox(-4.8f, -3.5f, -4, 1, 1, 3), PartPose.ZERO);
        p.torso().addOrReplaceChild("dress", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, 0, -3, 9, 11, 6), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose type = Clips.P().r("rightArm", -0.9f, -0.4f, 0).r("leftArm", -0.9f, 0.4f, 0).r("head", 0.25f, 0, 0).p("pod", 0, 2, -4);
        Pose pulse = Clips.castRaise().with(Clips.P().p("pod", 0, -6, -2));
        return Map.of(
            POD_GUN, Clips.podShoot(30, 8, 26, 3),
            REPAIR, Clips.seq(36).key(6, type).key(16, type.with(Clips.P().p("body", 0, 0.5f, 0))).key(20, pulse, Ease.OUT).key(28, pulse).build(),
            SHIELD, Clips.seq(30).key(6, type).key(12, Clips.P().r("rightArm", -1.8f, -0.3f, 0.6f).r("leftArm", -1.8f, 0.3f, -0.6f).p("pod", 0, -2, -6))
                    .key(16, Clips.castRelease().with(Clips.P().p("pod", 0, -6, -3)), Ease.OUT).key(24, Clips.castRelease()).build(),
            EVADE, Clips.evade(10));
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e, String id) { AllyClient.layers(e, id, OperatorClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<OperatorUnit> t, String id) {
        AllyClient.renderer(e, t, id, 0.5f, u -> AllyClient.tex(id), r -> new AllyClient.Model<>(r, clips()));
    }
}
