package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.PodUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Support Pod: original design - boxy hovering drone with two folding side fins and a forward muzzle; two paint schemes. */
public final class PodClient {
    static final String ID = "ally_pod";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition body = m.getRoot().addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -5, -6, 8, 9, 12)
                .texOffs(40, 0).addBox(-3, -6, -4, 6, 1, 8), PartPose.offset(0, 16, 0));
        body.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(0, 24).addBox(-1, -1, -3, 2, 2, 3), PartPose.offset(0, 1, -6));
        body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(16, 24).addBox(-1, -3, -4, 1, 7, 8), PartPose.offset(-4.5f, 0, 0));
        body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(16, 24).addBox(0, -3, -4, 1, 7, 8), PartPose.offset(4.5f, 0, 0));
        body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(36, 24).addBox(-2, -3, -6.3f, 4, 1, 1), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose aim = Clips.P().r("body", 0.1f, 0, 0).r("rightFin", 0, 0, 0.5f).r("leftFin", 0, 0, -0.5f);
        Pose kick = aim.with(Clips.P().r("body", -0.05f, 0, 0).p("body", 0, 0, 1).p("muzzle", 0, 0, 1));
        Clips.Seq gun = Clips.seq(30).key(3, aim);
        for (int t = 4; t <= 26; t += 2) gun.key(t, kick, Ease.OUT).key(t + 1, aim);
        Pose open = Clips.P().r("rightFin", 0, 0, 1.4f).r("leftFin", 0, 0, -1.4f).r("body", 0.15f, 0, 0).p("muzzle", 0, 0, -1);
        Clips.Seq laser = Clips.seq(50).key(5, aim).key(18, open.with(Clips.P().p("body", 0, 0, 2))).key(20, open, Ease.OUT);
        for (int t = 22; t <= 44; t += 4) laser.key(t, open.with(Clips.P().p("body", 0, 0, 0.7f))).key(t + 2, open);
        Pose up = Clips.P().r("body", -0.9f, 0, 0).r("rightFin", 0, 0, 0.9f).r("leftFin", 0, 0, -0.9f);
        Clips.Seq missile = Clips.seq(34).key(6, up);
        for (int t = 10; t <= 25; t += 3) missile.key(t, up.with(Clips.P().p("body", 0, 1, 0)), Ease.OUT).key(t + 1.5f, up);
        // hammer: fins fold into a head, the pod flips nose-down and spins into the slam
        Pose fold = Clips.P().r("rightFin", 0, 0, -0.3f).r("leftFin", 0, 0, 0.3f).r("body", 0, 0, 0).scale("body", 1.4f, 1.4f, 1.4f);
        Pose dive = fold.with(Clips.P().r("body", 1.57f, 0, 0));
        Pose impact = fold.with(Clips.P().r("body", 1.57f, 0, 0).scale("body", 1.7f, 1.2f, 1.7f));
        Pose heal = Clips.P().r("rightFin", 0, 0, 1.0f).r("leftFin", 0, 0, -1.0f).r("body", -0.3f, 0, 0);
        return Map.of(
            GUN, gun.build(),
            LASER, laser.build(),
            MISSILE, missile.key(28, up).build(),
            HAMMER, Clips.seq(36).key(8, fold).key(14, fold.with(Clips.P().r("body", -0.6f, 0, 0))).key(16, dive, Ease.IN).key(20, impact, Ease.OUT).key(28, fold).build(),
            REPAIR, Clips.seq(30).key(6, heal).key(12, heal.with(Clips.P().r("body", -0.3f, 1.6f, 0))).key(18, heal.with(Clips.P().r("body", -0.3f, 3.14f, 0)), Ease.OUT).key(24, heal).build());
    }

    static class Model extends NierModel<PodUnit> {
        Model(ModelPart r) {
            super(r);
            bind("body", "body/muzzle", "body/rightFin", "body/leftFin");
            clips().forEach(this::clip);
        }
        @Override protected void idle(PodUnit e, float ls, float la, float age, float yaw, float pitch) {
            bone("body").y += Mth.sin(age * 0.12f) * 1.0f;
            bone("body").xRot += pitch * Mth.DEG_TO_RAD * 0.5f + la * 0.3f;
            bone("body").yRot += yaw * Mth.DEG_TO_RAD * 0.5f;
            bone("rightFin").zRot += Mth.sin(age * 0.2f) * 0.08f;
            bone("leftFin").zRot -= Mth.sin(age * 0.2f) * 0.08f;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, PodClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<PodUnit> t) {
        AllyClient.renderer(e, t, ID, 0.3f, u -> AllyClient.tex(u.getForm() == 1 ? ID + "_153" : ID), Model::new);
    }
}
