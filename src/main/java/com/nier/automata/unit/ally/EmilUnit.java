package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Emil, the wandering magic-weapon merchant, with the full "Emil's Determination" moveset.
 * Form 0 (on his cart): Indiscriminate Fire, Laser, 720 Laser, Wheelie Charge, Stoppie Spin, Judgement Day (below 30% HP).
 * Form 1 (below 50% HP - the hovering head formation): Carpet Bombing, Spread Fire, Eye Lasers.
 * "Emil heads" are fired as bouncing-light orb projectiles.
 */
public class EmilUnit extends AllyUnit {
    public static final int FIRE = 1, LASER = 2, LASER720 = 3, WHEELIE = 4, STOPPIE = 5, JUDGEMENT = 6, CARPET = 7, SPREAD = 8, EYES = 9, RISE = 10;
    static final DustParticleOptions RED = new DustParticleOptions(new Vector3f(1f, 0.15f, 0.15f), 2f);

    public EmilUnit(EntityType<? extends EmilUnit> t, Level l) { super(t, l); }
    @Override public double preferredRange() { return 6; }
    @Override protected @Nullable Component bossName() { return getDisplayName(); }
    @Override protected @Nullable Skill forcedSkill() { return getForm() == 0 && getHealth() < getMaxHealth() * 0.5f ? skillById(RISE) : null; }

