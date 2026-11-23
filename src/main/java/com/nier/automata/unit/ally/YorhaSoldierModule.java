package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class YorhaSoldierModule extends AllyModule<YorhaSoldierUnit> {
    public YorhaSoldierModule() {
        super("ally_yorha_soldier", "YoRHa Type-E Soldier", "YoRHa E-gata Hei", YorhaSoldierUnit::new, 0.6f, 1.8f, 40, 4, 0.3, 4, 0x15151a, 0xd8d0c0);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { YorhaSoldierClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { YorhaSoldierClient.renderers(e, type.get()); }
}
