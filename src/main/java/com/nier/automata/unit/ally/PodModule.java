package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class PodModule extends AllyModule<PodUnit> {
    public PodModule() { super("ally_pod", "Support Pod (042 / 153)", "Pod", PodUnit::new, 0.6f, 0.7f, 30, 3, 0.3, 4, 0xc8b898, 0x3a3a3a); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { PodClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { PodClient.renderers(e, type.get()); }
}
