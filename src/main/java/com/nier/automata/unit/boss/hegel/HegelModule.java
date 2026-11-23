package com.nier.automata.unit.boss.hegel;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class HegelModule extends BossModule<HegelUnit> {
    public HegelModule() { super("boss_hegel", "Hegel", "\u30d8\u30fc\u30b2\u30eb", HegelUnit::new, 3.0f, 3.2f, 700, 14, 0.24, 12, 0x5a5650, 0xff2a2a); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { HegelModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { HegelModel.renderers(e, this); }
}
