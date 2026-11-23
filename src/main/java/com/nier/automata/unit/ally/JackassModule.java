package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class JackassModule extends AllyModule<JackassUnit> {
    public JackassModule() {
        super("ally_jackass", "Jackass", "Jackass", JackassUnit::new, 0.6f, 1.8f, 50, 4, 0.3, 3, 0x6a4a2a, 0xd09030);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { JackassClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { JackassClient.renderers(e, type.get()); }
}
