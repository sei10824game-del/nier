package com.nier.automata.unit.boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nier.automata.NierAutomata;
import com.nier.automata.client.NierModel;
import com.nier.automata.client.NierRenderer;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client-only helpers for boss renderers. */
public final class BossClient {
    private static boolean orbDone;
    private BossClient() {}

    static void orbRenderer(EntityRenderersEvent.RegisterRenderers e) {
        if (orbDone) return;
        orbDone = true;
        e.registerEntityRenderer(BossOrb.TYPE.get(), NoopRenderer::new);
    }

    public static ModelLayerLocation layer(String id) { return new ModelLayerLocation(new ResourceLocation(NierAutomata.MODID, id), "main"); }
    public static ResourceLocation tex(String name) { return new ResourceLocation(NierAutomata.MODID, "textures/entity/" + name + ".png"); }

    /** Renderer with a uniform model scale (bosses are modelled at humanoid scale then enlarged). */
    public static <T extends BossUnit> NierRenderer<T> scaled(EntityRendererProvider.Context ctx, float scale, ResourceLocation tex, NierModel<T> model) {
        return scaled(ctx, scale, e -> tex, model);
    }
    /** Texture chosen per entity (e.g. per form). */
    public static <T extends BossUnit> NierRenderer<T> scaled(EntityRendererProvider.Context ctx, float scale, Function<T, ResourceLocation> tex, NierModel<T> model) {
        return new NierRenderer<T>(ctx, 0.5f * scale, tex, model) {
            @Override protected void scale(T e, PoseStack ps, float pt) { ps.scale(scale, scale, scale); }
        };
    }
}
