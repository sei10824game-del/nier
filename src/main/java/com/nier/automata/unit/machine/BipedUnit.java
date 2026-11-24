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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Biped machines. Small: flailing arms, charge, kick, head laser. Medium (club / shield): overhead club, club sweep,
 * 3-hit combo, charge, jump slam, laser, kick, shield bash + frontal guard. Large: flail, stomp, double-fist slam,
 * orb volley, charge, laser, jump slam.
 */
public class BipedUnit extends MachineUnit {
    private final java.util.List<Vec3> marks = new java.util.ArrayList<>();
    public static final int LIGHTNING = 30, FLAIL = 1, CHARGE = 2, CLUB = 3, SWEEP = 4, COMBO = 5, LASER = 6, JUMP_SLAM = 7, STOMP = 8,
            ORB_VOLLEY = 9, FIST_SLAM = 10, KICK = 11, SHIELD_BASH = 12;

    public BipedUnit(EntityType<? extends NierUnit> t, Level l, MachineVariant v) { super(t, l, v); }

    @Override public double preferredRange() { return 1.6 * v.scale(); }

    @Override protected List<Skill> createSkills() {
        final float s = v.scale();
        boolean club = v.weapon() == Weapon.CLUB || v.weapon() == Weapon.SHIELD;
        List<Skill> l = new ArrayList<>();
        if (!club) l.add(new Skill(FLAIL, "Flailing Arms", 36, 40, 0, 2.2 * s, (u, t, k) -> {
            if (k >= 6 && k <= 28) dash(0.08);
            if (k == 8 || k == 14 || k == 20 || k == 26) { swing(2.2 * s, 100, dmg(0.7), 0.3, false); sound(SoundEvents.PLAYER_ATTACK_WEAK, 0.7f, 0.9f); }
        }).weight(3));
        l.add(new Skill(CHARGE, "Charge", 34, 80, 4 * s, 12 * s, (u, t, k) -> {
            if (k < 10 && k % 3 == 0) dust(position(), 2, 0.2 * s);
            if (k >= 10 && k < 26) { dash(0.6 * Math.sqrt(s)); swing(1.6 * s, 90, dmg(1.4), 1.5, true); if (k % 4 == 0) sound(SoundEvents.IRON_GOLEM_STEP, 0.6f, 1.4f); }
        }));
        if (v.size() != Size.LARGE) l.add(new Skill(KICK, "Front Kick", 22, 30, 0, 2.2 * s, (u, t, k) -> {
            if (k == 10) { swing(2.2 * s, 60, dmg(1.1), 1.2, false); sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 0.7f, 1f); }
        }).weight(2));
        if (v.weapon() != Weapon.SHIELD) l.add(new Skill(LASER, "Head Laser", 44, 120, 4, 14, (u, t, k) -> {
            Vec3 eye = at(0.3 * s, 1.75 * s, 0);
            if (k < 15 && k % 2 == 0) fx(LASER_FX, eye, 2, 0.1 * s, 0);
            if (k >= 15 && k <= 35) { aim = aim.add(targetPos(t).subtract(aim).scale(0.08)); beam(eye, aim.subtract(eye), 16, dmg(0.35), k);
                                      if (k % 5 == 0) sound(SoundEvents.BEACON_AMBIENT, 0.6f, 2f); }
        }));
        if (club) {
            l.add(new Skill(CLUB, "Overhead Club", 30, 30, 0, 3 * s, (u, t, k) -> {
                if (k == 12) dash(0.3);
                if (k == 13) { swing(3 * s, 50, dmg(1.8), 0.6, false); dust(at(2.6 * s, 0, 0), 15, 0.4); sound(SoundEvents.ANVIL_LAND, 0.4f, 1.2f); }
            }).weight(3));
            l.add(new Skill(COMBO, "Club Combo", 46, 60, 0, 3 * s, (u, t, k) -> {
                if (k == 9 || k == 19 || k == 31) dash(0.25);
                if (k == 10 || k == 20) { swing(3 * s, 160, dmg(0.9), 0.4, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.7f, 1.1f); }
                if (k == 32) { swing(3 * s, 60, dmg(1.6), 1.0, false); dust(at(2.6 * s, 0, 0), 15, 0.4); sound(SoundEvents.ANVIL_LAND, 0.4f, 1.2f); }
            }).weight(2));
        }
        if (v.weapon() == Weapon.CLUB) l.add(new Skill(SWEEP, "Club Sweep", 28, 40, 0, 3 * s, (u, t, k) -> {
            if (k == 11) { swing(3 * s, 180, dmg(1.3), 1.0, false); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 0.8f); }
        }).weight(2));
        if (v.weapon() == Weapon.SHIELD) l.add(new Skill(SHIELD_BASH, "Shield Bash", 26, 40, 0, 2.4 * s, (u, t, k) -> {
            if (k >= 10 && k < 15) { dash(0.45); if (swing(1.8 * s, 90, dmg(1.2), 1.8, true) > 0) sound(SoundEvents.SHIELD_BLOCK, 1f, 0.7f); }
        }).weight(2));
        if (v.weapon() == Weapon.CLUB || v.size() == Size.LARGE) l.add(new Skill(JUMP_SLAM, "Jump Slam", 36, 80, 3 * s, 8 * s, (u, t, k) -> {
            if (k == 9) { leap(targetPos(t), 12, 0.6); sound(SoundEvents.RAVAGER_STEP, 0.7f, 1.2f); }
            if (!landed && ((k >= 14 && onGround()) || k == 23)) { landed = true; area(position(), 2.4 * s, dmg(1.7), 1.0, true); dust(position(), 25, 0.7 * s);
                sound(SoundEvents.GENERIC_EXPLODE, 0.5f, 1.3f); }
        }));
        if (v.electric()) l.add(new Skill(LIGHTNING, "Thunderfall", 46, 100, 0, 22, (u, t, k) -> {
            int rgb = v.skin() == MachineVariant.Skin.KOSHI ? com.nier.automata.core.Fx.HACK : 0xb060ff;
            if (k == 8 && t != null) { marks.clear(); for (int i = 0; i < 5; i++) marks.add(t.position().add(new Vec3(i == 0 ? 0 : 3.2, 0, 0).yRot(i * 1.2566f))); }
            if (k >= 8 && k < 26 && k % 3 == 0) for (Vec3 m : marks) com.nier.automata.core.Fx.shock(level(), m, 1.6, rgb);
            if (k == 26) for (Vec3 m : marks) { com.nier.automata.core.Fx.pillar(level(), m, 9, 0.6, rgb); area(m, 1.8, dmg(2.0), 1.0, true); sound(SoundEvents.LIGHTNING_BOLT_THUNDER, 0.8f, 1.4f); }
        }).weight(3));
        if (v.size() == Size.LARGE) {
            l.add(new Skill(STOMP, "Stomp", 30, 60, 0, 4 * s, (u, t, k) -> {
                if (k == 13) { Vec3 c = at(0.4 * s, 0, -0.3 * s); area(c, 1.4 * s, dmg(1.6), 0.8, true); dust(c, 25, 0.5 * s); sound(SoundEvents.GENERIC_EXPLODE, 0.6f, 1.0f); }
                if (k >= 13 && k <= 25) ring(at(0.4 * s, 0, -0.3 * s), (k - 12) * 0.45 * s, dmg(0.9));
            }).weight(2));
            l.add(new Skill(FIST_SLAM, "Double Fist Slam", 34, 70, 0, 3 * s, (u, t, k) -> {
                if (k == 14) { Vec3 c = at(1.4 * s, 0, 0); area(c, 1.5 * s, dmg(2.0), 1.0, true); dust(c, 30, 0.5 * s); sound(SoundEvents.GENERIC_EXPLODE, 0.7f, 0.9f); }
                if (k >= 14 && k <= 24) ring(at(1.4 * s, 0, 0), (k - 13) * 0.5 * s, dmg(1.0));
            }).weight(2));
            l.add(new Skill(ORB_VOLLEY, "Orb Volley", 40, 90, 4, 18, (u, t, k) -> {
                if (k >= 12 && k <= 32 && k % 4 == 0) { boolean right = (k / 4) % 2 == 0;
                    shoot(at(0.8 * s, 1.0 * s, (right ? 0.5 : -0.5) * s), targetPos(t), 0.5, k % 8 == 0 ? Kind.HARD_ORB : Kind.ORB, dmg(0.6));
                    sound(SoundEvents.BLAZE_SHOOT, 0.5f, 1.5f); }
            }));
        }
        return l;
    }
}
