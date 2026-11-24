package com.nier.automata.unit.ally;

import com.nier.automata.client.NierModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Floating tome with a face plate; page flaps open like the Pod's fins. */
public final class GrimoireClient {
    static final String ID = "ally_grimoire";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition body = m.getRoot().addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5, -7, -1.5f, 10, 14, 3), PartPose.offset(0, 16, 0));
        body.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(0, 24).addBox(-1, -1, -2, 2, 2, 2), PartPose.offset(0, 6, -1.5f));
        body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(16, 24).addBox(-1, -6, -1.5f, 1, 12, 3), PartPose.offset(-5, 0, 0));
        body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(16, 24).addBox(0, -6, -1.5f, 1, 12, 3), PartPose.offset(5, 0, 0));
        body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(36, 24).addBox(-3, -4, -2.4f, 6, 6, 1), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static class Model extends NierModel<GrimoireUnit> {
        Model(ModelPart r) {
            super(r);
            bind("body", "body/muzzle", "body/rightFin", "body/leftFin");
            PodClient.clips().forEach(this::clip);
        }
        @Override protected void idle(GrimoireUnit e, float ls, float la, float age, float yaw, float pitch) {
            bone("body").y += Mth.sin(age * 0.12f) * 1.0f;
            bone("body").xRot += pitch * Mth.DEG_TO_RAD * 0.5f + la * 0.3f;
            bone("body").yRot += yaw * Mth.DEG_TO_RAD * 0.5f;
            bone("rightFin").zRot += 0.25f + Mth.sin(age * 0.2f) * 0.12f;
            bone("leftFin").zRot -= 0.25f + Mth.sin(age * 0.2f) * 0.12f;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, GrimoireClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<GrimoireUnit> t) {
        AllyClient.renderer(e, t, ID, 0.3f, u -> AllyClient.tex(ID + (u.getForm() == 1 ? "_noir" : u.getForm() == 2 ? "_rubrum" : "_weiss")), Model::new);
    }
}
