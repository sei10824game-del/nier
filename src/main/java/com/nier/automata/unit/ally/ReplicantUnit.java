package com.nier.automata.unit.ally;

import com.nier.automata.core.Fx;
import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * NieR Replicant roster sharing one kit class; the kit is fixed per entity type:
 * 0 Nier (brother): sword + Weiss' dark magic, 1 Nier (father): greatsword, 2 Kaine: twin swords, 3 Emil: pure dark magic.
 */
public class ReplicantUnit extends AllyUnit {
    public static final int COMBO = 1, DASH = 2, BLAST = 3, LANCE = 4, EVADE = 5, HEAVY = 6, SPIN = 7, DUAL = 8, DANCE = 9, RING = 10, BEAM = 11, BLAST_E = 12;
    private static final int DARK = 0x7a3cff;
    private final int kit;

    public ReplicantUnit(EntityType<? extends ReplicantUnit> t, Level l, int kit) { super(t, l); this.kit = kit; }
    @Override protected int variants() { return 1; }
    @Override public int getForm() { return kit; }
    @Override public double preferredRange() { return kit >= 3 ? 8 : 2.2; }
    @Override protected net.minecraft.network.chat.Component bossName() { return kit >= 4 ? net.minecraft.network.chat.Component.translatable(getType().getDescriptionId()) : null; }

    private void darkBlast(Vec3 from, Vec3 dir, float dmg, double speed) {
        shoot(from, dir, speed).dmg(dmg).trail(Fx.fade(0xffffff, DARK, 1.6f)).life(40).boom(1.4);
        fx(ParticleTypes.ENCHANT, from, 6, 0.3, 0.2);
    }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(COMBO, "Sword Combo", 28, 30, 0, 3.2, (u, t, i) -> {
                if (i == 6 || i == 12 || i == 18) { arc(3.2, 70, 5, 0.3, 0.05); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.3f); lunge(0.25, 0); }
                if (i == 23) { arc(3.4, 50, 9, 0.8, 0.25); sound(SoundEvents.PLAYER_ATTACK_STRONG, 1, 0.9f); }
            }).forms(0, 0).weight(3),
            new Skill(DASH, "Dash Strike", 18, 60, 4, 10, (u, t, i) -> {
                if (i == 8) lungeTo(t, 1.6, 0.1);
                if (i == 12) { arc(3, 80, 8, 0.6, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.1f); }
            }).forms(0, 2),
            new Skill(BLAST, "Dark Blast", 36, 70, 3, 22, (u, t, i) -> {
                if (i < 14 && i % 3 == 0) Fx.burst(level(), at(0.5, 1.5, 0), DARK, 3);
                if (i == 14 || (kit >= 3 && (i == 18 || i == 22))) { Vec3 m = at(0.8, 1.5, 0); darkBlast(m, aimAt(t, m), kit >= 3 ? 7 : 5, 1.2); sound(SoundEvents.EVOKER_CAST_SPELL, 1, 1.2f); }
            }).forms(0, 0).weight(2),
            new Skill(BLAST_E, "Dark Blast", 36, 60, 3, 22, (u, t, i) -> {
                if (i < 14 && i % 3 == 0) Fx.burst(level(), at(0.5, 1.5, 0), DARK, 3);
                if (i == 14 || i == 18 || i == 22) { Vec3 m = at(0.8, 1.5, 0); darkBlast(m, aimAt(t, m).add(random.nextGaussian() * 0.05, 0, random.nextGaussian() * 0.05), 7, 1.2); sound(SoundEvents.EVOKER_CAST_SPELL, 1, 1.2f); }
            }).forms(3, 6).weight(3),
            new Skill(LANCE, "Dark Lance", 44, 100, 4, 22, (u, t, i) -> {
                if (i == 14 && t != null) for (int k = -1; k <= 1; k++) { Vec3 src = t.position().add(k * 2.0, 6, 0); shoot(src, new Vec3(0, -1, 0), 1.4).dmg(6).trail(Fx.fade(0xffffff, DARK, 1.4f)).life(10).boom(1.2); fx(ParticleTypes.ENCHANT, src, 6, 0.4, 0.2); }
                if (i == 14) sound(SoundEvents.TRIDENT_THROW, 1, 0.7f);
            }).when(u -> u.getForm() <= 1 || u.getForm() >= 4).weight(2),
            new Skill(EVADE, "Evade", 10, 80, 0, 2.5, (u, t, i) -> { if (i == 1) evade(0.9); }).noTurn(),
            new Skill(HEAVY, "Heavy Strike", 36, 50, 0, 4.2, (u, t, i) -> {
                if (i == 18) { arc(4.2, 90, 14, 1.4, 0.4); fx(ParticleTypes.EXPLOSION, at(2.5, 0.2, 0), 1, 0, 0); sound(SoundEvents.GENERIC_EXPLODE, 0.6f, 1.2f); }
            }).forms(1, 1).weight(3),
            new Skill(SPIN, "Whirlwind", 36, 90, 0, 4, (u, t, i) -> {
                if (i <= 28) spin(i, 10, 36f);
                if (i >= 10 && i <= 28 && i % 6 == 4) { arc(4, 180, 7, 0.8, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.8f); }
            }).forms(1, 1).weight(2),
            new Skill(DUAL, "Twin Blade Flurry", 24, 30, 0, 3, (u, t, i) -> {
                if (i == 4 || i == 8 || i == 12 || i == 16 || i == 20) { arc(3, 75, 3.5f, 0.2, 0); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.8f, 1.5f); lunge(0.2, 0); }
            }).forms(2, 2).weight(3),
            new Skill(DANCE, "Blade Dance", 30, 80, 0, 3.6, (u, t, i) -> {
                if (i <= 24) spin(i, 8, 45f);
                if (i >= 8 && i <= 24 && i % 4 == 0) { arc(3.6, 180, 5, 0.6, 0.1); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 1.4f); }
            }).forms(2, 2).weight(2),
            new Skill(RING, "Dark Ring", 40, 90, 0, 16, (u, t, i) -> {
                if (i < 16 && i % 3 == 0) Fx.shock(level(), position(), 1 + i * 0.2, DARK);
                if (i == 18) for (int k = 0; k < 10; k++) { double a = k * Math.PI / 5; darkBlast(at(0, 1.2, 0), new Vec3(Math.cos(a), 0.05, Math.sin(a)), 5, 0.9); }
                if (i == 18) sound(SoundEvents.EVOKER_CAST_SPELL, 1, 0.6f);
            }).forms(3, 6).weight(2),
            new Skill(BEAM, "Dark Beam", 44, 110, 4, 22, (u, t, i) -> {
                if (i >= 18 && i <= 34 && i % 4 == 0) { Vec3 f = at(0.6, 1.5, 0); beam(f, aimAt(t, f), 20, 5, ParticleTypes.END_ROD); }
            }).forms(3, 6).weight(2)
        );
    }
}
