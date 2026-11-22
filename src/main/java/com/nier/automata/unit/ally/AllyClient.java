package com.nier.automata.unit.ally;

import com.nier.automata.NierAutomata;
import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.client.NierRenderer;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client-only registration helpers shared by the roster's *Client classes. */
public final class AllyClient {
    private AllyClient() {}
    public static ModelLayerLocation layer(String id) { return new ModelLayerLocation(new ResourceLocation(NierAutomata.MODID, id), "main"); }
    public static ResourceLocation tex(String name) { return new ResourceLocation(NierAutomata.MODID, "textures/entity/" + name + ".png"); }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e, String id, Supplier<LayerDefinition> def) {
        e.registerLayerDefinition(layer(id), def);
    }
    /** Registers a NierRenderer using one model built by factory (root part -> model). */
    public static <T extends AllyUnit> void renderer(EntityRenderersEvent.RegisterRenderers e, EntityType<T> type, String id, float shadow,
                                                     Function<T, ResourceLocation> tex, Function<ModelPart, NierModel<T>> model) {
        e.registerEntityRenderer(type, ctx -> new NierRenderer<>(ctx, shadow, tex, model.apply(ctx.bakeLayer(layer(id)))));
    }

    /** Plain humanoid model with a skill-id -> clip table and optional extra bone paths. */
    public static class Model<T extends AllyUnit> extends AllyModel<T> {
        public Model(ModelPart root, Map<Integer, AnimClip> clips, String... extra) {
            super(root, extra);
            clips.forEach(this::clip);
        }
    }
}
