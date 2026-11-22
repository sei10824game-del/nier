package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Emil, hacked: the head grafted onto a hulking combat frame. Haymaker, Ground Slam shockwave, Head Barrage, Mega Laser. */
public class EmilHackedUnit extends AllyUnit {
    public static final int HAYMAKER = 1, SLAM = 2, BARRAGE = 3, MEGA_LASER = 4;

    public EmilHackedUnit(EntityType<? extends EmilHackedUnit> t, Level l) { super(t, l); }
    @Override public double preferredRange() { return 3; }
    @Override protected @Nullable Component bossName() { return getDisplayName(); }
    Vec3 headPos() { return at(0.3, getBbHeight() * 0.85, 0); }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(HAYMAKER, "Haymaker", 30, 30, 0, 4, (u, t, i) -> {
                if (i == 10) { arc(4, 70, 9, 1.0, 0.2); sound(SoundEvents.IRON_GOLEM_ATTACK, 1, 0.7f); lunge(0.4, 0); }
                if (i == 20) { arc(4, 70, 12, 1.8, 0.5); fx(ParticleTypes.EXPLOSION, at(2, 1.2, 0), 1, 0, 0); sound(SoundEvents.IRON_GOLEM_ATTACK, 1.2f, 0.5f); }
            }).weight(3),
            new Skill(SLAM, "Ground Slam", 36, 80, 0, 7, (u, t, i) -> {
                if (i == 8) { setDeltaMovement(0, 0.7, 0); hurtMarked = true; }
                if (i == 20) { burst(position(), 4, 14, 1.6, 0.7); fx(ParticleTypes.EXPLOSION_EMITTER, position(), 1, 0, 0); sound(SoundEvents.GENERIC_EXPLODE, 1.5f, 0.6f); }
                if (i > 20 && i <= 30 && i % 2 == 0) { double r = 4 + (i - 20) * 0.5;      // expanding shockwave ring
                    for (int k = 0; k < 16; k++) { double a = k * Math.PI / 8; fx(ParticleTypes.CLOUD, position().add(Math.cos(a) * r, 0.2, Math.sin(a) * r), 1, 0.05, 0); }
                    burst(position(), r, 3, 0.8, 0.4); }
            }).weight(2),
            new Skill(BARRAGE, "Head Barrage", 36, 60, 3, 18, (u, t, i) -> {
                if (i >= 10 && i <= 28 && i % 3 == 1) { Vec3 d = aimAt(t, headPos()); double a = (i % 2 == 0 ? 1 : -1) * 0.15 * ((i - 10) / 3 % 3);
                    shoot(headPos(), EmilUnit.yaw(d, a), 1.0).dmg(5).size(0.5).trail(ParticleTypes.END_ROD).life(40).boom(1.2); sound(SoundEvents.SHULKER_SHOOT, 1, 0.8f); }
            }).weight(2),
            new Skill(MEGA_LASER, "Mega Laser", 64, 140, 3, 26, (u, t, i) -> {
                if (i < 24 && i % 2 == 0) fx(EmilUnit.RED, headPos().add(fwd().scale(1)), 10, 0.6 - i * 0.02, 0);
                if (i == 24) sound(SoundEvents.WARDEN_SONIC_BOOM, 1.5f, 1.2f);
                if (i >= 26 && i <= 56 && i % 2 == 0) { Vec3 h = headPos().add(fwd().scale(0.8)); beam(h, aimAt(t, h), 26, 5, EmilUnit.RED);
                    beam(h.add(0, 0.25, 0), aimAt(t, h), 26, 0, EmilUnit.RED); }
            })
        );
    }
}
