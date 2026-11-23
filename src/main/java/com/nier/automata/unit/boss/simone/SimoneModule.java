package com.nier.automata.unit.boss.simone;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class SimoneModule extends BossModule<SimoneUnit> {
    public SimoneModule() { super("boss_simone", "Simone", "\u30b7\u30e2\u30fc\u30cc", SimoneUnit::new, 2.0f, 4.6f, 800, 14, 0.26, 10, 0xe8e0d8, 0xff4fa0); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { SimoneModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { SimoneModel.renderers(e, this); }
}
