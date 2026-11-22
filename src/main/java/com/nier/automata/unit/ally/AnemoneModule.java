package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class AnemoneModule extends AllyModule<AnemoneUnit> {
    public AnemoneModule() {
        super("ally_anemone", "Anemone", "Anemone", AnemoneUnit::new, 0.6f, 1.8f, 80, 6, 0.32, 5, 0x2a2a2e, 0x6a8a5a);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { AnemoneClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { AnemoneClient.renderers(e, type.get()); }
}
