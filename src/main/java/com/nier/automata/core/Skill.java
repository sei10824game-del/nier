package com.nier.automata.core;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;

/**
 * One attack / action of a unit. id >= 1 (0 = none). The same id keys the client AnimClip, so the
 * animation always matches the server logic frame by frame (t = ticks since start, 0..duration-1).
 */
public final class Skill {
    public interface Tick { void run(NierUnit self, @Nullable LivingEntity target, int t); }

    public final int id;
    public final String name;
    public final int duration, cooldown;
    public final double minRange, maxRange;
    public int weight = 1;
    /** If true the unit does not auto-face the target while the skill runs. */
    public boolean noTurn = false;
    /** If true the unit can use it with no target (e.g. transformation). */
    public boolean needsTarget = true;
    public Predicate<NierUnit> condition = u -> true;
    public final Tick tick;

    public Skill(int id, String name, int duration, int cooldown, double minRange, double maxRange, Tick tick) {
        this.id = id; this.name = name; this.duration = duration; this.cooldown = cooldown;
        this.minRange = minRange; this.maxRange = maxRange; this.tick = tick;
    }
    public Skill weight(int w) { this.weight = w; return this; }
    public Skill noTurn() { this.noTurn = true; return this; }
    public Skill noTarget() { this.needsTarget = false; return this; }
    public Skill when(Predicate<NierUnit> p) { this.condition = p; return this; }
    /** Convenience: restrict to a form range (inclusive). */
    public Skill forms(int min, int max) { return when(u -> u.getForm() >= min && u.getForm() <= max); }
}
