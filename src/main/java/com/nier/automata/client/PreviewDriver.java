package com.nier.automata.client;

import com.mojang.blaze3d.platform.Window;
import com.nier.automata.Modules;
import com.nier.automata.core.NierUnit;
import com.nier.automata.core.Skill;
import com.nier.automata.core.UnitModule;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;

/**
 * DEV ONLY (-Dnier.preview=1): walks every unit in a flat world and saves screenshots (idle + frames of each skill) to run/screenshots.
 * Optional: -Dnier.preview.ids=a,b  -Dnier.preview.skills=N
 */
public final class PreviewDriver {
    private record Step(int delay, Runnable run) {}
    private static final Deque<Step> queue = new ArrayDeque<>();
    private static int wait = 0, start = 0, idx = -1;
    private static NierUnit cur;
    private static boolean built;
    private static volatile boolean pending;
    private static void srv(Minecraft mc, Runnable r) { pending = true; mc.getSingleplayerServer().execute(() -> { try { r.run(); } finally { pending = false; } }); }

    private PreviewDriver() {}

    public static void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getSingleplayerServer() == null) return;
        if (mc.player.isDeadOrDying()) { mc.player.respawn(); return; }
        if (++start < 400) return;
        mc.options.hideGui = true;
        if (!built) { built = true; build(mc); }
        if (pending) return;
        if (wait > 0) { wait--; return; }
        Step s = queue.poll();
        if (s == null) { if (queue.isEmpty()) { System.out.println("PREVIEW DONE"); mc.stop(); } return; }
        s.run.run(); wait = s.delay;
    }

    private static void build(Minecraft mc) {
        Set<String> only = new HashSet<>();
        String ids = System.getProperty("nier.preview.ids", "");
        if (!ids.isEmpty()) only.addAll(Arrays.asList(ids.split(",")));
        int maxSkills = Integer.getInteger("nier.preview.skills", 3);
        queue.add(new Step(5, () -> srv(mc, () -> { ServerLevel lv = mc.getSingleplayerServer().overworld(); lv.setDayTime(6000); lv.getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_DAYLIGHT).set(false, mc.getSingleplayerServer()); })));
        int n = 0;
        for (UnitModule<?> m : Modules.ALL) {
            if (!only.isEmpty() && !only.contains(m.id())) continue;
            final int num = n++;
            queue.add(new Step(25, () -> spawn(mc, m, num)));
            queue.add(new Step(4, () -> shot(mc, String.format("%02d_%s_idle", num, m.id()))));
            if (maxSkills > 0) queue.add(new Step(0, () -> planSkills(mc, m, num, maxSkills)));
        }
    }

    /** Skills are only known once the entity exists, so they are queued lazily (inserted at the queue head in order). */
    private static void planSkills(Minecraft mc, UnitModule<?> m, int num, int max) {
        if (cur == null) return;
        Deque<Step> add = new ArrayDeque<>();
        int j = 0;
        for (Skill sk : cur.skills()) {
            if (j++ >= max) break;
            final int jj = j;
            final int d = sk.duration;
            add.add(new Step(Math.max(1, (int) (d * 0.30)), () -> srv(mc, () -> { if (cur != null) cur.startSkill(sk); })));
            add.add(new Step(Math.max(1, (int) (d * 0.25)), () -> shot(mc, String.format("%02d_%s_s%d_a", num, m.id(), jj))));
            add.add(new Step(Math.max(1, (int) (d * 0.25)), () -> shot(mc, String.format("%02d_%s_s%d_b", num, m.id(), jj))));
            add.add(new Step(Math.max(1, d), () -> srv(mc, () -> { if (cur != null) cur.endSkill(); })));
        }
        // push in front, preserving order
        Step[] arr = add.toArray(new Step[0]);
        for (int i = arr.length - 1; i >= 0; i--) queue.addFirst(arr[i]);
    }

    private static void spawn(Minecraft mc, UnitModule<?> m, int num) {
        srv(mc, () -> {
            ServerLevel lv = mc.getSingleplayerServer().overworld();
            if (cur != null) cur.discard();
            java.util.List<net.minecraft.world.entity.Entity> all = new java.util.ArrayList<>(); lv.getAllEntities().forEach(all::add);
            for (var en : all) if (en != null && !(en instanceof net.minecraft.world.entity.player.Player)) en.discard();
            var p = mc.getSingleplayerServer().getPlayerList().getPlayers().get(0);
            double gy = lv.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p.blockPosition()).getY();
            @SuppressWarnings("unchecked") EntityType<? extends NierUnit> t = (EntityType<? extends NierUnit>) m.type.get();
            NierUnit u = t.create(lv);
            float h = u.getBbHeight() * (float) u.getScale();
            double d = Math.max(2.0, (u.getBbHeight() * 1.9 + 1.4) * Double.parseDouble(System.getProperty("nier.preview.dist", "1")));
            u.setNoAi(true); u.setInvulnerable(true); u.setPersistenceRequired();
            u.moveTo(0.5, gy, 0.5 + d, 180f, 0f); u.setYHeadRot(180f); u.setYBodyRot(180f);
            lv.addFreshEntity(u); cur = u;
            p.setNoGravity(true); p.setInvulnerable(true);
            p.teleportTo(lv, 0.5, gy, 0.5, java.util.Set.of(), 0f, 0f);
            double eye = 1.62, mid = Math.min(u.getBbHeight(), 12) * 0.5 + (u.getBbHeight() > 3 ? 0.5 : 0);
            float pitch = (float) Math.toDegrees(Math.atan2(eye - mid, d));
            p.setXRot(Math.max(-30, Math.min(30, pitch))); p.setYRot(0f);
        });
    }

    private static void shot(Minecraft mc, String name) {
        System.out.println("SHOT " + name + " cur=" + (cur == null ? null : cur.isRemoved() + " hp=" + cur.getHealth() + " pos=" + cur.position()) + " client=" + (cur == null ? null : mc.level.getEntity(cur.getId())) + " player=" + mc.player.position() + " yaw=" + mc.player.getYRot());
        File dir = new File(mc.gameDirectory, "preview"); dir.mkdirs();
        Screenshot.grab(mc.gameDirectory, "../preview/" + name + ".png", mc.getMainRenderTarget(), c -> {});
    }
}
