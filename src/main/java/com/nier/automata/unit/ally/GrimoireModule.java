package com.nier.automata.unit.ally;

import net.minecraftforge.client.event.EntityRenderersEvent;

public class GrimoireModule extends AllyModule<GrimoireUnit> {
    public GrimoireModule() { super("ally_grimoire", "Grimoire (Weiss / Noir / Rubrum)", "Grimoire", GrimoireUnit::new, 0.8f, 1.0f, 40, 4, 0.3, 4, 0xb8bcc4, 0x20262c); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { GrimoireClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { GrimoireClient.renderers(e, type.get()); }
}