    Vec3 headPos() { return at(0, getBbHeight() * 0.8, 0); }
    void head(Vec3 from, Vec3 dir, double speed, float dmg) { shoot(from, dir, speed).dmg(dmg).size(0.45).trail(ParticleTypes.END_ROD).life(40); }
    static Vec3 yaw(Vec3 d, double a) { return new Vec3(d.x * Math.cos(a) - d.z * Math.sin(a), d.y, d.x * Math.sin(a) + d.z * Math.cos(a)); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(FIRE, "Indiscriminate Fire", 40, 70, 0, 16, (u, t, i) -> {
                if (i >= 10 && i <= 34 && i % 2 == 0) { for (int k = 0; k < 3; k++) head(headPos(), yaw(new Vec3(0, 0.05, 1), i * 0.45 + k * Mth.TWO_PI / 3), 0.7, 4);
                    sound(SoundEvents.SHULKER_SHOOT, 0.6f, 1.5f); }
            }).forms(0, 0).weight(3),
            new Skill(LASER, "Laser", 60, 90, 3, 22, (u, t, i) -> {
                if (i < 20 && i % 2 == 0) fx(RED, headPos().add(fwd().scale(0.8)), 6, 0.3 - i * 0.01, 0);
                if (i == 20) sound(SoundEvents.BEACON_ACTIVATE, 1.2f, 1.8f);
                if (i >= 22 && i <= 52 && i % 2 == 0) beam(headPos().add(fwd().scale(0.6)), fwd().add(0, aimAt(t, headPos()).y, 0), 22, 3, RED);
            }).forms(0, 0).weight(2),
            new Skill(LASER720, "720 Laser", 70, 200, 0, 14, (u, t, i) -> {
                if (i < 18 && i % 2 == 0) fx(RED, headPos().add(fwd().scale(0.8)), 8, 0.4, 0);
                if (i <= 60) spin(i, 20, 18f);
                if (i >= 20 && i <= 60) { if (i % 2 == 0) beam(headPos().add(fwd().scale(0.6)), fwd().add(0, -0.08, 0), 18, 3, RED); }
            }).forms(0, 0).noTurn(),
            new Skill(WHEELIE, "Wheelie Charge", 30, 70, 3, 14, (u, t, i) -> {
                if (i == 12) { lungeTo(t, 1.7, 0.05); sound(SoundEvents.MINECART_RIDING, 1, 1.5f); }
                if (i >= 12 && i <= 20) arc(1.8, 80, 7, 1.5, 0.4);
            }).forms(0, 0),
            new Skill(STOPPIE, "Stoppie Spin", 40, 80, 0, 6, (u, t, i) -> {
                if (i <= 32) spin(i, 8, 30f);
                if (i >= 8 && i <= 32) { if (i % 4 == 0) { arc(2.8, 180, 5, 0.9, 0.2); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 0.7f); }
                    if (i % 8 == 0) lungeTo(t, 0.5, 0); }
            }).forms(0, 0),
            new Skill(JUDGEMENT, "Judgement Day", 100, 900, 0, 20, (u, t, i) -> {
                if (i < 85 && i % 3 == 0) { double r = 10 - i / 10.0; for (int k = 0; k < 8; k++) { double a = k * Math.PI / 4 + i * 0.1;
                    fx(RED, position().add(Math.cos(a) * r, 0.3, Math.sin(a) * r), 2, 0.1, 0); } fx(ParticleTypes.END_ROD, headPos(), 4, 0.5, 0.05); }
                if (i == 30 || i == 60) sound(SoundEvents.WARDEN_SONIC_CHARGE, 2, 0.6f);
                if (i == 85) { burst(position().add(0, 1, 0), 10, 40, 2.5, 0.8); fx(ParticleTypes.EXPLOSION_EMITTER, position(), 3, 2, 0);
                    sound(SoundEvents.WARDEN_SONIC_BOOM, 3, 0.7f); }
            }).forms(0, 0).noTurn().when(u -> u.getHealth() < u.getMaxHealth() * 0.3f),
            new Skill(CARPET, "Carpet Bombing", 60, 100, 0, 22, (u, t, i) -> {
                if (i >= 20 && i <= 50 && i % 2 == 0 && t != null) { Vec3 sky = t.position().add(random.nextGaussian() * 3, 12, random.nextGaussian() * 3);
                    shoot(sky, new Vec3(0, -1, 0), 0.8).gravity(0.03).boom(1.8).dmg(5).size(0.45).trail(ParticleTypes.END_ROD).life(40); }
                if (i == 20) sound(SoundEvents.ELYTRA_FLYING, 1, 1.5f);
            }).forms(1, 1).weight(2),
            new Skill(SPREAD, "Spread Fire", 30, 50, 2, 18, (u, t, i) -> {
                if (i == 12 || i == 20) { Vec3 d = aimAt(t, headPos()); for (int k = -3; k <= 3; k++) head(headPos(), yaw(d, k * 0.15), 0.9, 4);
                    sound(SoundEvents.SHULKER_SHOOT, 1, 1.1f); }
            }).forms(1, 1).weight(2),
            new Skill(EYES, "Eye Lasers", 56, 90, 3, 20, (u, t, i) -> {
                if (i < 20 && i % 2 == 0) { fx(RED, at(0.6, getBbHeight() * 0.85, 0.25), 3, 0.05, 0); fx(RED, at(0.6, getBbHeight() * 0.85, -0.25), 3, 0.05, 0); }
                if (i >= 20 && i <= 50 && i % 2 == 0) for (int s = -1; s <= 1; s += 2) { Vec3 e = at(0.6, getBbHeight() * 0.85, 0.25 * s); beam(e, aimAt(t, e), 20, 2, RED); }
            }).forms(1, 1),
            new Skill(RISE, "Formation", 40, 0, 0, 99, (u, t, i) -> {
                if (i % 4 == 0) fx(ParticleTypes.END_ROD, position().add(0, 1.5, 0), 12, 1.2, 0.05);
                if (i == 30) { setForm(1); sound(SoundEvents.BEACON_POWER_SELECT, 2, 0.6f); burst(position().add(0, 1, 0), 4, 4, 1.5, 0.4); }
            }).noTarget().noTurn()
        );
    }

    /** Head-formation phase uses this to stay airborne-looking (client hover handled by the model). */
    public boolean hovering() { return getForm() == 1; }
}
