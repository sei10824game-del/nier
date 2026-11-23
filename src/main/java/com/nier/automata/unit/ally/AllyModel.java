package com.nier.automata.unit.ally;

import com.nier.automata.client.NierModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Client-only humanoid base for the android roster. Standard skeleton (all names unique):
 * body(hips, y=12) > torso > head, rightArm > weapon, leftArm ; body > rightLeg, leftLeg, pod.
 * Texture 128x128, std UV: head(0,0) torso(16,16) rArm(0,32) lArm(12,32) rLeg(24,32) lLeg(36,32) pod(96,0).
 * Units add extras (hair, coat, weapon cubes) on the returned parts, UVs at y>=48 / x>=48.
 */
public class AllyModel<T extends AllyUnit> extends NierModel<T> {
    public static final String[] STD = {"body", "body/torso", "body/torso/head", "body/torso/rightArm", "body/torso/rightArm/weapon",
            "body/torso/leftArm", "body/rightLeg", "body/leftLeg", "body/pod"};

    public record Parts(PartDefinition root, PartDefinition body, PartDefinition torso, PartDefinition head,
                        PartDefinition rightArm, PartDefinition weapon, PartDefinition leftArm,
                        PartDefinition rightLeg, PartDefinition leftLeg, PartDefinition pod) {}

    /** Builds the standard skeleton. legLen 12 = adult; pod=false gives an empty pod bone. weaponRest = x rotation of the weapon bone. */
    public static Parts humanoid(MeshDefinition m, boolean pod, float weaponRest) {
        PartDefinition root = m.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0, 12, 0));
        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4, -12, -2, 8, 12, 4), PartPose.ZERO);
        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, -12, 0));
        PartDefinition ra = torso.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-2, -2, -1.5f, 3, 12, 3), PartPose.offset(-5, -10, 0));
        PartDefinition wp = ra.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5f, 9, 0, weaponRest, 0, 0));
        PartDefinition la = torso.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(12, 32).addBox(-1, -2, -1.5f, 3, 12, 3), PartPose.offset(5, -10, 0));
        PartDefinition rl = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(24, 32).addBox(-1.5f, 0, -1.5f, 3, 12, 3), PartPose.offset(-2, 0, 0));
        PartDefinition ll = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(36, 32).addBox(-1.5f, 0, -1.5f, 3, 12, 3), PartPose.offset(2, 0, 0));
        CubeListBuilder pc = CubeListBuilder.create();
        if (pod) pc.texOffs(96, 0).addBox(-2, -3, -3, 4, 5, 6).texOffs(116, 0).addBox(-3, -2, -2, 1, 3, 4).texOffs(116, 0).addBox(2, -2, -2, 1, 3, 4);
        PartDefinition pd = body.addOrReplaceChild("pod", pc, PartPose.offset(-10, -22, 6));
        return new Parts(root, body, torso, head, ra, wp, la, rl, ll, pd);
    }

    // ---- standard weapon cube sets (UV block at (64,32)..(128,64)) ----
    /** Katana-style sword extending from the fist: hilt, guard, blade (length l). */
    public static CubeListBuilder sword(int l) {
        return CubeListBuilder.create().texOffs(82, 32).addBox(-0.5f, -2, -0.5f, 1, 4, 1)
                .texOffs(70, 32).addBox(-2, 2, -1, 4, 1, 2).texOffs(64, 32).addBox(-0.5f, 3, -1, 1, l, 2);
    }
    /** Two-handed great sword (width 4). */
    public static CubeListBuilder greatSword(int l) {
        return CubeListBuilder.create().texOffs(82, 32).addBox(-0.5f, -3, -0.5f, 1, 5, 1)
                .texOffs(70, 32).addBox(-3, 2, -1, 6, 1, 2).texOffs(64, 48).addBox(-2, 3, -0.5f, 4, l, 1);
    }
    /** Spear through the fist: shaft + blade at the +y end. */
    public static CubeListBuilder spear() {
        return CubeListBuilder.create().texOffs(86, 32).addBox(-0.5f, -10, -0.5f, 1, 24, 1).texOffs(90, 32).addBox(-1, 14, -0.5f, 2, 5, 1);
    }
    /** Rifle along +y. */
    public static CubeListBuilder rifle() {
        return CubeListBuilder.create().texOffs(96, 32).addBox(-1, -2, -1.5f, 2, 10, 3).texOffs(106, 32).addBox(-0.5f, 8, -0.5f, 1, 4, 1);
    }

    protected AllyModel(ModelPart root, String... extra) {
        super(root);
        bind(STD);
        if (extra.length > 0) bind(extra);
    }

    protected ModelPart b(String n) { return bones.get(n); }

    @Override protected void idle(T e, float ls, float la, float age, float yaw, float pitch) {
        float sw = Mth.cos(ls * 0.6662f) * 1.2f * la;
        ModelPart h = b("head");
        if (h != null) { h.yRot += yaw * Mth.DEG_TO_RAD; h.xRot += pitch * Mth.DEG_TO_RAD; }
        float breathe = Mth.sin(age * 0.08f);
        if (b("torso") != null) b("torso").xRot += 0.03f * breathe + la * 0.15f;
        if (b("rightArm") != null) { b("rightArm").xRot -= sw * 0.7f; b("rightArm").zRot += 0.06f + 0.03f * breathe; }
        if (b("leftArm") != null) { b("leftArm").xRot += sw * 0.7f; b("leftArm").zRot -= 0.06f + 0.03f * breathe; }
        if (b("rightLeg") != null) b("rightLeg").xRot += sw;
        if (b("leftLeg") != null) b("leftLeg").xRot -= sw;
        if (b("pod") != null) { b("pod").y += Mth.sin(age * 0.1f) * 1.2f; b("pod").yRot += Mth.sin(age * 0.03f) * 0.3f; }
    }
}
