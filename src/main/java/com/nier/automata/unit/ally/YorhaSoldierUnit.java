package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * YoRHa Type-E line soldier. Loadout picked at spawn (form): 0 = sword, 1 = spear, 2 = rifle.
 * Every loadout keeps the standard YoRHa kit: Pod machine gun and the evasive back-step.
 */
public class YorhaSoldierUnit extends AllyUnit {
    public static final int COMBO = 1, DASH = 2, FLURRY = 3, CHARGE = 4, BURST = 5, BASH = 6, POD_GUN = 7, EVADE = 8;

    public YorhaSoldierUnit(EntityType<? extends YorhaSoldierUnit> t, Level l) { super(t, l); }
    @Override protected int variants() { return 3; }
    @Override public double preferredRange() { return getForm() == 2 ? 9 : 2; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            // Type-3 sword: three alternating cuts + overhead finisher
            new Skill(COMBO, "Sword Combo", 28, 30, 0, 3.2, (u, t, i) -> {
                if (i == 6 || i == 12 || i == 18) { arc(3.2, 70, 4, 0.3, 0.05); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.3f); lunge(0.25, 0); }
                if (i == 23) { arc(3.4, 50, 8, 0.8, 0.25); fx(ParticleTypes.CRIT, at(2, 0.2, 0), 12, 0.4, 0.2); sound(SoundEvents.PLAYER_ATTACK_STRONG, 1, 0.9f); }
            }).forms(0, 0).weight(3),
            new Skill(DASH, "Dash Strike", 18, 60, 4, 10, (u, t, i) -> {
                if (i == 8) { lungeTo(t, 1.6, 0.1); fx(ParticleTypes.CLOUD, position(), 6, 0.2, 0.02); }
                if (i == 12) { arc(3, 80, 7, 0.6, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.1f); }
            }).forms(0, 0),
            // spear: rapid thrusts, then a charging impale
            new Skill(FLURRY, "Spear Flurry", 28, 40, 0, 4, (u, t, i) -> {
                if (i >= 8 && i <= 20 && (i - 8) % 4 == 0) { arc(4, 25, 3, 0.15, 0); sound(SoundEvents.TRIDENT_THROW, 0.6f, 1.5f); }
            }).forms(1, 1).weight(3),
            new Skill(CHARGE, "Spear Charge", 22, 70, 3, 10, (u, t, i) -> {
                if (i == 10) lungeTo(t, 1.5, 0.05);
                if (i == 14) { arc(4.2, 30, 9, 1.0, 0.2); sound(SoundEvents.TRIDENT_HIT, 1, 1); }
            }).forms(1, 1),
            // rifle: burst fire, bayonet bash when crowded
            new Skill(BURST, "Rifle Burst", 30, 35, 3, 18, (u, t, i) -> {
                if (i >= 8 && i <= 24 && i % 4 == 0) { Vec3 m = at(0.9, 1.35, -0.3); shoot(m, aimAt(t, m), 2.2).dmg(3).trail(ParticleTypes.CRIT).life(15);
                    fx(ParticleTypes.SMOKE, m, 2, 0.05, 0); sound(SoundEvents.CROSSBOW_SHOOT, 0.7f, 1.8f); }
            }).forms(2, 2).weight(3),
            new Skill(BASH, "Bayonet Bash", 16, 30, 0, 2.5, (u, t, i) -> {
                if (i == 9) { arc(2.6, 60, 5, 1.1, 0.2); sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 1); }
            }).forms(2, 2),
            new Skill(POD_GUN, "Pod Machine Gun", 30, 50, 2, 20, (u, t, i) -> {
                if (i >= 6 && i <= 26 && i % 2 == 0) { Vec3 p = podPos(); shoot(p, aimAt(t, p).add(random.nextGaussian() * 0.03, 0, random.nextGaussian() * 0.03), 2.5)
                    .dmg(1.5f).trail(ParticleTypes.ELECTRIC_SPARK).life(14); if (i % 4 == 0) sound(SoundEvents.DISPENSER_LAUNCH, 0.4f, 2f); }
            }).weight(2),
            new Skill(EVADE, "Evade", 10, 80, 0, 2.2, (u, t, i) -> { if (i == 1) evade(0.9); }).noTurn()
        );
    }
}
