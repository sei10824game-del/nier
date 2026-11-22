package com.nier.automata.unit.yorha;

import com.nier.automata.client.NierRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public final class Yorha9sClient {
    static final ModelLayerLocation LAYER = Yorha2bClient.layer("yorha_9s");
    static final ResourceLocation T0 = Yorha2bClient.tex("yorha_9s");

    static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, Yorha9sModel::layer); }
    static void renderers(EntityRenderersEvent.RegisterRenderers e, Yorha9sModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> new NierRenderer<>(ctx, 0.5f, u -> T0, new Yorha9sModel(ctx.bakeLayer(LAYER))));
    }
}
