package com.nier.automata.unit.yorha;

import com.nier.automata.client.NierRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client registration of the A android; form 1 (berserk) uses the red-glow texture. */
public final class A2Client {
    static final ModelLayerLocation LAYER = Yorha2bClient.layer("a2");
    static final ResourceLocation T0 = Yorha2bClient.tex("a2"), T1 = Yorha2bClient.tex("a2_berserk");

    static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, A2Model::layer); }
    static void renderers(EntityRenderersEvent.RegisterRenderers e, A2Module m) {
        e.registerEntityRenderer(m.type.get(), ctx -> new NierRenderer<>(ctx, 0.5f, u -> u.getForm() == 1 ? T1 : T0, new A2Model(ctx.bakeLayer(LAYER))));
    }
}
