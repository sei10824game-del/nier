package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class PopolaModule extends AllyModule<TwinUnit> {
    public PopolaModule() { super("ally_popola", "Popola", "Popola", TwinUnit::popola, 0.6f, 1.8f, 60, 4, 0.3, 2, 0x2a2060, 0xe0c8a0); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { TwinClient.layers(e, id()); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { TwinClient.renderers(e, type.get(), id()); }
}
