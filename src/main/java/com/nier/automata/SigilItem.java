package com.nier.automata;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/** Boss summoning sigil: right-click a block to call the boss (consumed unless creative). */
public class SigilItem extends Item {
    private final Supplier<EntityType<? extends Mob>> type;
    public SigilItem(Supplier<EntityType<? extends Mob>> type, Properties p) { super(p); this.type = type; }

    @Override public InteractionResult useOn(UseOnContext c) {
        if (!(c.getLevel() instanceof ServerLevel lv)) return InteractionResult.SUCCESS;
        BlockPos pos = c.getClickedPos().relative(c.getClickedFace());
        Mob m = type.get().spawn(lv, c.getItemInHand(), c.getPlayer(), pos, MobSpawnType.SPAWN_EGG, true, false);
        if (m != null && (c.getPlayer() == null || !c.getPlayer().getAbilities().instabuild)) c.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}
