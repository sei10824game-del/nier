package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** YoRHa Heavy Armament: armoured gunner with a sustained minigun, a mortar-style cannon and a rifle-butt bash. */
public class YorhaHeavyUnit extends YorhaSoldierUnit {
    public static final int CANNON = 9;

    public YorhaHeavyUnit(EntityType<? extends YorhaSoldierUnit> t, Level l) { super(t, l); }
    @Override protected int variants() { return 1; }
    @Override public int getForm() { return 2; }
    @Override public double preferredRange() { return 11; }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(BURST, "Minigun Barrage", 44, 70, 3, 22, (u, t, i) -> {
                if (i >= 10 && i <= 38 && i % 2 == 0) { Vec3 m = at(1.0, 1.35, -0.3);
                    shoot(m, aimAt(t, m).add(random.nextGaussian() * 0.04, random.nextGaussian() * 0.02, random.nextGaussian() * 0.04), 2.4).dmg(2.5f).trail(ParticleTypes.CRIT).life(16);
                    fx(ParticleTypes.SMOKE, m, 1, 0.05, 0); if (i % 4 == 0) sound(SoundEvents.CROSSBOW_SHOOT, 0.7f, 1.5f); }
            }).weight(3),
            new Skill(CANNON, "Heavy Cannon", 40, 110, 6, 24, (u, t, i) -> {
                if (i < 14 && i % 3 == 0) fx(ParticleTypes.FLAME, at(1.0, 1.35, -0.3), 2, 0.1, 0.02);
                if (i == 16) { Vec3 m = at(1.0, 1.4, -0.3); shoot(m, aimAt(t, m), 1.6).dmg(9).boom(2.6).trail(ParticleTypes.FLAME).life(30);
                    fx(ParticleTypes.EXPLOSION, m, 1, 0, 0); sound(SoundEvents.GENERIC_EXPLODE, 0.7f, 1.4f); lunge(-0.3, 0); }
            }).weight(2),
            new Skill(BASH, "Gun Butt", 16, 30, 0, 2.5, (u, t, i) -> {
                if (i == 9) { arc(2.8, 70, 7, 1.3, 0.25); sound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 1, 0.8f); }
            }),
            new Skill(POD_GUN, "Pod Machine Gun", 30, 50, 2, 20, (u, t, i) -> {
                if (i >= 6 && i <= 26 && i % 2 == 0) { Vec3 p = podPos(); shoot(p, aimAt(t, p), 2.5).dmg(1.5f).trail(ParticleTypes.ELECTRIC_SPARK).life(14); }
            })
        );
    }
}
