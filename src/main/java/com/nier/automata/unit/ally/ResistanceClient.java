package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.ResistanceUnit.*;

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

/** Resistance soldier: original design - olive field jacket, scarf, bandana, patched trousers; spear or rifle. */
public final class ResistanceClient {
    static final String ID = "ally_resistance_soldier";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, -1.3f);
        p.head().addOrReplaceChild("bandana", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.5f, -4.5f, 9, 3, 9)
                .texOffs(48, 0).addBox(-1, -7, 4.5f, 2, 5, 1), PartPose.ZERO);
        p.torso().addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, -12.3f, -2.5f, 9, 13, 5), PartPose.ZERO);
        p.torso().addOrReplaceChild("scarf", CubeListBuilder.create().texOffs(28, 64).addBox(-4.5f, -12.6f, -2.8f, 9, 2, 5.6f), PartPose.ZERO);
        p.torso().addOrReplaceChild("pouch", CubeListBuilder.create().texOffs(84, 16).addBox(-4.8f, -2, -3, 3, 3, 2), PartPose.ZERO);
        p.weapon().addOrReplaceChild("spear", AllyModel.spear(), PartPose.ZERO);
        p.weapon().addOrReplaceChild("gun", AllyModel.rifle(), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        // underhand grenade lob: reach back with the left hand, step, release at t=14
        Pose reach = Clips.P().r("leftArm", 0.9f, 0, -0.4f).r("torso", -0.1f, -0.5f, 0).r("head", 0, 0.4f, 0).p("body", 0, 1.2f, 1.5f)
                .r("rightArm", -0.6f, 0.3f, 0.2f).r("rightLeg", 0.4f, 0, 0).r("leftLeg", -0.3f, 0, 0);
        Pose release = Clips.P().r("leftArm", -2.2f, 0.2f, -0.2f).r("torso", 0.2f, 0.4f, 0).r("head", -0.2f, -0.2f, 0).p("body", 0, 0.8f, -2)
                .r("rightArm", 0.3f, 0, 0.3f).r("rightLeg", -0.5f, 0, 0).r("leftLeg", 0.4f, 0, 0);
        return Map.of(
            THRUST, Clips.thrust(20, 12),
            SWEEP, Clips.seq(22).key(7, Clips.slashWind(-1).with(Clips.P().r("weapon", 0, 0, 0))).key(12, Clips.slashHit(-1).with(Clips.P().r("weapon", 0, 0, 0)), Ease.OUT)
                    .key(16, Clips.slashFollow(-1).with(Clips.P().r("weapon", 0, 0, 0))).build(),
            BURST, Clips.shoot(32, 8, 26, 3),
            GRENADE, Clips.seq(24).key(8, reach).key(14, release, Ease.OUT).key(18, release).build());
    }

    static class Model extends AllyClient.Model<ResistanceUnit> {
        Model(ModelPart r) { super(r, clips(), "body/torso/rightArm/weapon/spear", "body/torso/rightArm/weapon/gun"); }
        @Override protected void idle(ResistanceUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            b("spear").visible = e.getForm() == 0; b("gun").visible = e.getForm() == 1;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, ResistanceClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<ResistanceUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), Model::new);
    }
}
