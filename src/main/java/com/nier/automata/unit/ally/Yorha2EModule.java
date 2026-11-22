package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class Yorha2EModule extends AllyModule<Yorha2EUnit> {
    public Yorha2EModule() {
        super("ally_yorha_2e", "YoRHa No.2 Type E (2E)", "YoRHa 2E", Yorha2EUnit::new, 0.6f, 1.8f, 90, 6, 0.33, 6, 0x101014, 0xb01818);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { Yorha2EClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { Yorha2EClient.renderers(e, type.get()); }
}
