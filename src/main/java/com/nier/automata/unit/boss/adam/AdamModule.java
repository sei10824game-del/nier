package com.nier.automata.unit.boss.adam;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class AdamModule extends BossModule<AdamUnit> {
    public AdamModule() { super("boss_adam", "Adam", "\u30a2\u30c0\u30e0", AdamUnit::new, 0.7f, 2.0f, 420, 10, 0.32, 8, 0xd8d4cc, 0xc0202c); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { AdamModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { AdamModel.renderers(e, this); }
}
