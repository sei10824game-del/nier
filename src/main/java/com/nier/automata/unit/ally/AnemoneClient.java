package com.nier.automata.unit.ally;

import static com.nier.automata.unit.ally.AnemoneUnit.*;

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

/**
 * Resistance leader: original design - long charcoal coat with shoulder cape, tied-back dark hair, sabre in the right
 * hand, carbine on the back that swaps to the left hand while shooting.
 */
public final class AnemoneClient {
    static final String ID = "ally_anemone";

    public static LayerDefinition layer() {
        MeshDefinition m = new MeshDefinition();
        AllyModel.Parts p = AllyModel.humanoid(m, false, -1.3f);
        p.head().addOrReplaceChild("hair", CubeListBuilder.create().texOffs(48, 16).addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
                .texOffs(48, 0).addBox(-1.5f, -6, 4, 3, 6, 2), PartPose.ZERO);
        p.torso().addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 64).addBox(-4.6f, -1, -2.6f, 9.2f, 10, 5.2f), PartPose.ZERO);
        p.torso().addOrReplaceChild("cape", CubeListBuilder.create().texOffs(32, 64).addBox(-5, -12.5f, -2.8f, 10, 4, 5.6f), PartPose.ZERO);
        p.torso().addOrReplaceChild("backGun", AllyModel.rifle(), PartPose.offsetAndRotation(0, -11, 3, 0, 0, 0.7f));
        p.leftArm().addOrReplaceChild("handGun", AllyModel.rifle(), PartPose.offsetAndRotation(0.5f, 9, 0, 0, 0, 0));
        p.weapon().addOrReplaceChild("sabre", AllyModel.sword(14), PartPose.ZERO);
        return LayerDefinition.create(m, 128, 128);
    }

    /** Aim mirrored to the left hand (carbine), sabre held low in the right. */
    static Pose aimL() {
        return Clips.P().r("torso", 0, -0.35f, 0).r("head", 0, 0.3f, 0).r("leftArm", -1.55f, -0.15f, 0).r("rightArm", -1.2f, -0.7f, 0)
                .r("handGun", 0, 0, 0).p("body", 0, 0.8f, 0).r("rightLeg", -0.25f, 0, 0.05f).r("leftLeg", 0.3f, 0, -0.05f);
    }
    static Pose kickL(float yaw) { return aimL().with(Clips.P().r("leftArm", -1.75f, -0.15f + yaw, 0).r("torso", -0.08f, -0.35f + yaw, 0).p("leftArm", 0, 0, 1)); }

    static Map<Integer, AnimClip> clips() {
        Clips.Seq volley = Clips.seq(30).key(5, aimL());
        for (int t : new int[]{10, 15, 20}) volley.key(t, kickL(0), Ease.OUT).key(t + 3, aimL());
        Clips.Seq cover = Clips.seq(40).key(5, aimL());
        for (int t = 8; t <= 34; t += 2) { float sweep = (t - 21) / 13f * 0.6f; cover.key(t, kickL(-sweep), Ease.OUT); }
        return Map.of(
            COMBO, Clips.combo(26, true, 6, 12, 19),
            RISING, Clips.launcher(20, 12),
            VOLLEY, volley.build(),
            COVER, cover.build());
    }

    static class Model extends AllyClient.Model<AnemoneUnit> {
        Model(ModelPart r) { super(r, clips(), "body/torso/backGun", "body/torso/leftArm/handGun"); }
        @Override protected void idle(AnemoneUnit e, float ls, float la, float age, float yaw, float pitch) {
            super.idle(e, ls, la, age, yaw, pitch);
            boolean shooting = e.getSkillId() == VOLLEY || e.getSkillId() == COVER;
            b("handGun").visible = shooting; b("backGun").visible = !shooting;
        }
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { AllyClient.layers(e, ID, AnemoneClient::layer); }
    public static void renderers(EntityRenderersEvent.RegisterRenderers e, EntityType<AnemoneUnit> t) {
        AllyClient.renderer(e, t, ID, 0.5f, u -> AllyClient.tex(ID), Model::new);
    }
}
