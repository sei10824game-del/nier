package com.nier.automata;

import com.nier.automata.core.NierEvents;
import com.nier.automata.core.UnitModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(NierAutomata.MODID)
public class NierAutomata {
    public static final String MODID = "nierautomata";
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public NierAutomata() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        for (UnitModule m : Modules.ALL) {
            m.type = ENTITIES.register(m.id(), () -> m.builder().build(MODID + ":" + m.id()));
            ITEMS.register(m.id() + "_spawn_egg", () -> new net.minecraftforge.common.ForgeSpawnEggItem(() -> (EntityType) m.type.get(), m.eggPrimary(), m.eggSecondary(), new Item.Properties()));
            m.registerExtra(ENTITIES);
        }
        RegistryObject<CreativeModeTab> tab = TABS.register("units", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.nierautomata"))
                .icon(() -> ItemStack.EMPTY.isEmpty() && !Modules.ALL.isEmpty()
                        ? new ItemStack(ITEMS.getEntries().iterator().next().get()) : new ItemStack(net.minecraft.world.item.Items.IRON_SWORD))
                .displayItems((p, out) -> ITEMS.getEntries().forEach(i -> out.accept(i.get()))).build());
        ENTITIES.register(bus); ITEMS.register(bus); TABS.register(bus);
        bus.addListener((EntityAttributeCreationEvent e) -> {
            for (UnitModule m : Modules.ALL) e.put((EntityType) m.type.get(), m.attributes().build());
        });
        bus.addListener((net.minecraftforge.event.entity.SpawnPlacementRegisterEvent e) -> {
            for (UnitModule m : Modules.ALL) if (m instanceof com.nier.automata.unit.machine.MachineModule || m instanceof com.nier.automata.unit.ally.YorhaSoldierModule || m instanceof com.nier.automata.unit.ally.ResistanceModule)
                e.register((EntityType) m.type.get(), net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND, net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        (net.minecraft.world.entity.SpawnPlacements.SpawnPredicate) (t, lv, reason, pos, rnd) -> lv.getDifficulty() != net.minecraft.world.Difficulty.PEACEFUL
                                && net.minecraft.world.entity.Mob.checkMobSpawnRules((EntityType) t, lv, reason, pos, rnd),
                        net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation.OR);
        });
        MinecraftForge.EVENT_BUS.register(new NierEvents());
        net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> () -> ClientSetup.init(bus));
    }
}
