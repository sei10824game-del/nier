package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * YoRHa Type E executioner model "2E". Light small sword + heavy great sword (the light/heavy weapon set),
 * launcher, spinning heavy charge, perfect-evade counter, Pod machine gun, Pod Program laser and the
 * YoRHa self-destruct (once per life, below 20% HP; the black box reboots her at 40%).
 */
public class Yorha2EUnit extends AllyUnit {
    public static final int LIGHT = 1, HEAVY = 2, LAUNCH = 3, SPIN = 4, POD_GUN = 5, LASER = 6, COUNTER = 7, SELF_DESTRUCT = 8;
    private boolean destructed;
    private static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1f, 0.25f, 0.2f), 1.6f);

    public Yorha2EUnit(EntityType<? extends Yorha2EUnit> t, Level l) { super(t, l); }

    @Override protected @Nullable Skill forcedSkill() {
        return !destructed && getHealth() < getMaxHealth() * 0.2f ? skillById(SELF_DESTRUCT) : null;
    }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(LIGHT, "Light Combo", 30, 25, 0, 3.2, (u, t, i) -> {
                if (i == 5 || i == 10 || i == 15) { arc(3.2, 70, 4, 0.25, 0.05); lunge(0.3, 0); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.5f); }
                if (i == 22) { arc(3.4, 60, 7, 0.9, 0.3); fx(ParticleTypes.CRIT, at(2, 0.3, 0), 14, 0.4, 0.2); sound(SoundEvents.PLAYER_ATTACK_STRONG, 1, 1); }
            }).weight(3),
            new Skill(HEAVY, "Great Sword Cleave", 34, 50, 0, 3.8, (u, t, i) -> {
                if (i == 12) { arc(3.8, 90, 9, 0.9, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.7f); }
                if (i == 26) { arc(3.8, 45, 13, 1.2, 0.35); fx(ParticleTypes.EXPLOSION, at(2.2, 0.2, 0), 1, 0, 0); sound(SoundEvents.ANVIL_LAND, 0.8f, 0.6f); }
            }).weight(2),
            new Skill(LAUNCH, "Launcher", 20, 60, 0, 3, (u, t, i) -> {
                if (i == 12) { arc(3, 50, 6, 0.1, 1.0); sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 1.4f); setDeltaMovement(0, 0.6, 0); hurtMarked = true; }
            }),
            new Skill(SPIN, "Spinning Slash", 30, 90, 0, 3.5, (u, t, i) -> {
                if (i >= 8 && i <= 24 && i % 4 == 0) { arc(3.5, 180, 4, 0.4, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 1.8f); }
                if (i == 8) lungeTo(t, 0.6, 0);
                if (i <= 24) spin(i, 8, 45f);
            }).noTurn(),
            new Skill(POD_GUN, "Pod Machine Gun", 30, 45, 2, 22, (u, t, i) -> {
                if (i >= 6 && i <= 26 && i % 2 == 0) { Vec3 p = podPos(); shoot(p, aimAt(t, p).add(random.nextGaussian() * 0.03, 0, random.nextGaussian() * 0.03), 2.6)
                    .dmg(1.8f).trail(ParticleTypes.ELECTRIC_SPARK).life(15); if (i % 4 == 0) sound(SoundEvents.DISPENSER_LAUNCH, 0.4f, 2f); }
            }).weight(2),
            new Skill(LASER, "Pod Program: Laser", 50, 160, 4, 24, (u, t, i) -> {
                Vec3 p = podPos();
                if (i < 20 && i % 3 == 0) fx(RED, p, 4, 0.3, 0);
                if (i == 20) sound(SoundEvents.BEACON_ACTIVATE, 1, 1.6f);
                if (i >= 20 && i <= 44 && i % 2 == 0) beam(p, aimAt(t, p), 24, 2.5f, RED);
            }),
            new Skill(COUNTER, "Evade Counter", 18, 70, 0, 2.4, (u, t, i) -> {
                if (i == 1) evade(1.0);
                if (i == 9) lungeTo(t, 1.3, 0);
                if (i == 12) { arc(3, 80, 8, 0.7, 0.2); fx(ParticleTypes.ENCHANTED_HIT, at(1.5, 1, 0), 10, 0.4, 0.1); }
            }),
            new Skill(SELF_DESTRUCT, "Self-Destruct", 40, 0, 0, 99, (u, t, i) -> {
                if (i % 4 == 0) { fx(RED, position().add(0, 1, 0), 10, 0.6, 0); sound(SoundEvents.NOTE_BLOCK_BIT.value(), 1, 1 + i / 40f); }
                if (i == 36) { destructed = true; burst(position().add(0, 1, 0), 5, 18, 1.6, 0.6);
                    fx(ParticleTypes.EXPLOSION_EMITTER, position(), 1, 0, 0); sound(SoundEvents.GENERIC_EXPLODE, 2, 0.8f); setHealth(getMaxHealth() * 0.4f); }
            }).noTarget().noTurn()
        );
    }

    @Override public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag t) { super.addAdditionalSaveData(t); t.putBoolean("Destructed", destructed); }
    @Override public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag t) { super.readAdditionalSaveData(t); destructed = t.getBoolean("Destructed"); }
}
