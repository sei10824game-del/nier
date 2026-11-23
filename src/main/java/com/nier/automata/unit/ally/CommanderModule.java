package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class CommanderModule extends AllyModule<CommanderUnit> {
    public CommanderModule() {
        super("ally_commander", "YoRHa Commander", "YoRHa Commander", CommanderUnit::new, 0.6f, 1.85f, 80, 6, 0.3, 6, 0xeeeeee, 0xc8a040);
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { CommanderClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { CommanderClient.renderers(e, type.get()); }
}
