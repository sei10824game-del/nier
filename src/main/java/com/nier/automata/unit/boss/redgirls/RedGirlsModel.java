package com.nier.automata.unit.boss.redgirls;

import static com.nier.automata.unit.boss.redgirls.RedGirlsUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.AnimClip.Ease;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.boss.BossClient;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Original box design: two small faceless crimson constructs with flared hems and floating glyph halos; form 1 darkens to a black-red network texture. */
public class RedGirlsModel extends NierModel<RedGirlsUnit> {
    static final ModelLayerLocation LAYER = BossClient.layer("boss_red_girls");
    static final ResourceLocation TEX = BossClient.tex("boss_red_girls"), TEX_NET = BossClient.tex("boss_red_girls_network");

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, RedGirlsModel::create); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, RedGirlsModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> BossClient.scaled(ctx, 1.3f, (RedGirlsUnit u) -> u.getForm() == 1 ? TEX_NET : TEX, new RedGirlsModel(ctx.bakeLayer(LAYER))));
    }

    static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        for (String s : new String[]{"A", "B"}) {
            PartDefinition g = mesh.getRoot().addOrReplaceChild("girl" + s, CubeListBuilder.create().texOffs(0, 16).addBox(-2, -7, -1.5f, 4, 7, 3)
                    .texOffs(0, 26).addBox(-3, 0, -2.5f, 6, 5, 5), PartPose.offset(s.equals("A") ? -5 : 5, 10, 0));
            PartDefinition h = g.addOrReplaceChild("head" + s, CubeListBuilder.create().texOffs(0, 0).addBox(-2.5f, -5, -2.5f, 5, 5, 5)
                    .texOffs(20, 0).addBox(-3, -5.5f, 1, 6, 11, 2).texOffs(36, 0).addBox(2.5f, -4.5f, -2, 1, 9, 4).texOffs(36, 0).addBox(-3.5f, -4.5f, -2, 1, 9, 4)
                    .texOffs(20, 40).addBox(-3, -4.5f, -3, 6, 1, 6), PartPose.offset(0, -7, 0));   // long black hair + red headband
            h.addOrReplaceChild("halo" + s, CubeListBuilder.create().texOffs(0, 48).addBox(-3.5f, -0.5f, -3.5f, 7, 1, 7), PartPose.offset(0, -7.5f, 0));
            g.addOrReplaceChild("armR" + s, CubeListBuilder.create().texOffs(24, 16).addBox(-1, 0, -1, 2, 7, 2).texOffs(0, 40).addBox(-1.5f, -0.5f, -1.5f, 3, 3, 3), PartPose.offset(-3, -6.5f, 0));
            g.addOrReplaceChild("armL" + s, CubeListBuilder.create().texOffs(32, 16).addBox(-1, 0, -1, 2, 7, 2).texOffs(0, 40).addBox(-1.5f, -0.5f, -1.5f, 3, 3, 3), PartPose.offset(3, -6.5f, 0));
        }
        return LayerDefinition.create(mesh, 64, 64);
    }

    public RedGirlsModel(ModelPart root) {
        super(root);
        bind("girlA", "girlA/headA", "girlA/headA/haloA", "girlA/armRA", "girlA/armLA",
             "girlB", "girlB/headB", "girlB/headB/haloB", "girlB/armRB", "girlB/armLB");

        // A thrusts her arm (10), then B (18); each leans into her own shot while the other recoils
        clip(VOLLEY, AnimClip.of(30)
            .bone("girlA").rot(0, 0, 0, 0).rot(7, -0.2f, 0.3f, 0).rot(10, 0.25f, -0.1f, 0, Ease.OUT).rot(18, -0.1f, 0, 0).rot(30, 0, 0, 0).pos(0, 0, 0, 0).pos(10, 0, 0, -1.5f, Ease.OUT).pos(18, 0, 0, 0.5f).pos(30, 0, 0, 0)
            .bone("armRA").rot(0, 0, 0, 0).rot(7, -2.6f, 0, 0.3f).rot(10, -1.6f, 0.2f, 0, Ease.OUT).rot(16, -1.3f, 0, 0).rot(30, 0, 0, 0)
            .bone("armLA").rot(0, 0, 0, 0).rot(10, 0.4f, 0, -0.6f).rot(30, 0, 0, 0)
            .bone("headA").rot(0, 0, 0, 0).rot(7, -0.3f, 0, 0).rot(10, 0.1f, 0, 0).rot(30, 0, 0, 0)
            .bone("girlB").rot(0, 0, 0, 0).rot(10, -0.1f, 0, 0).rot(15, -0.2f, -0.3f, 0).rot(18, 0.25f, 0.1f, 0, Ease.OUT).rot(30, 0, 0, 0).pos(0, 0, 0, 0).pos(18, 0, 0, -1.5f, Ease.OUT).pos(30, 0, 0, 0)
            .bone("armLB").rot(0, 0, 0, 0).rot(15, -2.6f, 0, -0.3f).rot(18, -1.6f, -0.2f, 0, Ease.OUT).rot(24, -1.3f, 0, 0).rot(30, 0, 0, 0)
            .bone("armRB").rot(0, 0, 0, 0).rot(18, 0.4f, 0, 0.6f).rot(30, 0, 0, 0)
            .bone("headB").rot(0, 0, 0, 0).rot(15, -0.3f, 0, 0).rot(18, 0.1f, 0, 0).rot(30, 0, 0, 0).done());

        // both rise and spread their arms; halos spin up and tilt into the lattice plane (20..44)
        AnimClip lat = AnimClip.of(60);
        for (String s : new String[]{"A", "B"}) {
            float side = s.equals("A") ? 1 : -1;
            lat.bone("girl" + s).pos(0, 0, 0, 0).pos(18, side * -1, -5, 0).pos(44, side * -1, -5, 0).pos(60, 0, 0, 0).rot(0, 0, 0, 0).rot(18, -0.3f, 0, side * 0.15f).rot(44, -0.3f, 0, side * 0.15f).rot(60, 0, 0, 0)
               .bone("armR" + s).rot(0, 0, 0, 0).rot(18, -0.5f, 0, 2.2f).rot(44, -0.5f, 0, 2.2f).rot(60, 0, 0, 0)
               .bone("armL" + s).rot(0, 0, 0, 0).rot(18, -0.5f, 0, -2.2f).rot(44, -0.5f, 0, -2.2f).rot(60, 0, 0, 0)
               .bone("halo" + s).rot(0, 0, 0, 0).rot(18, 0, 6f, 0).rot(20, 0.4f, 7f, 0).rot(44, 0.4f, 30f, 0, Ease.LINEAR).rot(60, 0, 32f, 0).pos(0, 0, 0, 0).pos(20, 0, -3, 0).pos(44, 0, -3, 0).pos(60, 0, 0, 0)
               .bone("head" + s).rot(0, 0, 0, 0).rot(18, -0.5f, 0, 0).rot(44, -0.5f, 0, 0).rot(60, 0, 0, 0);
        }
        clip(LATTICE, lat);

        // both reach down, palms toward the floor (8), and wrench upward as the hands erupt (20)
        AnimClip hd = AnimClip.of(40);
        for (String s : new String[]{"A", "B"}) {
            hd.bone("girl" + s).rot(0, 0, 0, 0).rot(16, 0.6f, 0, 0).rot(20, -0.4f, 0, 0, Ease.OUT).rot(40, 0, 0, 0).pos(0, 0, 0, 0).pos(16, 0, 2, 0).pos(20, 0, -3, 0, Ease.OUT).pos(40, 0, 0, 0)
              .bone("armR" + s).rot(0, 0, 0, 0).rot(8, -0.6f, 0, 0.4f).rot(16, -0.3f, 0, 0.2f).rot(20, -2.9f, 0, -0.2f, Ease.OUT).rot(30, -2.8f, 0, -0.2f).rot(40, 0, 0, 0)
              .bone("armL" + s).rot(0, 0, 0, 0).rot(8, -0.6f, 0, -0.4f).rot(16, -0.3f, 0, -0.2f).rot(20, -2.9f, 0, 0.2f, Ease.OUT).rot(30, -2.8f, 0, 0.2f).rot(40, 0, 0, 0)
              .bone("head" + s).rot(0, 0, 0, 0).rot(16, 0.6f, 0, 0).rot(20, -0.5f, 0, 0).rot(40, 0, 0, 0);
        }
        clip(HANDS, hd);

        // fold together and vanish (6), A slashes (12), vanish again (18), B slashes (22)
        clip(BLINK, AnimClip.of(30).blend(2)
            .bone("girlA").pos(0, 0, 0, 0).pos(5, 4, 0, 0).pos(6, 0, 0, 0, Ease.SNAP).pos(9, 1, 0, 2).pos(12, -2, 0, -3, Ease.OUT).pos(17, 4, 0, 0).pos(18, 0, 0, 0, Ease.SNAP).pos(30, 0, 0, 0)
                .rot(0, 0, 0, 0).rot(9, 0, 0.8f, 0).rot(12, 0.2f, -0.9f, 0, Ease.OUT).rot(17, 0, 0, 0).rot(30, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(5, 0.2f, 1.3f, 0.2f).scale(7, 1, 1, 1).scale(17, 0.2f, 1.3f, 0.2f).scale(19, 1, 1, 1)
            .bone("armRA").rot(0, 0, 0, 0).rot(9, -1.8f, 0.6f, 1.2f).rot(12, -1.4f, -0.6f, -0.8f, Ease.OUT).rot(16, -1.2f, -0.6f, -0.8f).rot(30, 0, 0, 0)
            .bone("girlB").pos(0, 0, 0, 0).pos(5, -4, 0, 0).pos(6, 0, 0, 0, Ease.SNAP).pos(17, -4, 0, 0).pos(18, 0, 0, 0, Ease.SNAP).pos(19, -1, 0, 2).pos(22, 2, 0, -3, Ease.OUT).pos(30, 0, 0, 0)
                .rot(0, 0, 0, 0).rot(19, 0, -0.8f, 0).rot(22, 0.2f, 0.9f, 0, Ease.OUT).rot(30, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(5, 0.2f, 1.3f, 0.2f).scale(7, 1, 1, 1).scale(17, 0.2f, 1.3f, 0.2f).scale(19, 1, 1, 1)
            .bone("armLB").rot(0, 0, 0, 0).rot(19, -1.8f, -0.6f, -1.2f).rot(22, -1.4f, 0.6f, 0.8f, Ease.OUT).rot(26, -1.2f, 0.6f, 0.8f).rot(30, 0, 0, 0).done());

        // the twins orbit each other, merge at the centre, and burst apart (40)
        AnimClip mf = AnimClip.of(60);
        for (String s : new String[]{"A", "B"}) {
            float side = s.equals("A") ? 1 : -1;
            mf.bone("girl" + s).pos(0, 0, 0, 0).pos(10, side * 5, -2, side * 3).pos(20, 0, -4, side * -4).pos(30, side * 4, -6, 0).pos(38, side * 5, -6, 0).pos(41, side * -3, -3, 0, Ease.OUT).pos(60, 0, 0, 0)
              .rot(0, 0, 0, 0).rot(30, 0, side * 6.28f, 0).rot(38, -0.5f, side * 6.28f, 0).rot(41, 0.3f, side * 6.28f, side * 0.4f).rot(60, 0, side * 6.28f, 0)
              .bone("armR" + s).rot(0, 0, 0, 0).rot(30, -0.4f, 0, -0.6f).rot(41, -0.3f, 0, 2.4f, Ease.OUT).rot(60, 0, 0, 0)
              .bone("armL" + s).rot(0, 0, 0, 0).rot(30, -0.4f, 0, 0.6f).rot(41, -0.3f, 0, -2.4f, Ease.OUT).rot(60, 0, 0, 0)
              .bone("halo" + s).scale(0, 1, 1, 1).scale(38, 0.5f, 1, 0.5f).scale(41, 2.2f, 1, 2.2f).scale(60, 1, 1, 1);
        }
        clip(MANIFEST, mf);

        // twins drift apart and each drags a tendril across the field in opposite directions (16..40)
        AnimClip td = AnimClip.of(50)
            .bone("girlA").pos(0, 0, 0, 0).pos(14, 3, -2, 0).pos(40, 3, -2, 0).pos(50, 0, 0, 0).rot(0, 0, 0, 0).rot(14, 0, 1.2f, 0).rot(16, 0, 1.2f, 0).rot(40, 0, -1.2f, 0, Ease.LINEAR).rot(50, 0, 0, 0)
            .bone("armRA").rot(0, 0, 0, 0).rot(14, -1.6f, 0, 0).rot(40, -1.6f, 0, 0).rot(50, 0, 0, 0)
            .bone("girlB").pos(0, 0, 0, 0).pos(14, -3, -2, 0).pos(40, -3, -2, 0).pos(50, 0, 0, 0).rot(0, 0, 0, 0).rot(14, 0, -1.2f, 0).rot(16, 0, -1.2f, 0).rot(40, 0, 1.2f, 0, Ease.LINEAR).rot(50, 0, 0, 0)
            .bone("armLB").rot(0, 0, 0, 0).rot(14, -1.6f, 0, 0).rot(40, -1.6f, 0, 0).rot(50, 0, 0, 0)
            .bone("headA").rot(0, 0, 0, 0).rot(16, 0.2f, 0, 0).rot(40, 0.2f, 0, 0).rot(50, 0, 0, 0)
            .bone("headB").rot(0, 0, 0, 0).rot(16, 0.2f, 0, 0).rot(40, 0.2f, 0, 0).rot(50, 0, 0, 0).done();
        clip(TENDRILS, td);

        // hands joined overhead, halos merge into one ring above them, then they clap downward (14)
        clip(CONVERGE, AnimClip.of(50)
            .bone("girlA").pos(0, 0, 0, 0).pos(10, 3, -3, 0).pos(14, 3, -1, 0, Ease.OUT).pos(40, 3, -1, 0).pos(50, 0, 0, 0).rot(0, 0, 0, 0).rot(10, 0, 0, -0.3f).rot(40, 0, 0, -0.3f).rot(50, 0, 0, 0)
            .bone("girlB").pos(0, 0, 0, 0).pos(10, -3, -3, 0).pos(14, -3, -1, 0, Ease.OUT).pos(40, -3, -1, 0).pos(50, 0, 0, 0).rot(0, 0, 0, 0).rot(10, 0, 0, 0.3f).rot(40, 0, 0, 0.3f).rot(50, 0, 0, 0)
            .bone("armLA").rot(0, 0, 0, 0).rot(10, -3.0f, 0, -0.6f).rot(14, -1.4f, 0, -0.9f, Ease.IN).rot(40, -1.4f, 0, -0.9f).rot(50, 0, 0, 0)
            .bone("armRB").rot(0, 0, 0, 0).rot(10, -3.0f, 0, 0.6f).rot(14, -1.4f, 0, 0.9f, Ease.IN).rot(40, -1.4f, 0, 0.9f).rot(50, 0, 0, 0)
            .bone("haloA").pos(0, 0, 0, 0).pos(10, 2.5f, -4, 0).pos(40, 2.5f, -4, 0).pos(50, 0, 0, 0).scale(0, 1, 1, 1).scale(10, 1.6f, 1, 1.6f).scale(40, 1.6f, 1, 1.6f).scale(50, 1, 1, 1)
            .bone("haloB").pos(0, 0, 0, 0).pos(10, -2.5f, -4, 0).pos(40, -2.5f, -4, 0).pos(50, 0, 0, 0).scale(0, 1, 1, 1).scale(10, 1.6f, 1, 1.6f).scale(40, 1.6f, 1, 1.6f).scale(50, 1, 1, 1).done());
    }

    @Override protected void idle(RedGirlsUnit e, float ls, float la, float age, float yaw, float pitch) {
        float hy = yaw * Mth.DEG_TO_RAD, hp = pitch * Mth.DEG_TO_RAD;
        for (String s : new String[]{"A", "B"}) {
            float ph = s.equals("A") ? 0 : 1.7f, side = s.equals("A") ? 1 : -1;
            bone("girl" + s).y += Mth.sin(age * 0.09f + ph) * 1.2f;
            bone("girl" + s).zRot = Mth.sin(age * 0.05f + ph) * 0.06f;
            bone("girl" + s).xRot = la * 0.4f;                      // lean into the glide
            bone("head" + s).yRot = hy + side * 0.15f; bone("head" + s).xRot = hp;
            bone("halo" + s).yRot = age * 0.06f * side;
            bone("armR" + s).zRot = 0.15f + Mth.sin(age * 0.07f + ph) * 0.08f;
            bone("armL" + s).zRot = -0.15f - Mth.sin(age * 0.07f + ph) * 0.08f;
        }
        // twins hold hands at rest (inner arms reach toward each other)
        bone("armLA").zRot = -0.7f; bone("armRB").zRot = 0.7f;
    }
}
