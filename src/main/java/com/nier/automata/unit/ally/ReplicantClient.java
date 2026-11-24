package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.ReplicantUnit.*;

import com.nier.automata.client.AnimClip;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client side of the Replicant humans: swords toggled by kit (0 sword, 1 greatsword, 2 twin swords, 3 none). */
public final class ReplicantClient {
    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, -1.3f);
        p.head().addOrReplaceChild("hair", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
                .texOffs(48, 0).addBox(-4.5f, -4.5f, 2.5f, 9, 3, 2), PartPose.ZERO);
        p.head().addOrReplaceChild("longhair", CubeListBuilder.create().texOffs(84, 16).addBox(-4.5f, -5, 3.5f, 9, 12, 1), PartPose.ZERO);
        p.torso().addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, -1, -2.5f, 9, 7, 5), PartPose.ZERO);
        p.weapon().addOrReplaceChild("sword", AllyModel.sword(14), PartPose.ZERO);
        p.weapon().addOrReplaceChild("great", AllyModel.greatSword(22), PartPose.ZERO);
        p.leftArm().addOrReplaceChild("sword2", AllyModel.sword(12), PartPose.offsetAndRotation(0.5f, 9, 0, 0.3f, 0, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        return Map.ofEntries(
            Map.entry(COMBO, Clips.combo(28, true, 6, 12, 18, 23)), Map.entry(DASH, Clips.dashStrike(18, 12)),
            Map.entry(BLAST, Clips.cast(36, 14)), Map.entry(BLAST_E, Clips.cast(36, 14)), Map.entry(LANCE, Clips.cast(44, 14)),
            Map.entry(EVADE, Clips.evade(10)), Map.entry(HEAVY, Clips.overhead(36, 18)), Map.entry(SPIN, Clips.spin(36, 10, 28, 2)),
            Map.entry(DUAL, Clips.combo(24, true, 4, 8, 12, 16, 20)), Map.entry(DANCE, Clips.spin(30, 8, 24, 3)),
            Map.entry(RING, Clips.cast(40, 16)), Map.entry(BEAM, Clips.cast(44, 18)));
    }

    static class Model extends AllyClient.Model<ReplicantUnit> {
        Model(ModelPart r) { super(r, clips(), "body/torso/rightArm/weapon/sword", "body/torso/rightArm/weapon/great", "body/torso/leftArm/sword2", "body/torso/head/longhair"); }
        @Override protected void idle(ReplicantUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            int k = e.getForm();
            b("sword").visible = k == 0 || k == 2; b("great").visible = k == 1; b("sword2").visible = k == 2; b("longhair").visible = k == 1 || k == 2;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e, String id) { AllyClient.layers(e, id, ReplicantClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, String id, EntityType<ReplicantUnit> t) {
        AllyClient.renderer(e, t, id, 0.5f, u -> AllyClient.tex(id), Model::new);
    }
}
