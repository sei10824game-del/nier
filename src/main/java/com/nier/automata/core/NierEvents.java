package com.nier.automata.core;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Makes every non-NieR hostile-capable mob attack NieR units. */
public class NierEvents {
    @SubscribeEvent
    public void onJoin(EntityJoinLevelEvent e) {
        if (e.getLevel().isClientSide) return;
        if (e.getEntity() instanceof PathfinderMob m && !(m instanceof NierUnit)
                && m.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
            m.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(m, NierUnit.class, true));
        }
    }
}
