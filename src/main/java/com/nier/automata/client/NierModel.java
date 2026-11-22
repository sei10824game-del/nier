package com.nier.automata.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nier.automata.core.NierUnit;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * Base model. Bones are found BY NAME anywhere under the root (names must be unique) so clips and
 * idle code address them as "rightArm" etc. Subclass: build LayerDefinition statically, implement
 * idle(), and call clip(skillId, AnimClip) in the constructor for every Skill.
 */
public abstract class NierModel<T extends NierUnit> extends EntityModel<T> {
    protected final ModelPart root;
    protected final Map<String, ModelPart> bones = new HashMap<>();
    protected final Map<String, float[]> rest = new HashMap<>(); // x,y,z,xr,yr,zr
    private final Map<Integer, AnimClip> clips = new HashMap<>();

    protected NierModel(ModelPart root) { this.root = root; bones.put("root", root); }

    /** Call at end of ctor: bind("body", "body/head", "body/head/hair") - key = last path segment (must be unique). */
    protected final void bind(String... paths) {
        for (String path : paths) {
            ModelPart p = root;
            for (String seg : path.split("/")) p = p.getChild(seg);
            bones.put(path.substring(path.lastIndexOf('/') + 1), p);
        }
        bones.forEach((n, p) -> rest.put(n, new float[]{p.x, p.y, p.z, p.xRot, p.yRot, p.zRot}));
    }

    protected final void clip(int skillId, AnimClip c) { clips.put(skillId, c); }
    public ModelPart bone(String n) { return bones.get(n); }

    /** Idle / walk / look animation. Bones are already reset to rest. */
    protected abstract void idle(T e, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch);

    @Override public final void setupAnim(T e, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        bones.forEach((n, p) -> { float[] r = rest.get(n); if (r == null) return;
            p.setPos(r[0], r[1], r[2]); p.xRot = r[3]; p.yRot = r[4]; p.zRot = r[5]; p.xScale = p.yScale = p.zScale = 1f; });
        idle(e, limbSwing, limbSwingAmount, age, headYaw, headPitch);
        AnimClip c = clips.get(e.getSkillId());
        if (c != null) c.apply(bones, rest, e.skillTime + (age - e.tickCount));
    }

    @Override public void renderToBuffer(PoseStack ps, VertexConsumer vc, int light, int overlay, float r, float g, float b, float a) {
        root.render(ps, vc, light, overlay, r, g, b, a);
    }
}
