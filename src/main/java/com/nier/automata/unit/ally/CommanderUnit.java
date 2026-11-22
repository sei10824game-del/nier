package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * YoRHa Commander: duelist with a sabre (she holds the Bunker line herself in the final defence).
 * Sabre combo, lunging thrust, riposte, and the "Glory to Mankind" rally order that empowers nearby androids.
 */
public class CommanderUnit extends AllyUnit {
    public static final int COMBO = 1, LUNGE = 2, RIPOSTE = 3, RALLY = 4;

    public CommanderUnit(EntityType<? extends CommanderUnit> t, Level l) { super(t, l); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(COMBO, "Sabre Combo", 26, 25, 0, 3, (u, t, i) -> {
                if (i == 6 || i == 12) { arc(3, 65, 5, 0.3, 0.05); lunge(0.3, 0); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.6f); }
                if (i == 19) { arc(3.2, 30, 8, 0.8, 0.1); sound(SoundEvents.PLAYER_ATTACK_STRONG, 1, 1.3f); }
            }).weight(3),
            new Skill(LUNGE, "Lunging Thrust", 20, 50, 3, 9, (u, t, i) -> {
                if (i == 10) lungeTo(t, 1.5, 0.05);
                if (i == 13) { arc(3.5, 25, 9, 0.9, 0.1); fx(ParticleTypes.CRIT, at(2, 1.3, 0), 10, 0.2, 0.3); }
            }),
            new Skill(RIPOSTE, "Riposte", 18, 70, 0, 2.5, (u, t, i) -> {
                if (i == 1) evade(0.7);
                if (i == 9) lungeTo(t, 1.2, 0);
                if (i == 11) { arc(3, 50, 10, 0.6, 0.15); fx(ParticleTypes.ENCHANTED_HIT, at(1.4, 1, 0), 10, 0.3, 0.1); }
            }),
            new Skill(RALLY, "Rally: Glory to Mankind", 30, 400, 0, 30, (u, t, i) -> {
                if (i == 14) {
                    for (LivingEntity a : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(16), this::isAlliedTo)) {
                        a.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 1)); a.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
                        fx(ParticleTypes.END_ROD, a.position().add(0, 2, 0), 8, 0.3, 0.02);
                    }
                    addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0));
                    sound(SoundEvents.RAID_HORN.value(), 1.5f, 1.4f);
                }
            }).noTurn()
        );
    }
}
