package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class ResistanceModule extends AllyModule<ResistanceUnit> {
    public ResistanceModule() {
        super("ally_resistance_soldier", "Resistance Soldier", "Resistance Soldier", ResistanceUnit::new, 0.6f, 1.8f, 30, 4, 0.3, 3, 0x4a5030, 0x8a6a40);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { ResistanceClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { ResistanceClient.renderers(e, type.get()); }
}
