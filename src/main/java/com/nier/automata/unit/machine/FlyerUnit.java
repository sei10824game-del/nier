package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.unit.machine.MachineShot.Kind;
import com.nier.automata.unit.machine.MachineVariant.Size;
import com.nier.automata.unit.machine.MachineVariant.Weapon;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Flyers. Small: orb volley, dive ram. Medium: guns on its flight units (orb volley), side wire-cutters,
 * dropped self-destruct units (bombs), dive. Machine Flyer: homing missile salvo, orb volley, dive.
 */
public class FlyerUnit extends MachineUnit {
    public static final int VOLLEY = 1, DIVE = 2, MISSILES = 3, BOMBS = 4, CUTTER = 5;

    public FlyerUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) {
        super(t, l, v);
        moveControl = new FlyingMoveControl(this, 20, true);
        setNoGravity(true);
    }
    @Override protected PathNavigation createNavigation(Level l) {
        FlyingPathNavigation n = new FlyingPathNavigation(this, l); n.setCanFloat(true); n.setCanOpenDoors(false); return n;
    }
    @Override public double preferredRange() { return v.size() == Size.MEDIUM ? 3 : 7; }

    /** Hover a few blocks above the target while not attacking. */
    @Override public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && getTarget() != null && !isCasting()) {
            double want = getTarget().getY() + 3 + v.scale();
            if (getY() < want) setDeltaMovement(getDeltaMovement().add(0, 0.04, 0));
        }
        setDeltaMovement(getDeltaMovement().multiply(1, 0.9, 1));
    }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(VOLLEY, "Orb Volley", 30, 40, 2, 18, (u, t, k) -> {
            int[] frames = v.small() ? new int[]{10, 18} : new int[]{10, 14, 18, 22};
            for (int f : frames) if (k == f) {
                double side = v.size() == Size.MEDIUM ? ((k / 4) % 2 == 0 ? 0.8 : -0.8) * s : 0;
                shoot(at(0.4 * s, 0.3 * s, side), targetPos(t), 0.5, k == 14 ? Kind.HARD_ORB : Kind.ORB, dmg(0.8));
                sound(SoundEvents.BLAZE_SHOOT, 0.4f, 1.9f);
            }
        }).weight(3));
        l.add(new Skill(DIVE, "Dive Ram", 36, 80, 2, 12, (u, t, k) -> {
            if (k < 10) setDeltaMovement(0, 0.06, 0);
            if (k >= 10 && k < 24) { Vec3 d = aim.add(0, 0.5, 0).subtract(position()).normalize().scale(0.8); setDeltaMovement(d);
                area(position(), 1.4 * s, dmg(1.5), 1.0, true); }
            if (k >= 24) setDeltaMovement(getDeltaMovement().scale(0.7).add(0, 0.08, 0));
            if (k == 10) sound(SoundEvents.PHANTOM_SWOOP, 0.8f, 1.3f);
        }));
        if (v.weapon() == Weapon.MISSILE || v.size() == Size.MEDIUM) l.add(new Skill(MISSILES, "Missile Salvo", 40, 100, 5, 24, (u, t, k) -> {
            if (k == 12 || k == 16 || k == 20 || k == 24) {
                Vec3 from = at(0, 0.7 * s, (k % 8 == 0 ? 0.3 : -0.3) * s);
                MachineShot.fire(this, from, fwd().scale(0.3).add(0, 0.45, 0), Kind.MISSILE, dmg(1.0), t);
                sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 0.8f, 0.9f);
            }
        }).weight(2));
        if (v.size() == Size.MEDIUM) {
            l.add(new Skill(BOMBS, "Self-Destruct Units", 34, 120, 0, 8, (u, t, k) -> {
                if (k < 12 && t != null) { Vec3 d = t.position().add(0, 4, 0).subtract(position()); setDeltaMovement(d.scale(0.08)); }
                if (k == 12 || k == 18 || k == 24) { MachineShot.fire(this, at(0, -0.2, 0), getDeltaMovement().scale(0.3), Kind.SHELL, dmg(1.3), null);
                    sound(SoundEvents.DISPENSER_LAUNCH, 0.8f, 0.7f); }
            }).noTurn());
            l.add(new Skill(CUTTER, "Wire Cutters", 24, 30, 0, 2.6 * s, (u, t, k) -> {
                if (k == 9) dash(0.3);
                if (k == 10) { swing(2.6 * s, 160, dmg(1.2), 0.6, false); sound(SoundEvents.SHEEP_SHEAR, 1f, 0.7f); }
            }).weight(3));
        }
        return l;
    }
}
