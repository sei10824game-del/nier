package com.nier.automata.unit.ally;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ReplicantModule extends AllyModule<ReplicantUnit> {
    private final String id;
    public ReplicantModule(String id, String en, int kit, double hp, double dmg, int egg1, int egg2) {
        super(id, en, en, (t, l) -> new ReplicantUnit(t, l, kit), 0.6f, kit == 1 ? 1.9f : 1.75f, hp, dmg, 0.32, 6, egg1, egg2);
        this.id = id;
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { ReplicantClient.layers(e, id); }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { ReplicantClient.renderers(e, id, (EntityType) type.get()); }
}
