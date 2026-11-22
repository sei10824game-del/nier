package com.nier.automata.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nier.automata.core.NierUnit;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renderer that swaps the model by unit form (models[form], clamped) and textures via a function. */
public class NierRenderer<T extends NierUnit> extends MobRenderer<T, NierModel<T>> {
    private final NierModel<T>[] models;
    private final Function<T, ResourceLocation> tex;

    @SafeVarargs
    public NierRenderer(EntityRendererProvider.Context ctx, float shadow, Function<T, ResourceLocation> tex, NierModel<T>... models) {
        super(ctx, models[0], shadow);
        this.models = models; this.tex = tex;
    }
    @Override public void render(T e, float yaw, float pt, PoseStack ps, MultiBufferSource buf, int light) {
        this.model = models[Math.min(Math.max(e.getForm(), 0), models.length - 1)];
        super.render(e, yaw, pt, ps, buf, light);
    }
    @Override public ResourceLocation getTextureLocation(T e) { return tex.apply(e); }
}
