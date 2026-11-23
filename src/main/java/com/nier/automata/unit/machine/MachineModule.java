package com.nier.automata.unit.machine;

import com.nier.automata.core.UnitModule;
import com.nier.automata.unit.machine.MachineVariant.Arch;
import com.nier.automata.unit.machine.MachineVariant.Skin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.DeferredRegister;

/** One module per machine variant; all variants of an {@link Arch} share unit class, model and texture set. */
public class MachineModule extends UnitModule<MachineUnit> {
    public final MachineVariant v;
    public MachineModule(MachineVariant v) { this.v = v; }

    @Override public String id() { return v.id(); }
    @Override public String displayName() { return v.name(); }

    private MachineUnit create(EntityType<MachineUnit> t, Level l) {
        return switch (v.arch()) {
            case STUBBY -> new StubbyUnit(t, l, v);
            case BIPED -> new BipedUnit(t, l, v);
            case FLYER -> new FlyerUnit(t, l, v);
            case GOLIATH -> new GoliathUnit(t, l, v);
            case TANK -> new TankUnit(t, l, v);
            case SPHERE -> new SphereUnit(t, l, v);
            case MULTILEG -> new MultiLegUnit(t, l, v);
            case QUAD -> new QuadUnit(t, l, v);
        };
    }
    @Override public EntityType.Builder<MachineUnit> builder() {
        EntityFactory<MachineUnit> f = this::create;
        return EntityType.Builder.of(f, MobCategory.MONSTER).sized(v.width(), v.height()).clientTrackingRange(v.scale() > 2.5f ? 12 : 8);
    }
    @Override public AttributeSupplier.Builder attributes() {
        AttributeSupplier.Builder b = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, v.hp()).add(Attributes.ATTACK_DAMAGE, v.damage()).add(Attributes.MOVEMENT_SPEED, v.speed())
                .add(Attributes.ARMOR, v.armor()).add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, Math.min(1.0, Math.max(0, (v.scale() - 1) * 0.4)));
        if (v.arch() == Arch.FLYER) b.add(Attributes.FLYING_SPEED, v.speed() * 1.6);
        return b;
    }
    @Override public int eggPrimary() { return v.skin() == Skin.GOLD ? 0xdeb03a : v.skin() == Skin.HACKED ? 0x807868 : 0x92744f; }
    @Override public int eggSecondary() { return v.skin() == Skin.GOLD ? 0x705420 : v.skin() == Skin.HACKED ? 0x46e6ff : 0xff3020; }
    @Override public void registerExtra(DeferredRegister<EntityType<?>> reg) { MachineShot.register(reg); }

    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        com.nier.automata.unit.machine.client.MachineClient.layers(e);
    }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
        com.nier.automata.unit.machine.client.MachineClient.renderers(this, e);
    }
}
