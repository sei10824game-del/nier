package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.TwinUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.unit.ally.Clips.Pose;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Twin casters: original design - long hooded mantles over flowing robes, a floating rune ring; textures differ per twin. */
public final class TwinClient {
    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, 0);
        p.head().addOrReplaceChild("hood", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.6f, -4.5f, 9, 8, 9)
                .texOffs(48, 0).addBox(-4.5f, -1, 2, 9, 6, 3), PartPose.ZERO);
        p.torso().addOrReplaceChild("robe", CubeListBuilder.create().texOffs(0, 64).addBox(-4.6f, -1, -3, 9.2f, 13, 6), PartPose.ZERO);
        p.torso().addOrReplaceChild("mantle", CubeListBuilder.create().texOffs(32, 64).addBox(-5, -12.5f, -2.8f, 10, 6, 5.6f), PartPose.ZERO);
        p.torso().addOrReplaceChild("rune", CubeListBuilder.create().texOffs(0, 84).addBox(-6, 0, -6, 12, 0, 12), PartPose.offset(0, -19, 0));
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        Pose song = Clips.P().r("rightArm", -1.1f, -0.7f, 0).r("leftArm", -1.1f, 0.7f, 0).r("head", -0.3f, 0, 0.15f).r("torso", -0.1f, 0, 0.05f);
        Clips.Seq s = Clips.seq(50).key(6, song);
        Pose sway = song.with(Clips.P().r("head", -0.35f, 0, -0.15f).r("torso", -0.1f, 0, -0.06f).p("body", 0, 0.6f, 0));
        s.key(10, sway).key(14, song).key(18, sway).key(22, song);
        s.key(30, Clips.castRaise(), Ease.OUT).key(34, Clips.castRaise()).key(40, song).key(44, sway);
        // Maledict: conjure (hands sweep up drawing the halo), then point-and-release volley
        Pose point = Clips.P().r("rightArm", -1.7f, -0.1f, 0).r("leftArm", -0.4f, 0, -0.6f).r("torso", 0.05f, -0.3f, 0).r("head", 0, 0.3f, 0);
        Clips.Seq mal = Clips.seq(44).key(6, Clips.castRaise().with(Clips.P().r("rightArm", -2.0f, 0.8f, 0.9f).r("leftArm", -2.0f, -0.8f, -0.9f)))
                .key(18, Clips.castRaise()).key(22, point, Ease.OUT);
        for (int t = 24; t <= 36; t += 4) mal.key(t, point.with(Clips.P().r("rightArm", -1.85f, -0.1f, 0)), Ease.OUT).key(t + 2, point);
        Pose lanceDraw = Clips.P().r("rightArm", -1.2f, 0.9f, 0.6f).r("leftArm", -1.4f, -0.2f, 0).r("torso", 0, 0.7f, 0).r("head", 0, -0.6f, 0).p("body", 0, 1.5f, 1.5f);
        Pose lanceCast = Clips.P().r("rightArm", -1.6f, -0.3f, 0).r("leftArm", -0.2f, 0, -0.8f).r("torso", 0.2f, -0.5f, 0).r("head", 0, 0.4f, 0).p("body", 0, 1, -2);
        Pose blink = Clips.P().r("torso", 0.4f, 0, 0).r("rightArm", -0.8f, 0, 1.2f).r("leftArm", -0.8f, 0, -1.2f).p("body", 0, 2, 0);
        Map<Integer, AnimClip> c = new HashMap<>();
        c.put(BOLTS, Clips.cast(26, 14));
        c.put(MALEDICT, mal.build());
        c.put(ORBS, Clips.seq(30).key(8, Clips.castRaise()).key(12, Clips.castRelease(), Ease.OUT).key(14, Clips.castRaise())
                .key(16, Clips.castRelease(), Ease.OUT).key(18, Clips.castRaise()).key(20, Clips.castRelease(), Ease.OUT).key(25, Clips.castRelease()).build());
        c.put(BLINK, Clips.seq(12).key(3, blink).key(5, blink.with(Clips.P().p("body", 0, -2, 0)), Ease.OUT).build());
        c.put(SONG, s.build());
        c.put(LANCE, Clips.seq(30).key(10, lanceDraw).key(16, lanceDraw.with(Clips.P().r("torso", 0, 0.85f, 0))).key(18, lanceCast, Ease.OUT).key(24, lanceCast).build());
        c.put(BARRIER, Clips.seq(30).key(8, Clips.P().r("rightArm", -1.4f, -0.6f, 0).r("leftArm", -1.4f, 0.6f, 0).r("head", 0.3f, 0, 0))
                .key(16, Clips.castRaise().with(Clips.P().r("rightArm", -2.0f, 0, 1.2f).r("leftArm", -2.0f, 0, -1.2f)), Ease.OUT).key(24, Clips.castRaise()).build());
        return c;
    }

    static class Model extends AllyClient.Model<TwinUnit> {
        Model(net.minecraft.client.model.geom.ModelPart r) { super(r, clips(), "body/torso/rune"); }
        @Override protected void idle(TwinUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            b("rune").yRot = age * 0.05f; b("rune").visible = e.isCasting();
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e, String id) { AllyClient.layers(e, id, TwinClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<TwinUnit> t, String id) {
        AllyClient.renderer(e, t, id, 0.5f, u -> AllyClient.tex(id), Model::new);
    }
}
