package com.nier.automata.unit.boss.redgirls;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RedGirlsModule extends BossModule<RedGirlsUnit> {
    public RedGirlsModule() { super("boss_red_girls", "Red Girls", "\u8d64\u3044\u5c11\u5973", RedGirlsUnit::new, 1.6f, 2.4f, 650, 13, 0.3, 6, 0xc01020, 0x1a0408); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { RedGirlsModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { RedGirlsModel.renderers(e, this); }
}
