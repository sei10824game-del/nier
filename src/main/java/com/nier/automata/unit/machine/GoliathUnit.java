package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.unit.machine.MachineShot.Kind;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Goliath bipeds (small / medium / large): side-rail arm sweep, extending rail punch, grab-lift-slam, two-fist ground slam
 * with shockwave, shockwave stomp, eye beam sweep, shoulder missile barrage, mine scatter and (medium/large) a
 * rocket-engine leap into a diving kick.
 */
public class GoliathUnit extends MachineUnit {
    public static final int RAIL_SWIPE = 1, SLAM = 2, PUNCH = 3, GRAB = 4, BEAM = 5, MISSILES = 6, MINES = 7, DIVE_KICK = 8, STOMP = 9;
    private int grabbed = -1;

    public GoliathUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 1.6 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(RAIL_SWIPE, "Rail Arm Sweep", 36, 40, 0, 2.4 * s, (u, t, k) -> {
            if (k == 15) { swing(2.4 * s, 180, dmg(1.6), 1.5, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 0.5f); }
        }).weight(3));
        l.add(new Skill(PUNCH, "Rail Punch", 30, 40, 0, 2.6 * s, (u, t, k) -> {
            if (k == 12) { swing(2.6 * s, 45, dmg(1.8), 2.0, false); sound(SoundEvents.IRON_GOLEM_ATTACK, 1f, 0.6f); }
        }).weight(2));
        l.add(new Skill(GRAB, "Grab & Slam", 56, 160, 0, 2.2 * s, (u, t, k) -> {
            if (k == 0) grabbed = -1;
            if (k == 12) for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.2 * s - getBbWidth() / 2), this::foe))
                if (fwd().dot(e.position().subtract(position()).normalize()) > 0.3) { grabbed = e.getId(); sound(SoundEvents.IRON_GOLEM_HURT, 1f, 0.5f); break; }
            Entity g = grabbed < 0 ? null : level().getEntity(grabbed);
            if (g instanceof LivingEntity e && e.isAlive() && k > 12 && k <= 38) {
                double lift = Math.min(1, (k - 12) / 20.0) * 1.6 * s, down = k > 34 ? (k - 34) / 4.0 : 0;
                Vec3 p = at(1.1 * s, 0.9 * s + lift * (1 - down), 0.5 * s);
                e.teleportTo(p.x, Math.max(getY(), p.y - e.getBbHeight() / 2), p.z); e.setDeltaMovement(Vec3.ZERO); e.fallDistance = 0;
                if (k == 38) { strike(e, dmg(3.0), 0.5); dust(e.position(), 30, 0.5 * s); sound(SoundEvents.GENERIC_EXPLODE, 1f, 0.8f); }
            }
            if (k >= 38 && k <= 48) ring(at(1.1 * s, 0, 0.5 * s), (k - 37) * 0.4 * s, dmg(0.8));
        }));
        l.add(new Skill(SLAM, "Ground Slam", 44, 70, 0, 3 * s, (u, t, k) -> {
            if (k == 18) { Vec3 c = at(1.3 * s, 0, 0); area(c, 1.2 * s, dmg(2.2), 1.0, true); dust(c, 40, 0.6 * s); sound(SoundEvents.GENERIC_EXPLODE, 1.2f, 0.7f); }
            if (k >= 18 && k <= 32) ring(at(1.3 * s, 0, 0), (k - 17) * 0.5 * s, dmg(1.2));
        }).weight(2));
        l.add(new Skill(STOMP, "Shockwave Stomp", 38, 80, 0, 4 * s, (u, t, k) -> {
            if (k == 16) { Vec3 c = at(0.3 * s, 0, -0.35 * s); area(c, 1.0 * s, dmg(1.8), 1.0, true); dust(c, 40, 0.6 * s); sound(SoundEvents.GENERIC_EXPLODE, 1f, 0.8f); }
            if (k >= 16 && k <= 30) ring(at(0.3 * s, 0, -0.35 * s), (k - 15) * 0.6 * s, dmg(1.1));
        }).weight(2));
        l.add(new Skill(BEAM, "Eye Beam", 60, 140, 6, 30, (u, t, k) -> {
            Vec3 eye = at(0.45 * s, 1.95 * s, 0);
            if (k < 20 && k % 2 == 0) { fx(LASER_FX, eye, 3, 0.15 * s, 0); if (k == 0) sound(SoundEvents.BEACON_ACTIVATE, 1f, 1.5f); }
            if (k >= 20 && k < 50) {
                Vec3 side = new Vec3(-fwd().z, 0, fwd().x).scale(-5 + 10 * (k - 20) / 29.0);
                beam(eye, targetPos(t).add(side).subtract(eye), 30, dmg(0.5), k);
                if (k % 6 == 0) sound(SoundEvents.BEACON_AMBIENT, 1f, 2f);
            }
        }).noTurn());
        l.add(new Skill(MISSILES, "Missile Barrage", 50, 120, 8, 32, (u, t, k) -> {
            if (k >= 12 && k <= 42 && k % 3 == 0) { boolean r = (k / 3) % 2 == 0;
                MachineShot.fire(this, at(-0.1 * s, 2.1 * s, (r ? 0.6 : -0.6) * s), fwd().scale(0.2).add(0, 0.6, 0), Kind.MISSILE, dmg(0.9), t);
                fx(ParticleTypes.LARGE_SMOKE, at(-0.1 * s, 2.1 * s, (r ? 0.6 : -0.6) * s), 2, 0.2, 0.02);
                sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1f, 0.8f); }
        }));
        l.add(new Skill(MINES, "Mine Scatter", 36, 160, 4, 20, (u, t, k) -> {
            if (k >= 14 && k <= 24 && k % 2 == 0) { Vec3 p = (t != null ? t.position() : aim).add(random.nextGaussian() * 2.5, 0, random.nextGaussian() * 2.5);
                lob(at(-0.2 * s, 2.0 * s, 0), p, 20, Kind.MINE, dmg(1.2)); sound(SoundEvents.DISPENSER_LAUNCH, 1f, 0.6f); }
        }));
        if (!v.small()) l.add(new Skill(DIVE_KICK, "Rocket Dive Kick", 60, 160, 5, 16, (u, t, k) -> {
            if (k < 10 && k % 2 == 0) fx(ParticleTypes.FLAME, at(-0.5 * s, 1.2 * s, 0), 5, 0.3 * s, 0.05);
            if (k >= 10 && k < 22) { setDeltaMovement(0, 0.9, 0); fx(ParticleTypes.FLAME, at(-0.5 * s, 1.0 * s, 0), 6, 0.3 * s, 0.1);
                if (k == 10) sound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 2f, 0.5f); }
            if (k >= 22 && k < 26) setDeltaMovement(0, 0.02, 0);
            if (k == 26) { aim = targetPos(t); sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 2f, 0.5f); }
            if (k >= 26 && !landed) { setDeltaMovement(aim.subtract(position()).normalize().scale(1.4)); area(position(), 1.2 * s, dmg(2.0), 1.5, true);
                if ((k > 28 && onGround()) || k == 46) { landed = true; landTick = k; dust(position(), 50, s); sound(SoundEvents.GENERIC_EXPLODE, 1.5f, 0.6f); setDeltaMovement(Vec3.ZERO); } }
            if (landed && k - landTick <= 14) ring(position(), (k - landTick + 1) * 0.6 * s, dmg(1.4));
        }));
        return l;
    }
}
