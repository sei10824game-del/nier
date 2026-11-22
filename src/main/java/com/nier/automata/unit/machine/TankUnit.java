package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.unit.machine.MachineShot.Kind;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Goliath Tank: top-cannon energy rounds and sweeping laser, front cleaver arm, rear crane hammer, secondary missile
 * pods, cargo unit that deploys Small Stubbies, and the slow turn-to-face then charge.
 */
public class TankUnit extends MachineUnit {
    public static final int CANNON = 1, LASER = 2, CLEAVE = 3, HAMMER = 4, CHARGE = 5, DEPLOY = 6, MISSILES = 7;

    public TankUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 2.2 * v.scale(); }

    private Vec3 muzzle(float yaw) {
        float s = v.scale();
        return position().add(fwd(yaw).scale(1.8 * s)).add(0, 0.95 * s, 0);
    }
    private boolean behind() {
        return getTarget() != null && fwd().dot(getTarget().position().subtract(position()).normalize()) < 0.1;
    }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(CANNON, "Cannon Rounds", 36, 40, 4, 30, (u, t, k) -> {
            if (k < 14 && k % 2 == 0) fx(CHARGE_FX, muzzle(getYRot()), 3, 0.2, 0);
            if (k == 14 || k == 18 || k == 22) { shoot(muzzle(getYRot()), targetPos(t), 0.6, k == 18 ? Kind.ORB : Kind.HARD_ORB, dmg(0.9));
                fx(ParticleTypes.LARGE_SMOKE, muzzle(getYRot()), 3, 0.2, 0.02); sound(SoundEvents.BLAZE_SHOOT, 1f, 0.7f); }
        }).weight(3).when(u -> !behind()));
        l.add(new Skill(LASER, "Sweeping Laser", 64, 160, 4, 24, (u, t, k) -> {
            if (k == 0 && t != null) lockYaw = yawTo(t.position());
            face(lockYaw);
            if (k < 20 && k % 2 == 0) { fx(LASER_FX, muzzle(lockYaw), 3, 0.2, 0); if (k == 0) sound(SoundEvents.BEACON_ACTIVATE, 1.5f, 1.2f); }
            if (k >= 20 && k <= 51) {
                float yaw = lockYaw + (-50f + 100f * (k - 20) / 31f);
                Vec3 m = muzzle(yaw);
                beam(m, fwd(yaw).add(0, -0.08, 0), 24, dmg(0.6), k);
                if (k % 6 == 0) sound(SoundEvents.BEACON_AMBIENT, 1.5f, 2f);
            }
        }).noTurn());
        l.add(new Skill(CLEAVE, "Arm Cleave", 34, 30, 0, 3 * s, (u, t, k) -> {
            if (k == 14) { swing(3 * s, 70, dmg(2.0), 1.0, false); dust(at(2.6 * s, 0, 0), 25, 0.4 * s); sound(SoundEvents.ANVIL_LAND, 0.8f, 0.6f); }
        }).weight(3).when(u -> !behind()));
        l.add(new Skill(HAMMER, "Crane Hammer", 40, 40, 0, 3.4 * s, (u, t, k) -> {
            Vec3 c = at(-1.7 * s, 0, 0);
            if (k == 18) { area(c, 1.6 * s, dmg(2.2), 1.5, true); dust(c, 35, 0.5 * s); sound(SoundEvents.GENERIC_EXPLODE, 1f, 0.8f); }
            if (k >= 18 && k <= 28) ring(c, (k - 17) * 0.45 * s, dmg(1.0));
        }).weight(4).noTurn().when(u -> behind()));
        l.add(new Skill(CHARGE, "Turn & Charge", 56, 140, 6, 24, (u, t, k) -> {
            if (k < 18) { if (t != null) face(Mth.approachDegrees(getYRot(), yawTo(t.position()), 8)); fx(ParticleTypes.LARGE_SMOKE, at(-1.8 * s, 1.0 * s, 0), 2, 0.2, 0.02);
                if (k == 0) sound(SoundEvents.RAVAGER_ROAR, 1f, 0.6f); }
            if (k == 17) lockYaw = getYRot();
            if (k >= 18 && k < 44) { face(lockYaw); dash(0.7 * Math.sqrt(s)); swing(1.8 * s, 100, dmg(2.2), 2.5, true);
                if (k % 4 == 0) { dust(position(), 6, 0.6 * s); sound(SoundEvents.RAVAGER_STEP, 1f, 0.6f); } }
        }).noTurn());
        l.add(new Skill(MISSILES, "Missile Pods", 46, 110, 6, 28, (u, t, k) -> {
            if (k >= 12 && k <= 33 && k % 3 == 0) { boolean r = (k / 3) % 2 == 0;
                MachineShot.fire(this, at(0.1 * s, 1.1 * s, (r ? 0.6 : -0.6) * s), fwd().scale(0.2).add(0, 0.55, 0), Kind.MISSILE, dmg(0.8), t);
                sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1f, 0.8f); }
        }));
        l.add(new Skill(DEPLOY, "Deploy Cargo", 40, 400, 0, 30, (u, t, k) -> {
            if (k == 20 || k == 26) {
                var type = MachineModules.SMALL_STUBBY.type;
                if (type == null || !(level() instanceof net.minecraft.server.level.ServerLevel sl)) return;
                var e = type.get().create(sl);
                if (e == null) return;
                Vec3 p = at(-1.0 * s, 1.0 * s, (k == 20 ? 0.6 : -0.6) * s);
                e.moveTo(p.x, p.y, p.z, getYRot(), 0);
                e.setDeltaMovement(fwd().scale(-0.4).add(0, 0.4, 0));
                e.finalizeSpawn(sl, sl.getCurrentDifficultyAt(blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                e.setTarget(t); sl.addFreshEntity(e);
                fx(ParticleTypes.POOF, p, 10, 0.3, 0.05); sound(SoundEvents.PISTON_EXTEND, 1f, 0.6f);
            }
        }).when(u -> level().getEntitiesOfClass(StubbyUnit.class, getBoundingBox().inflate(16)).size() < 4));
        return l;
    }
}
