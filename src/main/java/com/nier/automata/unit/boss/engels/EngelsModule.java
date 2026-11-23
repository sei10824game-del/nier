package com.nier.automata.unit.boss.engels;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class EngelsModule extends BossModule<EngelsUnit> {
    public EngelsModule() { super("boss_engels", "Engels", "\u30a8\u30f3\u30b2\u30eb\u30b9", EngelsUnit::new, 3.4f, 6.2f, 900, 16, 0.22, 14, 0x6a6258, 0xff8a20); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { EngelsModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { EngelsModel.renderers(e, this); }
}
