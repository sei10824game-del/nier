package com.nier.automata.unit.boss.marx;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class MarxModule extends BossModule<MarxUnit> {
    public MarxModule() { super("boss_marx", "Marx", "\u30de\u30eb\u30af\u30b9", MarxUnit::new, 3.6f, 3.2f, 600, 14, 0.2, 16, 0x8a6a3a, 0x3a3836); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { MarxModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { MarxModel.renderers(e, this); }
}
