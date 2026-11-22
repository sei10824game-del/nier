package com.nier.automata.unit.yorha;

import com.nier.automata.core.UnitModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class Yorha2bModule extends UnitModule<Yorha2bUnit> {
    @Override public String id() { return "yorha_2b"; }
    @Override public String displayName() { return "Battle Android Type B"; }
    @Override public String displayNameJa() { return "\u6226\u95d8\u578b\u30a2\u30f3\u30c9\u30ed\u30a4\u30c9 B"; }
    @Override public EntityType.Builder<Yorha2bUnit> builder() { return EntityType.Builder.<Yorha2bUnit>of(Yorha2bUnit::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(10); }
    @Override public AttributeSupplier.Builder attributes() { return Yorha9sModule.attrs(80, 6, 0.32); }
    @Override public int eggPrimary() { return 0x1c2233; }
    @Override public int eggSecondary() { return 0xc9a35a; }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { Yorha2bClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { Yorha2bClient.renderers(e, this); }
}
