package com.nier.automata.unit.yorha;

import com.nier.automata.core.UnitModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class Yorha9sModule extends UnitModule<Yorha9sUnit> {
    @Override public String id() { return "yorha_9s"; }
    @Override public String displayName() { return "Scanner Android Type S"; }
    @Override public String displayNameJa() { return "\u30b9\u30ad\u30e3\u30ca\u30fc\u578b\u30a2\u30f3\u30c9\u30ed\u30a4\u30c9 S"; }
    @Override public EntityType.Builder<Yorha9sUnit> builder() { return EntityType.Builder.<Yorha9sUnit>of(Yorha9sUnit::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(10); }
    @Override public AttributeSupplier.Builder attributes() { return attrs(60, 4, 0.34); }
    @Override public int eggPrimary() { return 0x6b7480; }
    @Override public int eggSecondary() { return 0x3fd0e0; }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { Yorha9sClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { Yorha9sClient.renderers(e, this); }

    /** Shared android attribute set. */
    static AttributeSupplier.Builder attrs(double hp, double armor, double speed) {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, hp).add(Attributes.ARMOR, armor).add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.KNOCKBACK_RESISTANCE, 0.4);
    }
}
