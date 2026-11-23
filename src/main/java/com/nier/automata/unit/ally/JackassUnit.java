package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Resistance demolitions researcher: cluster bombs, shoulder rocket launcher, planted demolition charge, boot to the face. */
public class JackassUnit extends AllyUnit {
    public static final int CLUSTER = 1, ROCKET = 2, CHARGE = 3, KICK = 4;
    private Vec3 planted;

    public JackassUnit(EntityType<? extends JackassUnit> t, Level l) { super(t, l); }
    @Override public double preferredRange() { return 8; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(CLUSTER, "Cluster Bombs", 30, 80, 3, 14, (u, t, i) -> {
                if (i == 16 && t != null) { Vec3 h = at(0.3, 1.9, -0.3); double d = distanceTo(t);
                    for (int k = -1; k <= 1; k++) { Vec3 dir = t.position().subtract(h).normalize().add(right().scale(k * 0.18)).add(0, 0.35, 0);
                        shoot(h, dir, Math.min(1.1, 0.35 + d * 0.06)).gravity(0.05).boom(2.2).dmg(6).trail(ParticleTypes.SMOKE).life(60).size(0.2); }
                    sound(SoundEvents.SNOWBALL_THROW, 1, 0.6f); }
            }).weight(2),
            new Skill(ROCKET, "Rocket Launcher", 36, 100, 5, 24, (u, t, i) -> {
                if (i < 20 && i % 4 == 0) fx(ParticleTypes.SMOKE, at(0.2, 2.0, -0.5), 2, 0.1, 0);
                if (i == 22) { Vec3 m = at(0.9, 1.9, -0.45); shoot(m, aimAt(t, m), 1.3).homing(0.04, t).boom(3.5).dmg(12).trail(ParticleTypes.FLAME).life(50);
                    fx(ParticleTypes.LARGE_SMOKE, at(-1.0, 1.9, -0.45), 8, 0.2, 0.05); sound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.4f, 0.7f); }
            }),
            new Skill(CHARGE, "Demolition Charge", 44, 160, 0, 4, (u, t, i) -> {
                if (i == 10) { planted = at(1.0, 0.1, 0); sound(SoundEvents.TNT_PRIMED, 1, 1.2f); }
                if (i == 14) evade(1.1);
                if (planted != null && i > 10 && i % 3 == 0) fx(ParticleTypes.SMOKE, planted, 2, 0.05, 0.01);
                if (i == 38 && planted != null) { burst(planted, 4, 16, 1.4, 0.6); fx(ParticleTypes.EXPLOSION_EMITTER, planted, 1, 0, 0);
                    level().playSound(null, planted.x, planted.y, planted.z, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 2, 0.9f); planted = null; }
            }).noTurn(),
            new Skill(KICK, "Front Kick", 16, 25, 0, 2.4, (u, t, i) -> {
                if (i == 8) { arc(2.4, 50, 4, 1.3, 0.25); sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 0.9f); }
            })
        );
    }
}
