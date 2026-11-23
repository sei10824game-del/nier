package com.nier.automata.unit.boss.eve;

import com.nier.automata.unit.boss.BossModule;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class EveModule extends BossModule<EveUnit> {
    public EveModule() { super("boss_eve", "Eve", "\u30a4\u30f4", EveUnit::new, 0.9f, 2.2f, 460, 12, 0.34, 10, 0x2a2628, 0xe03020); }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { EveModel.layers(e); }
    @Override protected void renderers(EntityRenderersEvent.RegisterRenderers e) { EveModel.renderers(e, this); }
}
