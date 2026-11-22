package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class DevolaModule extends AllyModule<TwinUnit> {
    public DevolaModule() { super("ally_devola", "Devola", "Devola", TwinUnit::devola, 0.6f, 1.8f, 60, 4, 0.3, 2, 0x7a1820, 0xe0c8a0); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { TwinClient.layers(e, id()); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { TwinClient.renderers(e, type.get(), id()); }
}
