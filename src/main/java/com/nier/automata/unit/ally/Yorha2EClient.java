package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.Yorha2EUnit.*;

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

/** 2E: original design - hooded executioner cowl with a red visor slit, short dress-coat, light blade + great sword on the back. */
public final class Yorha2EClient {
    static final String ID = "ally_yorha_2e";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, true, -1.3f);
        p.head().addOrReplaceChild("hood", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.6f, -4.5f, 9, 5, 9)
                .texOffs(48, 0).addBox(-4.5f, -3.6f, 1.5f, 9, 4, 3), PartPose.ZERO);
        p.head().addOrReplaceChild("visor", CubeListBuilder.create().texOffs(84, 16).addBox(-4.3f, -5.6f, -4.5f, 8.6f, 1.5f, 1), PartPose.ZERO);
        p.torso().addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5f, -1, -2.5f, 9, 6, 5), PartPose.ZERO);
        p.torso().addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 64).addBox(-4, 0, 0, 8, 9, 1), PartPose.offsetAndRotation(0, 0, 2.5f, 0.15f, 0, 0));
        p.torso().addOrReplaceChild("backSword", AllyModel.greatSword(20), PartPose.offsetAndRotation(2, -11, 3, 0, 0, -0.5f));
        p.weapon().addOrReplaceChild("light", AllyModel.sword(13), PartPose.ZERO);
        p.weapon().addOrReplaceChild("heavy", AllyModel.greatSword(20), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    static Map<Integer, AnimClip> clips() {
        // heavy cleave: big wind-up behind the head, horizontal cut, re-chamber, then a crushing overhead
        Pose heavyWind = Clips.slashWind(1).with(Clips.P().r("leftArm", -1.9f, 0.9f, -0.3f).p("body", 0, 2.2f, 1.6f));
        Pose heavyHit = Clips.slashHit(1).with(Clips.P().r("leftArm", -1.4f, -1.0f, 0.2f));
        AnimClip heavy = Clips.seq(34).key(5, heavyWind).key(10, heavyWind.with(Clips.P().r("torso", 0.1f, 0.95f, 0.05f)))
                .key(12, heavyHit, Ease.OUT).key(15, Clips.slashFollow(1)).key(20, Clips.overUp()).key(24, Clips.overUp().with(Clips.P().p("body", 0, -3.5f, 0.5f)))
                .key(26, Clips.overDown(), Ease.IN).key(31, Clips.overDown()).build();
        // counter: back-step then dash-cut
        AnimClip counter = Clips.seq(18).key(3, Clips.backstep(), Ease.OUT).key(7, Clips.dashLow()).key(10, Clips.dashLow().with(Clips.P().p("body", 0, 2.5f, -6)))
                .key(12, Clips.slashHit(-1), Ease.OUT).key(15, Clips.slashFollow(-1)).build();
        // laser: hand raised commanding the pod, charge glow (pod pulled back), sustained beam with tremor
        Pose cmd = Clips.podAim().with(Clips.P().p("pod", 2, 3, -3));
        Clips.Seq laser = Clips.seq(50).key(4, cmd).key(18, cmd.with(Clips.P().p("pod", 2, 2.5f, -2).r("pod", 0.2f, 0, 0)));
        for (int t = 20; t <= 44; t += 4) laser.key(t, Clips.podKick(), Ease.OUT).key(t + 2, Clips.podAim());
        // self-destruct: arms spread, kneel, convulse, blast
        Pose kneel = Clips.crouch().with(Clips.P().r("head", 0.6f, 0, 0));
        Pose spread = Clips.castRaise().with(Clips.P().r("head", -0.7f, 0, 0).p("body", 0, -1, 0));
        AnimClip boom = Clips.seq(40).key(6, kneel).key(20, kneel.with(Clips.P().r("torso", 0.7f, 0.1f, 0.05f))).key(24, kneel.with(Clips.P().r("torso", 0.6f, -0.1f, -0.05f)))
                .key(34, spread, Ease.IN).key(36, spread.with(Clips.P().p("body", 0, -3, 0)), Ease.OUT).key(39, spread).build();
        return Map.of(
            LIGHT, Clips.combo(30, true, 5, 10, 15, 22),
            HEAVY, heavy,
            LAUNCH, Clips.launcher(20, 12),
            SPIN, Clips.spin(30, 8, 24, 2),
            POD_GUN, Clips.podShoot(30, 6, 26, 2),
            LASER, laser.build(),
            COUNTER, counter,
            SELF_DESTRUCT, boom);
    }

    static class Model extends AllyClient.Model<Yorha2EUnit> {
        Model(ModelPart r) { super(r, clips(), "body/torso/rightArm/weapon/light", "body/torso/rightArm/weapon/heavy", "body/torso/backSword"); }
        @Override protected void idle(Yorha2EUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            boolean heavy = e.getSkillId() == HEAVY || e.getSkillId() == SPIN;
            b("heavy").visible = heavy; b("backSword").visible = !heavy; b("light").visible = !heavy;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, Yorha2EClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<Yorha2EUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), Model::new);
    }
}
