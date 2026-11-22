package com.nier.automata.unit.yorha;

import com.nier.automata.core.UnitModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class A2Module extends UnitModule<A2Unit> {
    @Override public String id() { return "a2"; }
    @Override public String displayName() { return "Attacker Android Type A"; }
    @Override public String displayNameJa() { return "\u653b\u6483\u578b\u30a2\u30f3\u30c9\u30ed\u30a4\u30c9 A"; }
    @Override public EntityType.Builder<A2Unit> builder() { return EntityType.Builder.<A2Unit>of(A2Unit::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(10); }
    @Override public AttributeSupplier.Builder attributes() { return Yorha9sModule.attrs(90, 5, 0.33); }
    @Override public int eggPrimary() { return 0x5a2e1e; }
    @Override public int eggSecondary() { return 0xd94030; }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { A2Client.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { A2Client.renderers(e, this); }
}
