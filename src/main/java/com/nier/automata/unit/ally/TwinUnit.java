package com.nier.automata.unit.ally;

import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * The twin caster androids (Devola / Popola). Shared spellbook: magic bolt fan, Maledict (a ring of conjured
 * arrows that rain on the target), homing orbs, blink. Devola adds the healing Song; Popola adds Dark Lance and Barrier.
 */
public class TwinUnit extends AllyUnit {
    public static final int BOLTS = 1, MALEDICT = 2, ORBS = 3, BLINK = 4, SONG = 5, LANCE = 6, BARRIER = 7;
    private final boolean elder;

    protected TwinUnit(EntityType<? extends TwinUnit> t, Level l, boolean devola) { super(t, l); elder = devola; }
    public static TwinUnit devola(EntityType<? extends TwinUnit> t, Level l) { return new TwinUnit(t, l, true); }
    public static TwinUnit popola(EntityType<? extends TwinUnit> t, Level l) { return new TwinUnit(t, l, false); }

    @Override public double preferredRange() { return 9; }
    DustParticleOptions dust() { return new DustParticleOptions(elder ? new Vector3f(0.95f, 0.2f, 0.25f) : new Vector3f(0.45f, 0.3f, 1f), 1.2f); }

    @Override protected List<Skill> createSkills() {
        List<Skill> l = new ArrayList<>(List.of(
            new Skill(BOLTS, "Magic Bolts", 26, 30, 2, 18, (u, t, i) -> {
                if (i < 12 && i % 3 == 0) fx(dust(), at(0.6, 1.5, 0), 4, 0.25, 0);
                if (i == 14) { Vec3 h = at(0.8, 1.5, 0); Vec3 d = aimAt(t, h);
                    for (int k = -2; k <= 2; k++) { float a = k * 0.18f; shoot(h, new Vec3(d.x * Mth.cos(a) - d.z * Mth.sin(a), d.y, d.x * Mth.sin(a) + d.z * Mth.cos(a)), 1.1)
                        .dmg(3).trail(dust()).life(30); }
                    sound(SoundEvents.EVOKER_CAST_SPELL, 1, 1.4f); }
            }).weight(3),
            new Skill(MALEDICT, "Maledict", 44, 90, 3, 20, (u, t, i) -> {
                // 8 arrows conjured in a halo (t 6..20), then loosed one by one at the target (t 22..36)
                if (i >= 6 && i <= 20 && i % 2 == 0) fx(ParticleTypes.ENCHANT, halo(i / 2), 6, 0.1, 0.2);
                if (i >= 22 && i <= 36 && i % 2 == 0) { Vec3 p = halo((i - 22) / 2);
                    shoot(p, aimAt(t, p), 1.6).dmg(4).trail(ParticleTypes.END_ROD).homing(0.08, t).life(30).size(0.25);
                    sound(SoundEvents.ARROW_SHOOT, 0.7f, 1.6f); }
            }).weight(2),
            new Skill(ORBS, "Homing Orbs", 30, 80, 3, 20, (u, t, i) -> {
                if (i == 12 || i == 16 || i == 20) { Vec3 h = at(0.5, 2.2, (i - 16) / 4.0);
                    shoot(h, aimAt(t, h).add(0, 0.4, 0), 0.5).homing(0.12, t).dmg(5).trail(dust()).life(70).size(0.4).boom(1.2);
                    sound(SoundEvents.ILLUSIONER_CAST_SPELL, 0.8f, 1.2f); }
            }),
            new Skill(BLINK, "Blink", 12, 60, 0, 3, (u, t, i) -> {
                if (i == 4) { fx(ParticleTypes.PORTAL, position().add(0, 1, 0), 30, 0.4, 0.4); Vec3 b = at(-5, 0, random.nextBoolean() ? 3 : -3);
                    randomTeleport(b.x, b.y, b.z, true); fx(ParticleTypes.PORTAL, position().add(0, 1, 0), 30, 0.4, 0.4); sound(SoundEvents.ENDERMAN_TELEPORT, 0.7f, 1.4f); }
            }).noTurn()));
        if (elder) l.add(new Skill(SONG, "Song", 50, 260, 0, 40, (u, t, i) -> {
            if (i % 6 == 0) fx(ParticleTypes.NOTE, position().add(0, 2.2, 0), 2, 0.5, 1);
            if (i == 30) { healAllies(12, 8); for (LivingEntity a : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(12), this::isAlliedTo))
                a.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 0)); }
        }).noTurn());
        else {
            l.add(new Skill(LANCE, "Dark Lance", 30, 70, 3, 22, (u, t, i) -> {
                if (i < 16 && i % 2 == 0) fx(ParticleTypes.SQUID_INK, at(0.8, 1.6, 0), 3, 0.15, 0.01);
                if (i == 18) { Vec3 h = at(0.8, 1.5, 0); beam(h, aimAt(t, h), 22, 9, ParticleTypes.SQUID_INK); sound(SoundEvents.WITHER_SHOOT, 0.8f, 1.5f); }
            }).weight(2));
            l.add(new Skill(BARRIER, "Barrier", 30, 300, 0, 40, (u, t, i) -> {
                if (i == 16) for (LivingEntity a : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10), e -> e == this || isAlliedTo(e))) {
                    a.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1)); fx(dust(), a.position().add(0, 1, 0), 20, 0.6, 0); }
            }).noTurn());
        }
        return l;
    }
    Vec3 halo(int k) { double a = k * Math.PI / 4 + tickCount * 0.02; return position().add(Math.cos(a) * 1.6, 3.0, Math.sin(a) * 1.6); }
}
