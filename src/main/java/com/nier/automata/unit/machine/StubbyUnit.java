package com.nier.automata.unit.machine;

import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.unit.machine.MachineShot.Kind;
import com.nier.automata.unit.machine.MachineVariant.Weapon;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Stubby machines (small / medium / large, gold and hacked) with every weapon kit: bare-handed swipes, spinning arms,
 * jump body-press, tackle, energy rounds, orb rings and ground pound; axe, spear (thrust / rapid / pike charge),
 * hammer, staff and shield variants. Small unarmed stubbies overheat and self-destruct at low HP (form 1).
 */
public class StubbyUnit extends MachineUnit {
    public static final int SWIPE = 1, SPIN = 2, PRESS = 3, TACKLE = 4, ORB = 5, SELF_DESTRUCT = 6, ORB_RING = 7, POUND = 8,
            CHOP = 11, AXE_SWEEP = 12, THRUST = 14, MULTI = 15, PIKE = 16, SMASH = 17, WHIRL = 18,
            STAFF_ORBS = 19, NOVA = 20, STAFF_BASH = 21, SHIELD_BASH = 22;
    private Skill selfDestruct;

    public StubbyUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }

    @Override public double preferredRange() { return v.weapon() == Weapon.STAFF ? 6 : 1.5 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        Weapon w = v.weapon();
        List<Skill> l = new ArrayList<>();
        if (w == Weapon.NONE || w == Weapon.SHIELD) l.add(new Skill(SWIPE, "Arm Swipe", 24, 20, 0, 1.8 * s, (u, t, k) -> {
            if (k == 8 || k == 14) dash(0.25);
            if (k == 9) { swing(1.8 * s, 110, dmg(1), 0.4, false); sound(SoundEvents.IRON_GOLEM_ATTACK, 0.6f, 1.6f); }
            if (k == 15 && w == Weapon.NONE) swing(1.8 * s, 110, dmg(1), 0.6, false);
        }).weight(3));
        if (w == Weapon.NONE) l.add(new Skill(SPIN, "Spinning Arms", 40, 90, 0, 2.2 * s, (u, t, k) -> {
            if (k >= 9 && k <= 33) {
                if (t != null) { setYRot(yawTo(t.position())); dash(0.12); }
                if (k % 6 == 3) { area(position(), 1.9 * s, dmg(0.7), 0.5, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.6f, 1.4f); }
            }
        }).noTurn().weight(2));
        if (w == Weapon.NONE || w == Weapon.AXE || w == Weapon.HAMMER) l.add(pressSkill(s));
        if (w == Weapon.NONE || w == Weapon.AXE) l.add(new Skill(TACKLE, "Headbutt Tackle", 30, 70, 3 * s, 9 * s, (u, t, k) -> {
            if (k < 10) { if (k % 3 == 0) dust(position(), 2, 0.2); }
            else if (k < 20) { dash(0.6 * Math.sqrt(s)); swing(1.3 * s, 90, dmg(1.3), 1.2, true); }
        }));
        if (w == Weapon.NONE || w == Weapon.SHIELD) l.add(new Skill(ORB, "Energy Round", 26, 60, 4, 16, (u, t, k) -> {
            if (k < 12 && k % 2 == 0) fx(CHARGE_FX, at(0.3 * s, 1.0 * s, 0), 2, 0.1 * s, 0);
            if (k == 12) {
                Vec3 from = at(0.6 * s, 1.0 * s, 0), p = targetPos(t);
                shoot(from, p, 0.45, Kind.ORB, dmg(0.9));
                if (!v.small()) { shoot(from, p.add(fwd().cross(new Vec3(0, 1, 0)).scale(2)), 0.45, Kind.HARD_ORB, dmg(0.9));
                                  shoot(from, p.add(fwd().cross(new Vec3(0, 1, 0)).scale(-2)), 0.45, Kind.HARD_ORB, dmg(0.9)); }
                sound(SoundEvents.BLAZE_SHOOT, 0.5f, 1.8f);
            }
        }).weight(2));
        if (w == Weapon.NONE && !v.small()) {
            l.add(new Skill(ORB_RING, "Orb Burst", 36, 120, 0, 12, (u, t, k) -> {
                if (k < 16 && k % 2 == 0) fx(CHARGE_FX, at(0, 1.2 * s, 0), 3, 0.4 * s, 0);
                if (k == 16 || (k == 26 && v.large())) {
                    int n = 12; double off = k == 26 ? Math.PI / n : 0;
                    for (int i = 0; i < n; i++) { double a = off + i * 2 * Math.PI / n;
                        Vec3 d = new Vec3(Math.cos(a), 0, Math.sin(a));
                        MachineShot.fire(this, at(0, 0.9 * s, 0).add(d.scale(0.6 * s)), d.scale(0.3), i % 2 == 0 ? Kind.ORB : Kind.HARD_ORB, dmg(0.8), null); }
                    sound(SoundEvents.BLAZE_SHOOT, 0.8f, 1.2f);
                }
            }));
            l.add(new Skill(POUND, "Ground Pound", 36, 80, 0, 3.5 * s, (u, t, k) -> {
                if (k == 14) { Vec3 c = at(1.2 * s, 0, 0); area(c, 1.6 * s, dmg(1.8), 0.8, true); dust(c, 30, 0.6 * s);
                               sound(SoundEvents.GENERIC_EXPLODE, 0.6f, 1.4f); }
                if (k >= 14 && k <= 26) ring(at(1.2 * s, 0, 0), (k - 13) * 0.5 * s, dmg(1.0));
            }));
        }
        switch (w) {
            case AXE -> {
                l.add(new Skill(CHOP, "Axe Chop", 28, 30, 0, 2.4 * s, (u, t, k) -> {
                    if (k == 11) dash(0.3);
                    if (k == 12) { swing(2.4 * s, 70, dmg(1.7), 0.8, false); dust(at(2 * s, 0, 0), 12, 0.3); sound(SoundEvents.ANVIL_LAND, 0.4f, 1.6f); }
                }).weight(3));
                l.add(new Skill(AXE_SWEEP, "Axe Sweep", 30, 50, 0, 2.4 * s, (u, t, k) -> {
                    if (k == 11) { swing(2.4 * s, 220, dmg(1.4), 1.0, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 0.9f); }
                }).weight(2));
            }
            case SPEAR -> {
                l.add(new Skill(THRUST, "Spear Thrust", 22, 25, 0, 3.4 * s, (u, t, k) -> {
                    if (k == 9) { dash(0.45); swing(3.4 * s, 35, dmg(1.4), 0.6, false); sound(SoundEvents.TRIDENT_THROW, 0.7f, 1.4f); }
                }).weight(3));
                l.add(new Skill(MULTI, "Rapid Thrusts", 40, 70, 0, 3.2 * s, (u, t, k) -> {
                    if (k == 8 || k == 16 || k == 24 || k == 32) { dash(0.15); swing(3.2 * s, 35, dmg(0.8), 0.2, false); sound(SoundEvents.TRIDENT_THROW, 0.5f, 1.8f); }
                }).weight(2));
                l.add(new Skill(PIKE, "Pike Charge", 36, 90, 4, 10, (u, t, k) -> {
                    if (k < 12 && k % 3 == 0) dust(position(), 2, 0.2);
                    if (k >= 12 && k < 28) { dash(0.55); swing(2.6 * s, 40, dmg(1.8), 1.4, true); }
                }));
            }
            case HAMMER -> {
                l.add(new Skill(SMASH, "Hammer Smash", 34, 35, 0, 2.6 * s, (u, t, k) -> {
                    if (k == 14) { swing(2.6 * s, 70, dmg(2.0), 0.6, true); dust(at(2 * s, 0, 0), 25, 0.5); sound(SoundEvents.GENERIC_EXPLODE, 0.5f, 1.6f); }
                    if (k >= 14 && k <= 22) ring(at(2 * s, 0, 0), (k - 13) * 0.45 * s, dmg(0.9));
                }).weight(3));
                l.add(new Skill(WHIRL, "Hammer Whirl", 44, 90, 0, 2.8 * s, (u, t, k) -> {
                    if (k >= 9 && k <= 35) {
                        if (t != null) { setYRot(yawTo(t.position())); dash(0.1); }
                        if (k % 7 == 2) { area(position(), 2.6 * s, dmg(0.9), 0.6, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 0.8f); }
                    }
                }).noTurn());
            }
            case STAFF -> {
                l.add(new Skill(STAFF_ORBS, "Staff Volley", 36, 50, 3, 16, (u, t, k) -> {
                    if (k < 12 && k % 2 == 0) fx(CHARGE_FX, at(0.8 * s, 1.6 * s, -0.4 * s), 2, 0.1, 0);
                    if (k == 12 || k == 18 || k == 24) { shoot(at(0.8 * s, 1.6 * s, -0.4 * s), targetPos(t), 0.5, k == 18 ? Kind.HARD_ORB : Kind.ORB, dmg(1.0));
                                                         sound(SoundEvents.EVOKER_CAST_SPELL, 0.5f, 1.6f); }
                }).weight(3));
                l.add(new Skill(NOVA, "Staff Nova", 40, 100, 0, 5, (u, t, k) -> {
                    if (k < 16 && k % 2 == 0) fx(CHARGE_FX, at(0.5 * s, 1.8 * s, 0), 3, 0.3, 0.02);
                    if (k == 16) { area(position(), 3.5 * s, dmg(1.4), 1.2, true); fx(ParticleTypes.FLASH, position(), 1, 0, 0);
                        for (int i = 0; i < 8; i++) { double a = i * Math.PI / 4; Vec3 d = new Vec3(Math.cos(a), 0, Math.sin(a));
                            MachineShot.fire(this, at(0, 0.6 * s, 0).add(d), d.scale(0.3), Kind.HARD_ORB, dmg(0.8), null); }
                        sound(SoundEvents.EVOKER_PREPARE_ATTACK, 0.8f, 1.2f); }
                }).weight(2));
                l.add(new Skill(STAFF_BASH, "Staff Strike", 22, 20, 0, 2.6 * s, (u, t, k) -> {
                    if (k == 10) { swing(2.6 * s, 140, dmg(1.2), 0.9, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.6f, 1.3f); }
                }).weight(2));
            }
            case SHIELD -> l.add(new Skill(SHIELD_BASH, "Shield Bash", 26, 40, 0, 2.4 * s, (u, t, k) -> {
                if (k >= 10 && k < 15) { dash(0.4); if (swing(1.8 * s, 90, dmg(1.2), 1.6, true) > 0) sound(SoundEvents.SHIELD_BLOCK, 1f, 0.8f); }
            }).weight(2));
            default -> {}
        }
        if ((w == Weapon.NONE || w == Weapon.BOMB) && v.small()) l.add(selfDestruct = new Skill(SELF_DESTRUCT, "Self-Destruct", 50, 0, 0, 999, (u, t, k) -> {
            if (k == 0) { setForm(1); sound(SoundEvents.CREEPER_PRIMED, 1f, 0.8f); }
            fx(ParticleTypes.LARGE_SMOKE, at(0, 1.4, 0), 1, 0.2, 0.02);
            if (k < 40 && t != null) getNavigation().moveTo(t, 1.6);
            if (k >= 40) { getNavigation().stop(); fx(ParticleTypes.FLAME, at(0, 0.8, 0), 3, 0.4, 0.02); }
            if (k == 49) {
                area(position(), 3, dmg(3), 1.5, true);
                level().explode(this, getX(), getY() + 0.5, getZ(), 1.5f, Level.ExplosionInteraction.NONE);
                kill();
            }
        }).noTarget().when(u -> false));
        return l;
    }

    private Skill pressSkill(float s) {
        return new Skill(PRESS, "Jump Press", 34, 70, 2.5 * s, 7 * s, (u, t, k) -> {
            if (k == 8) { leap(targetPos(t), 12, 0.55); sound(SoundEvents.RAVAGER_STEP, 0.6f, 1.5f); }
            if (!landed && ((k >= 13 && onGround()) || k == 22)) {
                landed = true; area(position(), 2.2 * s, dmg(1.5), 1.0, true); dust(position(), 25, 0.6 * s);
                sound(SoundEvents.GENERIC_EXPLODE, 0.4f, 1.6f);
            }
        });
    }

    @Override @Nullable protected Skill forcedSkill() {
        if (v.weapon() == Weapon.BOMB && getTarget() != null && distanceTo(getTarget()) < 3.2) return selfDestruct;
        return selfDestruct != null && getHealth() < getMaxHealth() * 0.3f ? selfDestruct : null;
    }
}
