package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.YorhaSoldierUnit.*;

import com.nier.automata.client.AnimClip;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client side of the YoRHa soldier: visored trooper in a black field coat, three loadouts (original design). */
public final class YorhaSoldierClient {
    static final String ID = "ally_yorha_soldier";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, true, -1.3f);
        p.head().addOrReplaceChild("hair", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
                .texOffs(48, 0).addBox(-4.5f, -4.5f, 2.5f, 9, 3, 2), PartPose.ZERO);
        p.head().addOrReplaceChild("visor", CubeListBuilder.create().texOffs(84, 16).addBox(-4.3f, -5.5f, -4.4f, 8.6f, 2, 1), PartPose.ZERO);
        p.torso().addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, -1, -2.5f, 9, 7, 5), PartPose.ZERO);
        p.torso().addOrReplaceChild("collar", CubeListBuilder.create().texOffs(28, 64).addBox(-4.5f, -12.5f, -2.5f, 9, 3, 5), PartPose.ZERO);
        p.weapon().addOrReplaceChild("sword", AllyModel.sword(14), PartPose.ZERO);
        p.weapon().addOrReplaceChild("spear", AllyModel.spear(), PartPose.ZERO);
        p.weapon().addOrReplaceChild("gun", AllyModel.rifle(), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        return Map.of(
            COMBO, Clips.combo(28, true, 6, 12, 18, 23),
            DASH, Clips.dashStrike(18, 12),
            FLURRY, Clips.flurry(28, 8, 20, 4),
            CHARGE, Clips.thrust(22, 14),
            BURST, Clips.shoot(30, 8, 24, 4),
            BASH, Clips.seq(16).key(5, Clips.thrustWind()).key(9, Clips.thrustHit(), AnimClip.Ease.OUT).key(12, Clips.thrustHit()).build(),
            POD_GUN, Clips.podShoot(30, 6, 26, 2),
            EVADE, Clips.evade(10));
    }

    static class Model extends AllyClient.Model<YorhaSoldierUnit> {
        Model(ModelPart r) { super(r, clips(), "body/torso/rightArm/weapon/sword", "body/torso/rightArm/weapon/spear", "body/torso/rightArm/weapon/gun"); }
        @Override protected void idle(YorhaSoldierUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            b("sword").visible = e.getForm() == 0; b("spear").visible = e.getForm() == 1; b("gun").visible = e.getForm() == 2;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, YorhaSoldierClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<YorhaSoldierUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), Model::new);
    }
}
