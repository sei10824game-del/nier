package com.nier.automata.unit.yorha;

import com.nier.automata.core.Skill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

/**
 * Attacker-type android (A). Blade, greatsword, combat spear, heavy fists, dash strike, pod gun, evade counter.
 * Berserk (form 1, forced below 60% HP): red glow, x1.6 damage, faster, drains 1 HP/s for 20 s.
 */
public class A2Unit extends AndroidUnit {
    public static final int BLADE = 1, GREAT = 2, SPEAR = 3, FISTS = 4, DASH = 5, POD_GUN = 6, EVADE = 7, BERSERK = 8;
    private static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1f, 0.05f, 0.05f), 1.2f);
    private int berserkTicks, berserkCd;

    public A2Unit(EntityType<? extends A2Unit> type, Level level) { super(type, level); }

    @Override public float damageMult() { return getForm() == 1 ? 1.6f : 1f; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            // blade: 3 fast slashes + spinning kick
            new Skill(BLADE, "blade_combo", 24, 20, 0, 3.5, (u, t, k) -> {
                if (k == 4 || k == 9 || k == 14) arc(3, 65, 4.5, 0.15);
                if (k == 20) arc(2.5, 90, 4, 1.2);
            }).weight(4),
            // greatsword: spin-up then two heavy cleaves
            new Skill(GREAT, "greatsword_combo", 36, 70, 0, 4, (u, t, k) -> {
                if (k == 12) { aoe(position(), 3.8, 8, 0.2); ring(position(), 2.5, ParticleTypes.SWEEP_ATTACK); }
                if (k == 22) lunge(0.6, 0);
                if (k == 25) { arc(4.2, 40, 11, 0.8); sl().sendParticles(ParticleTypes.CRIT, getX() + fwd().x * 2, getY() + 0.1, getZ() + fwd().z * 2, 15, 0.4, 0.1, 0.4, 0.2); }
            }).weight(3),
            // combat spear: whirl (4 hits) then thrust
            new Skill(SPEAR, "spear_whirl", 30, 70, 0, 5, (u, t, k) -> {
                if (k == 8 || k == 12 || k == 16 || k == 20) { aoe(position(), 3.5, 3.5, 0); if (k == 8) ring(position(), 3, ParticleTypes.SWEEP_ATTACK); }
                if (k == 24) lunge(0.8, 0);
                if (k == 25) arc(4.5, 25, 8, 1.1);
            }).weight(3),
            // heavy-grip fists: jab, cross, hook, launching uppercut
            new Skill(FISTS, "fist_combo", 26, 40, 0, 2.8, (u, t, k) -> {
                if (k == 4 || k == 8 || k == 12) arc(2.5, 50, 3.5, 0.1);
                if (k == 19) { if (arc(2.8, 50, 7, 0) > 0 && t != null) t.setDeltaMovement(t.getDeltaMovement().add(0, 0.8, 0));
                    sound(SoundEvents.IRON_GOLEM_ATTACK, 1, 1.2f); }
            }).weight(3),
            new Skill(DASH, "dash_strike", 16, 50, 4, 10, (u, t, k) -> {
                if (k == 3) { lunge(1.7, 0.05); iframes = 4; }
                if (k >= 6 && k <= 8) arc(2.5, 80, 3.5, 0.3);
                if (k == 4) sl().sendParticles(ParticleTypes.CLOUD, getX(), getY() + 0.5, getZ(), 6, 0.2, 0.2, 0.2, 0.01);
            }).weight(2),
            AndroidUnit.podGun(POD_GUN, 1.5f),
            AndroidUnit.evade(EVADE),
            new Skill(BERSERK, "berserk", 30, 0, 0, 99, (u, t, k) -> {
                iframes = 2; setDeltaMovement(0, getDeltaMovement().y, 0);
                if (k < 14) sl().sendParticles(RED, getX(), getY() + 1, getZ(), 4, 0.4, 0.7, 0.4, 0);
                if (k == 14) { setForm(1); berserkTicks = 400; getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4);
                    sound(SoundEvents.RAVAGER_ROAR, 1, 1.3f); ring(position(), 2, ParticleTypes.LAVA); ring(position(), 3, RED); }
            }).noTarget().noTurn()
        );
    }

    @Nullable @Override protected Skill forcedSkill() {
        return getForm() == 0 && berserkCd == 0 && getHealth() < getMaxHealth() * 0.6f ? skillById(BERSERK) : null;
    }

    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            if (getForm() == 1 && random.nextInt(2) == 0) level().addParticle(RED, getRandomX(0.6), getY() + random.nextDouble() * 1.9, getRandomZ(0.6), 0, 0, 0);
            return;
        }
        if (berserkCd > 0) berserkCd--;
        if (getForm() == 1) {
            if (tickCount % 20 == 0 && getHealth() > 2) setHealth(getHealth() - 1);   // berserk HP drain
            if (--berserkTicks <= 0) { setForm(0); berserkCd = 600; getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33); }
        }
    }

    @Override public void addAdditionalSaveData(CompoundTag t) { super.addAdditionalSaveData(t); t.putInt("Berserk", berserkTicks); t.putInt("BerserkCd", berserkCd); }
    @Override public void readAdditionalSaveData(CompoundTag t) { super.readAdditionalSaveData(t); berserkTicks = t.getInt("Berserk"); berserkCd = t.getInt("BerserkCd"); }
}
