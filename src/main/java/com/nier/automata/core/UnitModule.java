package com.nier.automata.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * One module per unit (or tightly-related group). Add the instance to {@link com.nier.automata.Modules}.
 * Client-only code (models/renderers) MUST live in separate classes referenced only from the two client hooks.
 */
public abstract class UnitModule<T extends NierUnit> {
    public RegistryObject<EntityType<T>> type;       // filled at registration
    public abstract String id();                      // registry name, e.g. "yorha_2b"
    public abstract String displayName();             // English name for lang
    public String displayNameJa() { return displayName(); }
    public abstract EntityType.Builder<T> builder();  // EntityType.Builder.of(X::new, MobCategory.MONSTER).sized(..)
    public abstract AttributeSupplier.Builder attributes();
    public int eggPrimary() { return 0x1a1a1a; }
    public int eggSecondary() { return 0xd8d0c0; }
    /** Register extra entity types (projectiles etc). Names must be prefixed with id(). */
    public void registerExtra(DeferredRegister<EntityType<?>> reg) {}
    // ---- client hooks (called only on the client) ----
    public abstract void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e);
    public abstract void registerRenderers(EntityRenderersEvent.RegisterRenderers e);
}
