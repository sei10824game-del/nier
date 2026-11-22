package com.nier.automata.unit.yorha;

import com.nier.automata.core.Skill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Battle-type android (B). Small sword + greatsword, support pod (gun / laser / hammer), evade counter.
 * Below 25% HP: forced self-destruct, then fights on in the scorched "Overload" form (form 1).
 */
public class Yorha2bUnit extends AndroidUnit {
    public static final int LIGHT = 1, HEAVY = 2, AERIAL = 3, TREATY = 4, POD_GUN = 5, POD_LASER = 6, POD_HAMMER = 7, EVADE = 8, SELF_DESTRUCT = 9;
    private Vec3 laserDir = Vec3.ZERO;

    public Yorha2bUnit(EntityType<? extends Yorha2bUnit> type, Level level) { super(type, level); }

    @Override public float damageMult() { return getForm() == 1 ? 1.35f : 1f; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            // small sword: 3 slashes, last one a stepping diagonal cut
            new Skill(LIGHT, "light_combo", 26, 30, 0, 3.5, (u, t, k) -> {
                if (k == 6 || k == 12) arc(3, 60, 5, 0.2);
                if (k == 18) lunge(0.5, 0);
                if (k == 20) arc(3.5, 70, 7, 0.6);
            }).weight(4),
            // greatsword: overhead cleave then full spin
            new Skill(HEAVY, "heavy_combo", 36, 70, 0, 4, (u, t, k) -> {
                if (k == 4) lunge(0.3, 0);
                if (k == 14) { arc(4, 45, 10, 0.4); sl().sendParticles(ParticleTypes.CRIT, getX() + fwd().x * 2, getY() + 0.1, getZ() + fwd().z * 2, 15, 0.4, 0.1, 0.4, 0.2); }
                if (k == 26) { aoe(position(), 4, 9, 0.3); ring(position(), 3, ParticleTypes.SWEEP_ATTACK); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.6f); }
            }).weight(3),
            // aerial: jump-launcher, two air cuts, plunge
            new Skill(AERIAL, "aerial_combo", 34, 100, 0, 5, (u, t, k) -> {
                if (k == 2) setDeltaMovement(getDeltaMovement().x, 0.75, getDeltaMovement().z);
                if (k == 8 && t != null && distanceTo(t) < 4) t.setDeltaMovement(t.getDeltaMovement().add(0, 0.6, 0));
                if (k == 9 || k == 15) { setDeltaMovement(getDeltaMovement().multiply(1, 0, 1).add(0, 0.12, 0)); arc(3.5, 80, 5, 0); }
                if (k == 20) setDeltaMovement(fwd().x * 0.3, -1.3, fwd().z * 0.3);
                if (k == 26) { fallDistance = 0; aoe(position(), 3.5, 9, 0.6); ring(position(), 2.5, ParticleTypes.CLOUD); sound(SoundEvents.GENERIC_EXPLODE, 0.5f, 1.6f); }
            }).weight(2),
            // mixed weapon set: small, great, small, great thrust
            new Skill(TREATY, "treaty_combo", 40, 80, 0, 4, (u, t, k) -> {
                if (k == 6 || k == 21) arc(3, 60, 4, 0.2);
                if (k == 13) arc(4, 80, 7, 0.5);
                if (k == 28) lunge(1.0, 0);
                if (k == 30) arc(5, 25, 9, 1.0);
            }).weight(3),
            AndroidUnit.podGun(POD_GUN, 1.5f),
            // pod laser: charge 20 ticks, then sweeping beam
            new Skill(POD_LASER, "pod_laser", 56, 200, 4, 24, (u, t, k) -> {
                Vec3 p = podPos();
                if (k < 20) { sl().sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 2, 0.3, 0.3, 0.3, 0.05); if (k == 0) sound(SoundEvents.BEACON_ACTIVATE, 1, 1.6f); }
                if (k == 18 && t != null) laserDir = t.getEyePosition().subtract(p).normalize();
                if (k >= 20 && k <= 44 && k % 2 == 0) {
                    if (t != null) laserDir = laserDir.lerp(t.getEyePosition().subtract(p).normalize(), 0.25).normalize();
                    beam(p, laserDir, 24, 2.5f); if (k % 6 == 2) sound(SoundEvents.BEACON_AMBIENT, 1, 2f);
                }
            }),
            // pod hammer: pod rises and slams a shockwave ring in front
            new Skill(POD_HAMMER, "pod_hammer", 30, 140, 0, 6, (u, t, k) -> {
                if (k < 14) { Vec3 p = podPos(); sl().sendParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y + k * 0.05, p.z, 1, 0.1, 0.1, 0.1, 0); }
                if (k == 16) { Vec3 c = position().add(fwd().scale(1.8));
                    aoe(c, 5, 8, 0.9); ring(c, 2, ParticleTypes.EXPLOSION); ring(c, 4, ParticleTypes.CLOUD);
                    sound(SoundEvents.ANVIL_LAND, 1, 0.5f); sound(SoundEvents.GENERIC_EXPLODE, 0.7f, 1.2f); }
            }).weight(2),
            AndroidUnit.evade(EVADE),
            // self-destruct: invulnerable build-up, blast, survive in the overload form
            new Skill(SELF_DESTRUCT, "self_destruct", 60, 0, 0, 99, (u, t, k) -> {
                iframes = 3; setDeltaMovement(0, getDeltaMovement().y, 0);
                if (k < 44) { sl().sendParticles(k > 30 ? ParticleTypes.FLAME : ParticleTypes.SMOKE, getX(), getY() + 1, getZ(), 3 + k / 8, 0.3, 0.6, 0.3, 0.02);
                    if (k % 10 == 0) sound(SoundEvents.NOTE_BLOCK_PLING.value(), 1, 0.5f + k / 40f); }
                if (k == 45) { aoe(position(), 6, 20, 0.9);
                    sl().sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY() + 1, getZ(), 1, 0, 0, 0, 0);
                    sl().sendParticles(ParticleTypes.LAVA, getX(), getY() + 1, getZ(), 30, 1, 1, 1, 0.2);
                    sound(SoundEvents.GENERIC_EXPLODE, 2, 0.7f);
                    setHealth(getMaxHealth() * 0.35f); setForm(1);
                    getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.38); }
            }).noTarget().noTurn()
        );
    }

    @Nullable @Override protected Skill forcedSkill() {
        return getForm() == 0 && getHealth() < getMaxHealth() * 0.25f ? skillById(SELF_DESTRUCT) : null;
    }

    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide && getForm() == 1 && random.nextInt(4) == 0)
            level().addParticle(ParticleTypes.SMALL_FLAME, getRandomX(0.5), getY() + random.nextDouble() * 1.8, getRandomZ(0.5), 0, 0.02, 0);
    }
}
