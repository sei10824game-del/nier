package com.nier.automata.unit.yorha;

import com.nier.automata.NierAutomata;
import com.nier.automata.client.NierRenderer;
import com.nier.automata.core.UnitModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class Yorha2bCopyModule extends UnitModule<Yorha2bCopyUnit> {
    @Override public String id() { return "yorha_2b_copy"; }
    @Override public String displayName() { return "2B Copy"; }
    @Override public EntityType.Builder<Yorha2bCopyUnit> builder() { return EntityType.Builder.<Yorha2bCopyUnit>of(Yorha2bCopyUnit::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(10); }
    @Override public AttributeSupplier.Builder attributes() { return Yorha9sModule.attrs(70, 6, 0.32); }
    @Override public int eggPrimary() { return 0x1c2233; }
    @Override public int eggSecondary() { return 0xd03040; }
    @Override public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions e) { Yorha2bClient.layers(e); }
    @Override public void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
        ResourceLocation t0 = Yorha2bClient.tex("yorha_2b_copy"), t1 = Yorha2bClient.tex("yorha_2b_copy_overload");
        e.registerEntityRenderer(type.get(), ctx -> new NierRenderer<>(ctx, 0.5f, u -> u.getForm() == 1 ? t1 : t0, new Yorha2bModel(ctx.bakeLayer(Yorha2bClient.LAYER))));
    }
}
