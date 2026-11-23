package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.CommanderUnit.*;

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

/** Commander: original design - white officer's long coat with gold trim, peaked cap, sabre. */
public final class CommanderClient {
    static final String ID = "ally_commander";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, -1.3f);
        p.head().addOrReplaceChild("cap", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -9.5f, -4.5f, 9, 2, 9)
                .texOffs(48, 0).addBox(-4, -7.5f, -5.5f, 8, 1, 2), PartPose.ZERO);
        p.head().addOrReplaceChild("hair", CubeListBuilder.create().texOffs(84, 16).addBox(-4.3f, -7.5f, 2.6f, 8.6f, 9, 2), PartPose.ZERO);
        p.torso().addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 64).addBox(-4.6f, -1, -2.6f, 9.2f, 11, 5.2f), PartPose.ZERO);
        p.torso().addOrReplaceChild("epaulets", CubeListBuilder.create().texOffs(32, 64).addBox(-6.5f, -12.5f, -2, 3, 1, 4).texOffs(32, 64).addBox(3.5f, -12.5f, -2, 3, 1, 4), PartPose.ZERO);
        p.weapon().addOrReplaceChild("sabre", AllyModel.sword(14), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose salute = Clips.P().r("rightArm", -2.4f, -0.6f, 0.2f).r("weapon", 1.2f, 0, 0).r("leftArm", 0.1f, 0, -0.1f).r("head", -0.15f, 0, 0).r("torso", -0.1f, 0, 0);
        Pose raise = Clips.P().r("rightArm", -3.0f, 0.1f, 0.15f).r("weapon", 1.2f, 0, 0).r("leftArm", 0.3f, 0, -0.5f).r("head", -0.45f, 0, 0).r("torso", -0.2f, 0, 0).p("body", 0, -0.5f, 0);
        return Map.of(
            COMBO, Clips.combo(26, false, 6, 12, 19),
            LUNGE, Clips.thrust(20, 13),
            RIPOSTE, Clips.seq(18).key(3, Clips.backstep(), Ease.OUT).key(7, Clips.guard()).key(9, Clips.thrustWind()).key(11, Clips.thrustHit(), Ease.OUT).key(14, Clips.thrustHit()).build(),
            RALLY, Clips.seq(30).key(6, salute).key(12, salute.with(Clips.P().p("body", 0, 0.5f, 0))).key(14, raise, Ease.OUT).key(24, raise).build());
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, CommanderClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<CommanderUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), r -> new AllyClient.Model<>(r, clips()));
    }
}
