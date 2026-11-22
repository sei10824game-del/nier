package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class EmilHackedModule extends AllyModule<EmilHackedUnit> {
    public EmilHackedModule() { super("ally_emil_hacked", "Emil (Hacked)", "Emil (Hacked)", EmilHackedUnit::new, 1.4f, 3.0f, 300, 10, 0.27, 12, 0x1a1a1e, 0xd02020); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { EmilHackedClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { EmilHackedClient.renderers(e, type.get()); }
}
