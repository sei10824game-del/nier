package com.nier.automata.unit.boss.grun;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class GrunModule extends BossModule<GrunUnit> {
    public GrunModule() { super("boss_grun", "Gr\u00fcn", "\u30b0\u30ea\u30e5\u30f3", GrunUnit::new, 5.0f, 7.2f, 1400, 18, 0.1, 18, 0x3e4a52, 0x40e0ff); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { GrunModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { GrunModel.renderers(e, this); }
}
