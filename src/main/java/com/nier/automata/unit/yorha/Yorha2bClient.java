package com.nier.automata.unit.yorha;

import com.nier.automata.NierAutomata;
import com.nier.automata.client.NierRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client registration of the B android; form 1 (overload) swaps to the scorched texture. */
public final class Yorha2bClient {
    static final ModelLayerLocation LAYER = layer("yorha_2b");
    static final ResourceLocation T0 = tex("yorha_2b"), T1 = tex("yorha_2b_overload");

    static ModelLayerLocation layer(String id) { return new ModelLayerLocation(new ResourceLocation(NierAutomata.MODID, id), "main"); }
    static ResourceLocation tex(String name) { return new ResourceLocation(NierAutomata.MODID, "textures/entity/" + name + ".png"); }

    static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) { e.registerLayerDefinition(LAYER, Yorha2bModel::layer); }
    static void renderers(EntityRenderersEvent.RegisterRenderers e, Yorha2bModule m) {
        e.registerEntityRenderer(m.type.get(), ctx -> new NierRenderer<>(ctx, 0.5f, u -> u.getForm() == 1 ? T1 : T0, new Yorha2bModel(ctx.bakeLayer(LAYER))));
    }
    static void copyRenderers(EntityRenderersEvent.RegisterRenderers e, EntityType<Yorha2bCopyUnit> t) {
        ResourceLocation c0 = tex("yorha_2b_copy"), c1 = tex("yorha_2b_copy_overload");
        e.registerEntityRenderer(t, ctx -> new NierRenderer<>(ctx, 0.5f, u -> u.getForm() == 1 ? c1 : c0, new Yorha2bModel(ctx.bakeLayer(LAYER))));
    }
}
