package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class YorhaHeavyModule extends AllyModule<YorhaHeavyUnit> {
    public YorhaHeavyModule() {
        super("ally_yorha_heavy", "YoRHa Heavy Armament", "YoRHa Juusou-gata", YorhaHeavyUnit::new, 0.7f, 1.9f, 110, 6, 0.24, 10, 0x2a2a32, 0xc03030);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { YorhaSoldierClient.layers(e, "ally_yorha_heavy"); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { YorhaSoldierClient.renderers(e, "ally_yorha_heavy", (net.minecraft.world.entity.EntityType) type.get()); }
}
