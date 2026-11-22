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
 * Pacifist machine village leader, fighting on the android side to defend the village: Pound (two-fisted
 * hammer blow with shockwave), Tackle (body charge), Rescue (heals and shields nearby allies, clears their debuffs).
 * Faction ANDROID by design (ally of YoRHa / Resistance).
 */
public class PascalUnit extends AllyUnit {
    public static final int POUND = 1, TACKLE = 2, RESCUE = 3;

    public PascalUnit(EntityType<? extends PascalUnit> t, Level l) { super(t, l); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(POUND, "Pound", 28, 35, 0, 3, (u, t, i) -> {
                if (i == 16) { arc(3, 60, 7, 0.4, 0.5); burst(at(1.6, 0, 0), 2.5, 3, 0.8, 0.3);
                    fx(ParticleTypes.EXPLOSION, at(1.6, 0.1, 0), 1, 0, 0); sound(SoundEvents.IRON_GOLEM_ATTACK, 1, 0.8f); }
            }).weight(3),
            new Skill(TACKLE, "Tackle", 26, 60, 2.5, 10, (u, t, i) -> {
                if (i == 10) { lungeTo(t, 1.3, 0.1); sound(SoundEvents.RAVAGER_STEP, 1, 1.4f); }
                if (i >= 11 && i <= 16 && arc(1.8, 70, 6, 1.4, 0.35) > 0) fx(ParticleTypes.CRIT, at(1, 1, 0), 10, 0.3, 0.2);
            }),
            new Skill(RESCUE, "Rescue", 40, 240, 0, 30, (u, t, i) -> {
                if (i % 5 == 0) fx(ParticleTypes.HEART, position().add(0, 1.8, 0), 1, 0.3, 0);
                if (i == 24) {
                    for (LivingEntity a : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10), e -> e == this || isAlliedTo(e))) {
                        a.heal(10); a.removeEffect(MobEffects.POISON); a.removeEffect(MobEffects.WITHER); a.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        a.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 0)); fx(ParticleTypes.HAPPY_VILLAGER, a.position().add(0, 1, 0), 10, 0.4, 0);
                    }
                    sound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.5f, 0.8f);
                }
            }).when(u -> u.getHealth() < u.getMaxHealth() * 0.8f).noTurn()
        );
    }
}
