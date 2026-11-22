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
import net.minecraft.world.phys.Vec3;

/** YoRHa Operator (6O / 21O): non-combat model; fights only through a support Pod - light gun, Repair, Shield. Keeps distance. */
public class OperatorUnit extends AllyUnit {
    public static final int POD_GUN = 1, REPAIR = 2, SHIELD = 3, EVADE = 4;

    public OperatorUnit(EntityType<? extends OperatorUnit> t, Level l) { super(t, l); }
    @Override public double preferredRange() { return 8; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(POD_GUN, "Pod Machine Gun", 30, 40, 0, 18, (u, t, i) -> {
                if (i >= 8 && i <= 26 && i % 3 == 0) { Vec3 p = podPos(); shoot(p, aimAt(t, p), 2.4).dmg(1.2f).trail(ParticleTypes.ELECTRIC_SPARK).life(14);
                    sound(SoundEvents.DISPENSER_LAUNCH, 0.35f, 2f); }
            }).weight(3),
            new Skill(REPAIR, "Pod Program: Repair", 36, 200, 0, 40, (u, t, i) -> {
                if (i % 4 == 0) fx(ParticleTypes.HAPPY_VILLAGER, podPos(), 3, 0.3, 0);
                if (i == 20) { healAllies(10, 8); sound(SoundEvents.BEACON_POWER_SELECT, 1, 1.5f); }
            }).when(u -> u.getHealth() < u.getMaxHealth() * 0.7f || u.getTarget() != null).noTurn(),
            new Skill(SHIELD, "Pod Program: Shield", 30, 300, 0, 40, (u, t, i) -> {
                if (i == 16) {
                    for (LivingEntity a : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(8), e -> e == this || isAlliedTo(e))) {
                        a.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1)); fx(ParticleTypes.ENCHANT, a.position().add(0, 1, 0), 20, 0.5, 0.3);
                    }
                    sound(SoundEvents.ARMOR_EQUIP_DIAMOND, 1, 1.3f);
                }
            }).noTurn(),
            new Skill(EVADE, "Evade", 10, 40, 0, 3, (u, t, i) -> { if (i == 1) evade(1.0); }).noTurn().weight(2)
        );
    }
}
