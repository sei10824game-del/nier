package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.EmilUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.HashMap;
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

/**
 * Emil: original design - an oversized pale round head with two glowing slit eyes, small cloaked body seated on a
 * two-wheeled merchant cart. Form 1 drops the cart, hovers and circles a ring of small orbiting heads.
 */
public final class EmilClient {
    static final String ID = "ally_emil";

    /** Shared by Emil (cart) and Emil Hacked builds. */
    static PartDefinition emilHead(PartDefinition torso, float y) {
        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6, -12, -6, 12, 12, 12), PartPose.offset(0, y, 0));
        head.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(48, 0).addBox(-4.5f, -7, -6.4f, 3, 1, 1).texOffs(48, 0).addBox(1.5f, -7, -6.4f, 3, 1, 1), PartPose.ZERO);
        return head;
    }

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition body = m.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 14, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 24).addBox(-3.5f, -7, -2.5f, 7, 7, 5)
                .texOffs(24, 24).addBox(-4.5f, -7.5f, -3, 9, 4, 6), PartPose.ZERO);
        emilHead(torso, -7);
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 40).addBox(-2, -1, -1, 2, 7, 2), PartPose.offset(-3.5f, -6, 0));
        ra.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.ZERO);
        torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(8, 40).addBox(0, -1, -1, 2, 7, 2), PartPose.offset(3.5f, -6, 0));
        // seated legs pointing forward
        body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(16, 40).addBox(-1, 0, -1, 2, 6, 2), PartPose.offsetAndRotation(-1.5f, 0, 0, -1.4f, 0, 0));
        body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(16, 40).addBox(-1, 0, -1, 2, 6, 2), PartPose.offsetAndRotation(1.5f, 0, 0, -1.4f, 0, 0));
        body.addOrReplaceChild("pod", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition cart = m.getRoot().addOrReplaceChild("cart", CubeListBuilder.create().texOffs(0, 64).addBox(-5, -3, -7, 10, 3, 14)
                .texOffs(48, 64).addBox(-5, -9, 5, 10, 6, 2).texOffs(72, 64).addBox(-6, -6, -8, 12, 1, 1), PartPose.offset(0, 20, 0));
        cart.addOrReplaceChild("wheels", CubeListBuilder.create().texOffs(0, 84).addBox(-7, -2, -2, 1, 4, 4).texOffs(0, 84).addBox(6, -2, -2, 1, 4, 4)
                .texOffs(12, 84).addBox(-6, -0.5f, -0.5f, 12, 1, 1), PartPose.offset(0, 2, 0));
        PartDefinition ring = m.getRoot().addOrReplaceChild("ring", CubeListBuilder.create(), PartPose.offset(0, 8, 0));
        for (int k = 0; k < 6; k++) {
            double a = k * Math.PI / 3;
            ring.addOrReplaceChild("orb" + k, CubeListBuilder.create().texOffs(64, 0).addBox(-2, -2, -2, 4, 4, 4),
                    PartPose.offset((float) Math.cos(a) * 13, (k % 2) * 3f, (float) Math.sin(a) * 13));
        }
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose brace = Clips.P().r("rightArm", -1.4f, 0, 0.5f).r("leftArm", -1.4f, 0, -0.5f).r("head", -0.15f, 0, 0).r("torso", -0.1f, 0, 0);
        Pose open = Clips.P().r("rightArm", -2.4f, 0, 1.2f).r("leftArm", -2.4f, 0, -1.2f).r("head", -0.3f, 0, 0).r("torso", -0.25f, 0, 0);
        Pose stare = Clips.P().r("head", 0.15f, 0, 0).r("torso", 0.2f, 0, 0).r("rightArm", -0.4f, 0, 0.3f).r("leftArm", -0.4f, 0, -0.3f).p("body", 0, 0, -1);
        Pose stareCharge = stare.with(Clips.P().r("head", -0.15f, 0, 0).p("body", 0, 1, 1).r("torso", -0.1f, 0, 0));
        Pose wheelie = Clips.P().r("cart", -0.6f, 0, 0).p("cart", 0, -2, 2).p("body", 0, -2, 3).r("torso", -0.5f, 0, 0).r("rightArm", -1.8f, 0, 0.2f).r("leftArm", -1.8f, 0, -0.2f).r("head", -0.2f, 0, 0);
        Pose ram = Clips.P().r("cart", 0.1f, 0, 0).p("body", 0, 0, -1).r("torso", 0.4f, 0, 0).r("head", 0.3f, 0, 0).r("rightArm", -0.6f, 0, 0.6f).r("leftArm", -0.6f, 0, -0.6f);
        Pose stoppie = Clips.P().r("cart", 0.5f, 0, 0).p("cart", 0, -2, -2).p("body", 0, -1, -2).r("torso", 0.4f, 0, 0).r("rightArm", -1.5f, 0, 1.3f).r("leftArm", -1.5f, 0, -1.3f);

        Map<Integer, AnimClip> c = new HashMap<>();
        Clips.Seq fire = Clips.seq(40).key(6, brace);
        for (int t = 10; t <= 34; t += 4) fire.key(t, open.with(Clips.P().r("head", -0.3f, t * 0.45f % 1f - 0.5f, 0)), Ease.OUT).key(t + 2, open);
        c.put(FIRE, fire.build());
        Clips.Seq laser = Clips.seq(60).key(6, stareCharge).key(20, stareCharge.with(Clips.P().p("body", 0, 1.5f, 1.5f)));
        for (int t = 22; t <= 52; t += 4) laser.key(t, stare.with(Clips.P().p("body", 0, 0, -1.4f)), Ease.OUT).key(t + 2, stare);
        c.put(LASER, laser.build());
        c.put(LASER720, Clips.seq(70).key(8, stareCharge).key(18, stareCharge).key(20, stare.with(Clips.P().r("rightArm", -1.5f, 0, 1.4f).r("leftArm", -1.5f, 0, -1.4f)), Ease.OUT)
                .key(60, stare.with(Clips.P().r("rightArm", -1.5f, 0, 1.4f).r("leftArm", -1.5f, 0, -1.4f))).build());
        c.put(WHEELIE, Clips.seq(30).key(8, wheelie).key(11, wheelie.with(Clips.P().r("cart", -0.75f, 0, 0))).key(14, ram, Ease.OUT).key(22, ram).build());
        Clips.Seq stop = Clips.seq(40).key(6, stoppie);
        for (int i = 0; i <= 12; i++) stop.key(8 + i * 2, stoppie.with(Clips.P().r("cart", i % 2 == 0 ? 0.55f : 0.4f, 0, i % 2 == 0 ? 0.1f : -0.1f)), Ease.LINEAR);
        c.put(STOPPIE, stop.key(36, stoppie).build());
        c.put(JUDGEMENT, Clips.seq(100).key(10, brace).key(30, open.with(Clips.P().p("body", 0, -3, 0))).key(60, open.with(Clips.P().p("body", 0, -5, 0).r("head", -0.5f, 0, 0)))
                .key(82, open.with(Clips.P().p("body", 0, -6, 0).r("head", -0.6f, 0, 0))).key(85, Clips.P().r("rightArm", -1.0f, 0, 2.2f).r("leftArm", -1.0f, 0, -2.2f).p("body", 0, 2, 0).r("head", 0.3f, 0, 0), Ease.OUT)
                .key(92, brace).build());
        c.put(CARPET, Clips.seq(60).key(8, brace).key(18, open.with(Clips.P().p("body", 0, -6, 0))).key(50, open.with(Clips.P().p("body", 0, -7, 0))).build());
        c.put(SPREAD, Clips.seq(30).key(8, brace).key(12, open, Ease.OUT).key(16, brace).key(20, open, Ease.OUT).key(25, open).build());
        Clips.Seq eyes = Clips.seq(56).key(8, stareCharge).key(18, stareCharge);
        for (int t = 20; t <= 50; t += 6) eyes.key(t, stare.with(Clips.P().r("head", 0.15f, Mth.sin(t * 0.4f) * 0.25f, 0)));
        c.put(EYES, eyes.build());
        c.put(RISE, Clips.seq(40).key(8, brace).key(30, open.with(Clips.P().p("body", 0, -4, 0)), Ease.OUT).key(36, open).build());
        return c;
    }

    static class Model extends AllyClient.Model<EmilUnit> {
        Model(ModelPart r) { super(r, clips(), "cart", "cart/wheels", "ring"); }
        @Override protected void idle(EmilUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            b("rightLeg").xRot = -1.4f; b("leftLeg").xRot = -1.4f;   // stay seated
            boolean hover = e.hovering();
            b("cart").visible = !hover; b("ring").visible = hover;
            b("wheels").xRot = -ls * 1.5f;
            b("ring").yRot = age * 0.08f;
            if (hover) { b("body").y += -4 + Mth.sin(age * 0.1f) * 1.5f; b("ring").y += Mth.sin(age * 0.1f) * 1.5f; }
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, EmilClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<EmilUnit> t) {
        AllyClient.renderer(e, t, ID, 0.7f, u -> AllyClient.tex(ID), Model::new);
    }
}
