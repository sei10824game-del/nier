package com.nier.automata.core;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/** Chases the target, picks a weighted-random eligible skill, runs its tick() every game tick. */
public class NierCombatGoal extends Goal {
    private final NierUnit u;
    public NierCombatGoal(NierUnit u) { this.u = u; setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }

    @Override public boolean canUse() { return u.isCasting() || u.getTarget() != null && u.getTarget().isAlive() || u.pickForced() != null; }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public void stop() { if (u.isCasting()) u.endSkill(); u.getNavigation().stop(); }

    @Override public void tick() {
        LivingEntity t = u.getTarget();
        Skill active = u.activeSkill();
        if (active == null) {
            Skill forced = u.pickForced();
            if (forced != null) { u.startSkill(forced); active = forced; }
        }
        if (active != null) {
            if (t != null && !active.noTurn) u.getLookControl().setLookAt(t, 60f, 60f);
            active.tick.run(u, t, u.skillTime);
            if (u.skillTime + 1 >= active.duration) u.endSkill();
            return;
        }
        if (t == null) return;
        u.getLookControl().setLookAt(t, 30f, 30f);
        double d = u.distanceTo(t);
        List<Skill> ok = new ArrayList<>();
        int total = 0;
        for (Skill s : u.skills()) {
            if (u.onCooldown(s) || d < s.minRange || d > s.maxRange || !s.condition.test(u)) continue;
            if (s.maxRange > 3 && !u.getSensing().hasLineOfSight(t)) continue;
            ok.add(s); total += s.weight;
        }
        if (!ok.isEmpty()) {
            int r = u.getRandom().nextInt(total);
            for (Skill s : ok) { r -= s.weight; if (r < 0) { u.startSkill(s); u.getNavigation().stop(); return; } }
        }
        if (d > u.preferredRange()) u.getNavigation().moveTo(t, 1.0); else u.getNavigation().stop();
    }
}
