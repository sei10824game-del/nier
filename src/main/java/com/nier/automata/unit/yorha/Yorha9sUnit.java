package com.nier.automata.unit.yorha;

import com.nier.automata.core.Faction;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Scanner-type android (S). Sword + greatsword combo, lunging spear, hacking (stun hack, remote hack-and-destroy
 * that is lethal against machines), pod gun and pod missile program, evade counter.
 */
public class Yorha9sUnit extends AndroidUnit {
    public static final int COMBO = 1, DUAL = 2, SPEAR = 3, HACK_STUN = 4, HACK_DESTROY = 5, POD_GUN = 6, POD_MISSILE = 7, EVADE = 8;

    public Yorha9sUnit(EntityType<? extends Yorha9sUnit> type, Level level) { super(type, level); }

    @Override public double preferredRange() { return 3.0; }

    static boolean machine(LivingEntity e) { return e instanceof NierUnit n && n.faction() == Faction.MACHINE; }

    /** Hacking visual: link stream to the target and a rotating wire cube around it (the hack "minigame"). */
    void hackFx(LivingEntity t, int k) {
        tracer(getEyePosition(), t.getEyePosition(), ParticleTypes.ENCHANT, 2);
        Vec3 c = t.position().add(0, t.getBbHeight() / 2, 0);
        double r = t.getBbWidth() + 0.4, a = k * 0.25;
        for (int i = 0; i < 4; i++) { double ang = a + i * Math.PI / 2;
            for (int y = -1; y <= 1; y += 2)
                sl().sendParticles(ParticleTypes.ELECTRIC_SPARK, c.x + Math.cos(ang) * r, c.y + y * r, c.z + Math.sin(ang) * r, 1, 0, 0, 0, 0); }
        if (k % 5 == 0) sound(SoundEvents.NOTE_BLOCK_BIT.value(), 0.6f, 1.2f + (k % 20) / 20f);
    }

    @Override protected List<Skill> createSkills() {
        return List.of(
            new Skill(COMBO, "sword_combo", 24, 25, 0, 3.5, (u, t, k) -> {
                if (k == 5 || k == 11) arc(3, 60, 4, 0.15);
                if (k == 16) lunge(0.7, 0);
                if (k == 18) arc(3.5, 30, 6, 0.5);
            }).weight(4),
            // sword + greatsword: rising cut, then double spin
            new Skill(DUAL, "dual_combo", 34, 70, 0, 4, (u, t, k) -> {
                if (k == 7) { arc(3.5, 70, 6, 0); if (t != null && distanceTo(t) < 4) t.setDeltaMovement(t.getDeltaMovement().add(0, 0.5, 0)); }
                if (k == 17 || k == 24) { aoe(position(), 3.8, 6, 0.1); ring(position(), 2.5, ParticleTypes.SWEEP_ATTACK); sound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.8f); }
            }).weight(3),
            // spear: dash, flurry of 3 jabs, piercing thrust
            new Skill(SPEAR, "spear_lunge", 28, 70, 2, 7, (u, t, k) -> {
                if (k == 8) lunge(1.4, 0.05);
                if (k == 10 || k == 12 || k == 14) arc(3.2, 35, 3.5, 0.1);
                if (k == 19) { lunge(0.5, 0); arc(4, 30, 7, 1.2); sl().sendParticles(ParticleTypes.CRIT, getX() + fwd().x * 3, getY() + 1.2, getZ() + fwd().z * 3, 12, 0.2, 0.2, 0.2, 0.3); }
            }).weight(2),
            // hack: 30-tick hack, then stun (and real damage to machines)
            new Skill(HACK_STUN, "hack", 40, 160, 2, 14, (u, t, k) -> {
                if (t == null) return;
                if (k < 30) hackFx(t, k);
                if (k == 30) {
                    boolean m = machine(t);
                    t.hurt(damageSources().indirectMagic(this, this), (m ? 8 : 3) * damageMult());
                    t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, m ? 80 : 40, m ? 6 : 3));
                    t.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, m ? 80 : 40, 1));
                    sl().sendParticles(ParticleTypes.FLASH, t.getX(), t.getY() + 1, t.getZ(), 1, 0, 0, 0, 0);
                    sound(SoundEvents.ZOMBIE_VILLAGER_CURE, 0.5f, 2f);
                }
            }).weight(2),
            // hack-and-destroy: long remote hack ending in a core detonation (35% max HP vs machines)
            new Skill(HACK_DESTROY, "hack_destroy", 60, 400, 2, 14, (u, t, k) -> {
                if (t == null) return;
                if (k < 50) { hackFx(t, k); if (k > 30) sl().sendParticles(ParticleTypes.SMOKE, t.getX(), t.getY() + 1, t.getZ(), 3, 0.3, 0.4, 0.3, 0.02); }
                if (k == 50) {
                    t.invulnerableTime = 0;
                    t.hurt(damageSources().indirectMagic(this, this), (machine(t) ? Math.max(12, t.getMaxHealth() * 0.35f) : 10) * damageMult());
                    sl().sendParticles(ParticleTypes.EXPLOSION_EMITTER, t.getX(), t.getY() + 1, t.getZ(), 1, 0, 0, 0, 0);
                    sound(SoundEvents.GENERIC_EXPLODE, 1.2f, 1.1f);
                }
            }),
            AndroidUnit.podGun(POD_GUN, 1.2f),
            // pod missile program: 4 homing rounds at t=10/14/18/22
            new Skill(POD_MISSILE, "pod_missile", 40, 140, 5, 20, (u, t, k) -> {
                if (t == null || k < 10 || k > 22 || (k - 10) % 4 != 0) return;
                Vec3 a = podPos(), b = t.position().add(0, 0.5, 0), mid = a.lerp(b, 0.5).add(0, 2.5, 0);
                for (int i = 0; i <= 20; i++) { double s = i / 20.0;
                    Vec3 q = a.lerp(mid, s).lerp(mid.lerp(b, s), s);
                    sl().sendParticles(ParticleTypes.FIREWORK, q.x, q.y, q.z, 1, 0, 0, 0, 0); }
                sl().sendParticles(ParticleTypes.EXPLOSION, b.x, b.y, b.z, 1, 0, 0, 0, 0);
                aoe(b, 1.8, 3.5, 0.2); sound(SoundEvents.FIREWORK_ROCKET_BLAST, 1, 1);
            }).weight(2),
            AndroidUnit.evade(EVADE)
        );
    }
}
