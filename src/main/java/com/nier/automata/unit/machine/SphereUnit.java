package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.unit.machine.MachineShot.Kind;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Sphere machine: rolling ram, bounce slam, orb burst, fin spin; overheats (form 1) and self-destructs at low HP. */
public class SphereUnit extends MachineUnit {
    public static final int ROLL = 1, BOUNCE = 2, BURST = 3, FIN_SPIN = 4, SELF_DESTRUCT = 5, DRILL = 6, SAW = 7;
    private Skill selfDestruct;

    public SphereUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }
    @Override public double preferredRange() { return 1.5 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        List<Skill> l = new ArrayList<>();
        l.add(new Skill(ROLL, "Rolling Ram", 40, 60, 3, 14, (u, t, k) -> {
            if (k < 10 && k % 3 == 0) dust(position(), 3, 0.2);
            if (k >= 10 && k < 30) { dash(0.55); swing(1.4 * s, 100, dmg(1.3), 1.0, true); if (k % 5 == 0) sound(SoundEvents.MINECART_RIDING, 0.4f, 1.6f); }
        }).weight(3));
        l.add(new Skill(BOUNCE, "Bounce Slam", 34, 60, 2, 8, (u, t, k) -> {
            if (k == 8) { leap(targetPos(t), 12, 0.6); sound(SoundEvents.SLIME_JUMP, 1f, 0.6f); }
            if (!landed && ((k >= 13 && onGround()) || k == 22)) { landed = true; landTick = k; area(position(), 2.2 * s, dmg(1.5), 1.0, true);
                dust(position(), 20, 0.6); sound(SoundEvents.GENERIC_EXPLODE, 0.4f, 1.6f); }
            if (landed && k - landTick <= 8) ring(position(), (k - landTick + 1) * 0.5 * s, dmg(0.8));
        }).weight(2));
        l.add(new Skill(BURST, "Orb Burst", 32, 80, 0, 14, (u, t, k) -> {
            if (k < 12 && k % 2 == 0) fx(CHARGE_FX, at(0, 0.4 * s, 0), 3, 0.4 * s, 0);
            if (k == 12 || k == 20) { double off = k == 20 ? Math.PI / 8 : 0;
                for (int i = 0; i < 8; i++) { double a = off + i * Math.PI / 4; Vec3 d = new Vec3(Math.cos(a), 0, Math.sin(a));
                    MachineShot.fire(this, at(0, 0.4 * s, 0).add(d.scale(0.5 * s)), d.scale(0.3), i % 2 == 0 ? Kind.ORB : Kind.HARD_ORB, dmg(0.8), null); }
                sound(SoundEvents.BLAZE_SHOOT, 0.7f, 1.5f); }
        }).weight(2));
        l.add(new Skill(FIN_SPIN, "Fin Spin", 30, 50, 0, 2.2 * s, (u, t, k) -> {
            if (k >= 6 && k <= 25) { if (t != null) dash(0.12); if (k % 5 == 1) { area(position(), 1.8 * s, dmg(0.7), 0.6, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.6f, 1.6f); } }
        }).weight(2));
        l.add(new Skill(DRILL, "Drill Dive", 44, 70, 3, 12, (u, t, k) -> {
            if (k < 14 && k % 3 == 0) fx(CHARGE_FX, at(0, 1.2 * s, 0), 3, 0.3 * s, 0);
            if (k == 14) { leap(targetPos(t), 12, 0.75); sound(SoundEvents.IRON_GOLEM_ATTACK, 1f, 1.4f); }
            if (k >= 14 && k < 28) { swing(1.6 * s, 360, dmg(1.6), 0.5, true); if (k % 3 == 0) fx(ParticleTypes.CRIT, at(0, 1.4 * s, 0), 3, 0.4, 0.1); }
            if (!landed && k >= 24 && onGround()) { landed = true; area(position(), 2.0 * s, dmg(2.0), 1.0, true); dust(position(), 18, 0.5); sound(SoundEvents.GENERIC_EXPLODE, 0.4f, 1.8f); }
        }).weight(4).when(u -> v.weapon() == MachineVariant.Weapon.DRILL));
        l.add(new Skill(SAW, "Saw Rush", 40, 60, 2, 14, (u, t, k) -> {
            if (k < 10 && k % 3 == 0) fx(ParticleTypes.CRIT, at(0, 1.3 * s, 0), 4, 0.5 * s, 0.2);
            if (k >= 10 && k < 32) { dash(0.5); swing(1.7 * s, 140, dmg(1.2), 0.6, false); if (k % 4 == 0) { fx(ParticleTypes.CRIT, at(1.2 * s, 0.6 * s, 0), 5, 0.4, 0.2); sound(SoundEvents.GRINDSTONE_USE, 0.8f, 1.6f); } }
        }).weight(4).when(u -> v.weapon() == MachineVariant.Weapon.SAW));
        l.add(selfDestruct = new Skill(SELF_DESTRUCT, "Self-Destruct", 50, 0, 0, 999, (u, t, k) -> {
            if (k == 0) { setForm(1); sound(SoundEvents.CREEPER_PRIMED, 1f, 0.6f); }
            fx(ParticleTypes.LARGE_SMOKE, at(0, 0.8 * s, 0), 1, 0.2, 0.02);
            if (k < 40 && t != null) getNavigation().moveTo(t, 1.7);
            if (k >= 40) { getNavigation().stop(); fx(ParticleTypes.FLAME, at(0, 0.4 * s, 0), 3, 0.3, 0.02); }
            if (k == 49) { area(position(), 3, dmg(3), 1.5, true); level().explode(this, getX(), getY() + 0.4, getZ(), 1.5f, Level.ExplosionInteraction.NONE); kill(); }
        }).noTarget().when(u -> false));
        return l;
    }

    @Override @Nullable protected Skill forcedSkill() { return getHealth() < getMaxHealth() * 0.25f ? selfDestruct : null; }
}
