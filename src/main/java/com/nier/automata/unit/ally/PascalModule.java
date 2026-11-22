package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class PascalModule extends AllyModule<PascalUnit> {
    public PascalModule() {
        super("ally_pascal", "Pascal", "Pascal", PascalUnit::new, 0.8f, 1.6f, 100, 6, 0.26, 8, 0x8ab0a8, 0xd8a040);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { PascalClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { PascalClient.renderers(e, type.get()); }
}
