package com.nier.automata.unit.ally;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class YorhaBetrayerModule extends AllyModule<YorhaBetrayerUnit> {
    private final String id;
    public YorhaBetrayerModule(String id, String en, int loadout, int egg) {
        super(id, en, en, (t, l) -> new YorhaBetrayerUnit(t, l, loadout), 0.6f, 1.85f, 130, 7, 0.3, 10, 0x15151a, egg);
        this.id = id;
    }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { YorhaSoldierClient.layers(e, id); }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) { YorhaSoldierClient.renderers(e, id, (EntityType) type.get()); }
}
