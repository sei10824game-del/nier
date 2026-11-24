package com.nier.automata.unit.machine.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nier.automata.NierAutomata;
import com.nier.automata.client.NierModel;
import com.nier.automata.client.NierRenderer;
import com.nier.automata.unit.machine.MachineModule;
import com.nier.automata.unit.machine.MachineShot;
import com.nier.automata.unit.machine.MachineUnit;
import com.nier.automata.unit.machine.MachineVariant;
import com.nier.automata.unit.machine.MachineVariant.Arch;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

/** Client-only registration for every machine module (layers once per archetype, renderer per variant). */
public final class MachineClient {
    private MachineClient() {}
    private static final Map<Arch, ModelLayerLocation> LAYERS = new EnumMap<>(Arch.class);
    private static boolean shotDone;

    private static ModelLayerLocation layer(Arch a) {
        return LAYERS.computeIfAbsent(a, x -> new ModelLayerLocation(new ResourceLocation(NierAutomata.MODID, "machine_" + x.name().toLowerCase()), "main"));
    }

    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        // re-registering the same location just replaces it, so calling this once per module is harmless
        e.registerLayerDefinition(layer(Arch.STUBBY), MachineGeo::stubby);
        e.registerLayerDefinition(layer(Arch.BIPED), MachineGeo::biped);
        e.registerLayerDefinition(layer(Arch.FLYER), MachineGeo::flyer);
        e.registerLayerDefinition(layer(Arch.GOLIATH), MachineGeo::goliath);
        e.registerLayerDefinition(layer(Arch.TANK), MachineGeo::tank);
        e.registerLayerDefinition(layer(Arch.SPHERE), MachineGeo::sphere);
        e.registerLayerDefinition(layer(Arch.MULTILEG), MachineGeo::multileg);
        e.registerLayerDefinition(layer(Arch.QUAD), MachineGeo::quad);
        e.registerLayerDefinition(layer(Arch.MONSTER), MachineGeo::monster);
        e.registerLayerDefinition(layer(Arch.EXPLODER), MachineGeo::exploder);
    }

    public static void renderers(MachineModule m, EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(m.type.get(), ctx -> new Renderer(ctx, m.v));
        if (!shotDone) { shotDone = true; e.registerEntityRenderer(MachineShot.TYPE.get(), NoopRenderer::new); }
    }

    private static NierModel<MachineUnit> model(MachineVariant v, EntityRendererProvider.Context ctx) {
        var root = ctx.bakeLayer(layer(v.arch()));
        return switch (v.arch()) {
            case STUBBY -> new StubbyModel(root);
            case BIPED -> new BipedModel(root);
            case FLYER -> new FlyerModel(root);
            case GOLIATH -> new GoliathModel(root);
            case TANK -> new TankModel(root);
            case SPHERE -> new SphereModel(root);
            case MULTILEG -> new MultiLegModel(root);
            case QUAD -> new QuadModel(root);
            case MONSTER -> new MonsterModel(root);
            case EXPLODER -> new ExploderModel(root);
        };
    }

    /** Scales the shared archetype model to the variant size; form 1 = overheated texture. */
    static final class Renderer extends NierRenderer<MachineUnit> {
        private final float scale;
        Renderer(EntityRendererProvider.Context ctx, MachineVariant v) {
            super(ctx, 0.4f * v.scale() * (v.arch() == Arch.TANK || v.arch() == Arch.GOLIATH ? 1.6f : 1f), textures(v), model(v, ctx));
            scale = v.scale();
        }
        private static java.util.function.Function<MachineUnit, ResourceLocation> textures(MachineVariant v) {
            String base = "textures/entity/machine_" + v.arch().name().toLowerCase();
            ResourceLocation normal = new ResourceLocation(NierAutomata.MODID, base + v.skin().suffix + ".png");
            ResourceLocation burn = new ResourceLocation(NierAutomata.MODID, base + "_burn.png");
            boolean canBurn = v.arch() == Arch.STUBBY || v.arch() == Arch.SPHERE;
            return e -> canBurn && e.getForm() == 1 ? burn : normal;
        }
        @Override protected void scale(MachineUnit e, PoseStack ps, float pt) { ps.scale(scale, scale, scale); }
    }
}
