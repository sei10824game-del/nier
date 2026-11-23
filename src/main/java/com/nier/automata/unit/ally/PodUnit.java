package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Tactical support Pod (form 0 = Pod 042 tan, 1 = Pod 153 grey). Hovers above the fight: Machine Gun,
 * Pod Program Laser, Missile salvo, Hammer (dives and slams as a hammer), Repair.
 */
public class PodUnit extends AllyUnit {
    public static final int GUN = 1, LASER = 2, MISSILE = 3, HAMMER = 4, REPAIR = 5;
    private boolean slammed;
    private static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1f, 0.25f, 0.2f), 1.4f);

    public PodUnit(EntityType<? extends PodUnit> t, Level l) { super(t, l); moveControl = new FlyingMoveControl(this, 20, true); setNoGravity(true); }
    @Override protected int variants() { return 2; }
    @Override public double preferredRange() { return 6; }
    @Override protected PathNavigation createNavigation(Level l) { FlyingPathNavigation n = new FlyingPathNavigation(this, l); n.setCanOpenDoors(false); n.setCanFloat(true); return n; }
    @Override public boolean causeFallDamage(float d, float m, net.minecraft.world.damagesource.DamageSource s) { return false; }
    @Override protected void checkFallDamage(double y, boolean g, BlockState s, BlockPos p) {}

    @Override public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        LivingEntity t = getTarget();
        double want = (t != null ? t.getY() + 2.5 : level().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, getBlockX(), getBlockZ()) + 1.8);
        if (getSkillId() != HAMMER) setDeltaMovement(getDeltaMovement().multiply(1, 0.6, 1).add(0, Math.max(-0.08, Math.min(0.08, (want - getY()) * 0.05)), 0));
    }
    @Override public Vec3 podPos() { return position().add(0, 0.35, 0).add(fwd().scale(0.4)); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(GUN, "Machine Gun", 30, 30, 0, 22, (u, t, i) -> {
                if (i >= 4 && i <= 26 && i % 2 == 0) { Vec3 p = podPos(); shoot(p, aimAt(t, p).add(random.nextGaussian() * 0.03, 0, random.nextGaussian() * 0.03), 2.6)
                    .dmg(1.6f).trail(ParticleTypes.ELECTRIC_SPARK).life(15); if (i % 4 == 0) sound(SoundEvents.DISPENSER_LAUNCH, 0.4f, 2f); }
            }).weight(4),
            new Skill(LASER, "Pod Program: Laser", 50, 140, 3, 24, (u, t, i) -> {
                if (i < 20 && i % 2 == 0) fx(RED, podPos(), 4, 0.25, 0);
                if (i == 20) sound(SoundEvents.BEACON_ACTIVATE, 1, 1.6f);
                if (i >= 20 && i <= 44 && i % 2 == 0) beam(podPos(), aimAt(t, podPos()), 24, 2.5f, RED);
            }),
            new Skill(MISSILE, "Pod Program: Missile", 34, 120, 4, 24, (u, t, i) -> {
                if (i >= 10 && i <= 25 && i % 3 == 1) { Vec3 p = podPos().add(0, 0.3, 0);
                    shoot(p, new Vec3(random.nextGaussian() * 0.4, 1, random.nextGaussian() * 0.4), 0.7).homing(0.15, t).boom(1.8).dmg(5).trail(ParticleTypes.FLAME).life(60).size(0.25);
                    sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 0.7f, 1.3f); }
            }),
            new Skill(HAMMER, "Pod Program: Hammer", 36, 160, 0, 6, (u, t, i) -> {
                if (i == 0) slammed = false;
                if (i == 8) setDeltaMovement(0, 0.45, 0);
                if (i == 16 && t != null) { Vec3 d = t.position().subtract(position()); setDeltaMovement(d.x * 0.3, -1.4, d.z * 0.3); }
                if (!slammed && i >= 16 && i <= 22 && (onGround() || i == 22)) { slammed = true; burst(position(), 3.2, 11, 1.2, 0.6); fx(ParticleTypes.EXPLOSION, position(), 2, 0.5, 0);
                    sound(SoundEvents.ANVIL_LAND, 1, 0.6f); }
            }).noTurn(),
            new Skill(REPAIR, "Pod Program: Repair", 30, 240, 0, 40, (u, t, i) -> {
                if (i % 4 == 0) fx(ParticleTypes.HAPPY_VILLAGER, podPos(), 3, 0.3, 0);
                if (i == 18) { healAllies(10, 8); sound(SoundEvents.BEACON_POWER_SELECT, 1, 1.6f); }
            }).noTurn()
        );
    }
}
