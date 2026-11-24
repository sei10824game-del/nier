package com.nier.automata.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base of every NieR unit. Hostility rules: Machines attack players, Androids and every non-NieR mob;
 * Androids attack Machines and every non-NieR mob (not players unless hit); every non-NieR mob attacks
 * NieR units (see NierEvents). Attacks are {@link Skill}s driven by {@link NierCombatGoal}.
 */
public abstract class NierUnit extends PathfinderMob {
    private static final EntityDataAccessor<Integer> SKILL_ID = SynchedEntityData.defineId(NierUnit.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FORM = SynchedEntityData.defineId(NierUnit.class, EntityDataSerializers.INT);

    /** Ticks since the current skill started (kept in sync client+server). */
    public int skillTime;
    private List<Skill> skills;
    private final java.util.Map<Integer, Integer> cooldowns = new java.util.HashMap<>();
    private ServerBossEvent bossBar;

    protected NierUnit(EntityType<? extends NierUnit> type, Level level) { super(type, level); }

    public abstract Faction faction();
    /** All skills of this unit. Called once. */
    protected abstract List<Skill> createSkills();
    /** Distance the unit tries to keep to its target while no skill fits (2 = melee, larger = ranged). */
    public double preferredRange() { return 2.0; }
    /** Return a skill to force-start now (e.g. phase transition). Checked every tick, ignores cooldown/range. */
    @Nullable protected Skill forcedSkill() { return null; }
    /** Display name used for boss bar; null = no boss bar. */
    @Nullable protected Component bossName() { return null; }

    @Override protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SKILL_ID, 0);
        entityData.define(FORM, 0);
    }
    public int getSkillId() { return entityData.get(SKILL_ID); }
    public void setSkillId(int id) { entityData.set(SKILL_ID, id); }
    public int getForm() { return entityData.get(FORM); }
    public void setForm(int f) { entityData.set(FORM, f); }

    public List<Skill> skills() { if (skills == null) skills = new ArrayList<>(createSkills()); return skills; }
    public @Nullable Skill skillById(int id) { for (Skill s : skills()) if (s.id == id) return s; return null; }
    public @Nullable Skill activeSkill() { return getSkillId() == 0 ? null : skillById(getSkillId()); }
    public boolean isCasting() { return getSkillId() != 0; }

    public void startSkill(Skill s) { skillTime = 0; setSkillId(s.id); }
    public void endSkill() {
        Skill s = activeSkill();
        if (s != null) cooldowns.put(s.id, s.cooldown);
        setSkillId(0); skillTime = 0;
    }
    public boolean onCooldown(Skill s) { return cooldowns.getOrDefault(s.id, 0) > 0; }

    @Override public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SKILL_ID.equals(key)) skillTime = 0;
    }

    @Override public void tick() {
        super.tick();
        if (getSkillId() != 0) skillTime++;
    }

    @Override public void aiStep() {
        super.aiStep();
        if (!level().isClientSide) {
            cooldowns.replaceAll((k, v) -> Math.max(0, v - 1));
            if (bossBar != null) bossBar.setProgress(getHealth() / getMaxHealth());
            if (tickCount % 5 == 0 && !isCasting()) retarget();
        }
    }

    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new NierCombatGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, NierUnit.class).setAlertOthers());
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, this::isEnemy));
    }

    /** Turn the whole body (not just the head) toward e by at most maxStep degrees this tick, so skills aimed along getYRot() really hit the target. */
    public void faceTarget(LivingEntity e, float maxStep) {
        double dx = e.getX() - getX(), dz = e.getZ() - getZ();
        if (dx * dx + dz * dz < 1e-4) return;
        float want = (float) (net.minecraft.util.Mth.atan2(dz, dx) * (180 / Math.PI)) - 90f;
        float yaw = net.minecraft.util.Mth.approachDegrees(getYRot(), want, maxStep);
        setYRot(yaw); yBodyRot = yaw; yHeadRot = yaw;
    }

    /** Switch to whoever is actually hurting us / is closest, so a unit never keeps chasing a far target while being beaten. */
    private void retarget() {
        LivingEntity t = getTarget();
        LivingEntity atk = getLastHurtByMob();
        boolean far = t == null || !t.isAlive() || distanceToSqr(t) > 36;
        if (atk != null && atk.isAlive() && atk != t && tickCount - getLastHurtByMobTimestamp() < 60 && canAttack(atk)
                && (far || distanceToSqr(atk) < distanceToSqr(t))) { setTarget(atk); return; }
        if (far) {
            LivingEntity best = null; double bd = 49;
            for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(7), e -> e != this && e.isAlive() && isEnemy(e) && canAttack(e) && hasLineOfSight(e))) {
                double d = distanceToSqr(e); if (d < bd) { bd = d; best = e; }
            }
            if (best != null && best != t) setTarget(best);
        }
    }

    public boolean isEnemy(LivingEntity e) {
        if (e instanceof NierUnit n) return n.faction() != faction();
        if (e instanceof Player p) return faction() == Faction.MACHINE && !p.isCreative() && !p.isSpectator();
        return e instanceof Mob;
    }
    @Override public boolean isAlliedTo(Entity e) {
        return (e instanceof NierUnit n && n.faction() == faction()) || super.isAlliedTo(e);
    }
    @Override public boolean canAttack(LivingEntity e) { return !isAlliedTo(e) && super.canAttack(e); }

    // ---- boss bar ----
    @Override public void startSeenByPlayer(ServerPlayer p) {
        super.startSeenByPlayer(p);
        Component n = bossName();
        if (n != null) {
            if (bossBar == null) bossBar = new ServerBossEvent(n, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
            bossBar.addPlayer(p);
        }
    }
    @Override public void stopSeenByPlayer(ServerPlayer p) { super.stopSeenByPlayer(p); if (bossBar != null) bossBar.removePlayer(p); }

    @Override public void addAdditionalSaveData(CompoundTag t) { super.addAdditionalSaveData(t); t.putInt("Form", getForm()); }
    @Override public void readAdditionalSaveData(CompoundTag t) { super.readAdditionalSaveData(t); setForm(t.getInt("Form")); }

    // package-private hooks for the goal
    @Nullable Skill pickForced() { return forcedSkill(); }
}
