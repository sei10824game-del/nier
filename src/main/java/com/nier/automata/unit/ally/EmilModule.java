package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class EmilModule extends AllyModule<EmilUnit> {
    public EmilModule() { super("ally_emil", "Emil", "Emil", EmilUnit::new, 1.0f, 1.7f, 220, 6, 0.3, 6, 0xe8e2d0, 0x30302a); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { EmilClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { EmilClient.renderers(e, type.get()); }
}
