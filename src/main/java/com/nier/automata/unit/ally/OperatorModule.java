package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

/** Shared operator module; concrete ones are {@link Operator6OModule} and {@link Operator21OModule}. */
public abstract class OperatorModule extends AllyModule<OperatorUnit> {
    protected OperatorModule(String id, String name, int egg1, int egg2) {
        super(id, name, name, OperatorUnit::new, 0.6f, 1.8f, 24, 2, 0.3, 2, egg1, egg2);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { OperatorClient.layers(e, id()); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { OperatorClient.renderers(e, type.get(), id()); }
}
