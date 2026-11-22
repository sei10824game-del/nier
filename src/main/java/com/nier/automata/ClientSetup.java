package com.nier.automata;

import com.nier.automata.core.UnitModule;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientSetup {
    static void init(IEventBus bus) {
        bus.addListener((EntityRenderersEvent.RegisterLayerDefinitions e) -> { for (UnitModule<?> m : Modules.ALL) m.registerLayers(e); });
        bus.addListener((EntityRenderersEvent.RegisterRenderers e) -> { for (UnitModule<?> m : Modules.ALL) m.registerRenderers(e); });
    }
}
