package com.nier.automata.unit.boss;

import com.nier.automata.core.Faction;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Shared base of the machine bosses: boss bar, HP-threshold form changes (invulnerable while transforming), no fall damage. */
public abstract class BossUnit extends NierUnit {
    private record Phase(float hp, int form, Skill skill) {}
    private final List<Phase> phases = new ArrayList<>();

    protected BossUnit(EntityType<? extends NierUnit> type, Level level) {
        super(type, level);
        xpReward = 200;
        setPersistenceRequired();
    }

    @Override public Faction faction() { return Faction.MACHINE; }
    @Override protected Component bossName() { return getType().getDescription(); }

    /** Registers a transformation: when HP <= hp*max and form < form, the skill is forced. Returns the skill (never picked randomly). */
    protected Skill phase(float hp, int form, Skill s) { phases.add(new Phase(hp, form, s)); return s.noTarget().noTurn().when(u -> false); }

    @Override @Nullable protected Skill forcedSkill() {
        if (isCasting()) return null;
        for (Phase p : phases) if (getForm() < p.form && getHealth() <= getMaxHealth() * p.hp) return p.skill;
        return null;
    }

    private boolean transforming() {
        Skill a = activeSkill();
        if (a == null) return false;
        for (Phase p : phases) if (p.skill == a) return true;
        return false;
    }

    /** Damage multiplier taken (override for armoured phases). */
    protected float damageTakenMult() { return 1f; }

    @Override public boolean hurt(DamageSource src, float amount) {
        if (transforming() && !src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.hurt(src, amount * damageTakenMult());
    }

    @Override public boolean causeFallDamage(float d, float m, DamageSource s) { return false; }
    @Override public boolean removeWhenFarAway(double d) { return false; }
}
