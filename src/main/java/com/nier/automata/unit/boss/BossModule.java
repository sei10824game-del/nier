package com.nier.automata.unit.boss;

import com.nier.automata.core.UnitModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.DeferredRegister;

/** Module boilerplate shared by every boss; also registers the shared boss_orb projectile once. */
public abstract class BossModule<T extends BossUnit> extends UnitModule<T> {
    private final String id, name, ja;
    private final EntityType.EntityFactory<T> factory;
    private final float w, h;
    private final double hp, atk, speed, armor;
    private final int egg1, egg2;

    protected BossModule(String id, String name, String ja, EntityType.EntityFactory<T> factory, float w, float h,
                         double hp, double atk, double speed, double armor, int egg1, int egg2) {
        this.id = id; this.name = name; this.ja = ja; this.factory = factory; this.w = w; this.h = h;
        this.hp = hp; this.atk = atk; this.speed = speed; this.armor = armor; this.egg1 = egg1; this.egg2 = egg2;
    }

    @Override public String id() { return id; }
    @Override public String displayName() { return name; }
    @Override public String displayNameJa() { return ja; }
    @Override public int eggPrimary() { return egg1; }
    @Override public int eggSecondary() { return egg2; }
    @Override public EntityType.Builder<T> builder() {
        return EntityType.Builder.of(factory, MobCategory.MONSTER).sized(w, h).fireImmune().clientTrackingRange(16);
    }
    @Override public AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, hp).add(Attributes.ATTACK_DAMAGE, atk)
                .add(Attributes.MOVEMENT_SPEED, speed).add(Attributes.ARMOR, armor)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.FOLLOW_RANGE, 48);
    }
    @Override public void registerExtra(DeferredRegister<EntityType<?>> reg) { BossOrb.register(reg); }

    @Override public final void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
        BossClient.orbRenderer(e);
        renderers(e);
    }
    /** Client only. */
    protected abstract void renderers(EntityRenderersEvent.RegisterRenderers e);
}
