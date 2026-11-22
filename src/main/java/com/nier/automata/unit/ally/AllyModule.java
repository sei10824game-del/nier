package com.nier.automata.unit.ally;

import com.nier.automata.core.UnitModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** Shared boilerplate for the android roster modules. */
public abstract class AllyModule<T extends AllyUnit> extends UnitModule<T> {
    private final String id, en, ja;
    private final EntityType.EntityFactory<T> factory;
    private final float w, h;
    private final double hp, dmg, speed, armor;
    private final int egg1, egg2;

    protected AllyModule(String id, String en, String ja, EntityType.EntityFactory<T> factory, float w, float h,
                         double hp, double dmg, double speed, double armor, int egg1, int egg2) {
        this.id = id; this.en = en; this.ja = ja; this.factory = factory; this.w = w; this.h = h;
        this.hp = hp; this.dmg = dmg; this.speed = speed; this.armor = armor; this.egg1 = egg1; this.egg2 = egg2;
    }
    @Override public String id() { return id; }
    @Override public String displayName() { return en; }
    @Override public String displayNameJa() { return ja; }
    @Override public EntityType.Builder<T> builder() { return EntityType.Builder.of(factory, MobCategory.CREATURE).sized(w, h).clientTrackingRange(10); }
    @Override public AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, hp).add(Attributes.ATTACK_DAMAGE, dmg)
                .add(Attributes.MOVEMENT_SPEED, speed).add(Attributes.ARMOR, armor).add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.FLYING_SPEED, speed * 1.5).add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
    }
    @Override public int eggPrimary() { return egg1; }
    @Override public int eggSecondary() { return egg2; }
}
