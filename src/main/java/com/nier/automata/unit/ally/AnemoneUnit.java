package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Resistance leader. Sabre in the right hand, carbine slung: sabre combo, rising slash, aimed volley, sweeping covering fire. */
public class AnemoneUnit extends AllyUnit {
    public static final int COMBO = 1, RISING = 2, VOLLEY = 3, COVER = 4;

    public AnemoneUnit(EntityType<? extends AnemoneUnit> t, Level l) { super(t, l); }
    @Override public double preferredRange() { return 3; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(COMBO, "Sabre Combo", 26, 25, 0, 3.2, (u, t, i) -> {
                if (i == 6 || i == 12) { arc(3.2, 70, 5, 0.3, 0.05); lunge(0.3, 0); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.2f); }
                if (i == 19) { arc(3.2, 55, 8, 0.9, 0.2); sound(SoundEvents.PLAYER_ATTACK_STRONG, 1, 1.1f); }
            }).weight(3),
            new Skill(RISING, "Rising Slash", 20, 60, 0, 3, (u, t, i) -> {
                if (i == 12) { arc(3, 60, 7, 0.2, 0.9); setDeltaMovement(0, 0.5, 0); hurtMarked = true; sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 1.3f); }
            }),
            new Skill(VOLLEY, "Carbine Volley", 30, 40, 4, 20, (u, t, i) -> {
                if (i == 10 || i == 15 || i == 20) { Vec3 m = at(1.0, 1.35, 0.3); shoot(m, aimAt(t, m), 2.6).dmg(5).trail(ParticleTypes.CRIT).life(15);
                    fx(ParticleTypes.SMOKE, m, 3, 0.05, 0); sound(SoundEvents.CROSSBOW_SHOOT, 0.9f, 1.4f); }
            }).weight(2),
            new Skill(COVER, "Covering Fire", 40, 120, 3, 16, (u, t, i) -> {
                if (i >= 8 && i <= 34 && i % 2 == 0) { float sweep = (i - 21) / 13f * 0.6f; Vec3 m = at(1.0, 1.35, 0.3);
                    Vec3 d = aimAt(t, m); d = new Vec3(d.x * Math.cos(sweep) - d.z * Math.sin(sweep), d.y, d.x * Math.sin(sweep) + d.z * Math.cos(sweep));
                    shoot(m, d, 2.4).dmg(2).trail(ParticleTypes.CRIT).life(14); sound(SoundEvents.CROSSBOW_SHOOT, 0.5f, 2f); }
            })
        );
    }
}
