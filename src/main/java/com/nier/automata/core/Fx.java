package com.nier.automata.core;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Shared server-side visual effects: crescent slashes, layered beams, shockwave rings, impact bursts.
 * Everything is built from vanilla particles so it needs no client code; all methods are no-ops on the client.
 */
public final class Fx {
    private Fx() {}

    /** Colour themes (rgb). */
    public static final int YORHA = 0xe8e0ff, YORHA_DEEP = 0x8a5cff, MACHINE = 0xff3a2a, MACHINE_HOT = 0xffb040, HACK = 0x40e8ff, BLOOD = 0xc01020;

    public static DustParticleOptions dust(int rgb, float size) {
        return new DustParticleOptions(new Vector3f(((rgb >> 16) & 255) / 255f, ((rgb >> 8) & 255) / 255f, (rgb & 255) / 255f), size);
    }
    public static DustColorTransitionOptions fade(int from, int to, float size) {
        return new DustColorTransitionOptions(new Vector3f(((from >> 16) & 255) / 255f, ((from >> 8) & 255) / 255f, (from & 255) / 255f),
                new Vector3f(((to >> 16) & 255) / 255f, ((to >> 8) & 255) / 255f, (to & 255) / 255f), size);
    }

    private static void p(ServerLevel l, ParticleOptions o, Vec3 v, int n, double sp, double speed) {
        l.sendParticles(o, v.x, v.y, v.z, n, sp, sp, sp, speed);
    }

    /**
     * Crescent slash in front of an entity. center = shoulder height point, yaw = facing (deg), tiltDeg = roll of the slash plane
     * (0 horizontal sweep, 90 vertical chop), arcDeg = sweep angle, reach = radius, dir = +1/-1 sweep direction.
     */
    public static void slash(Level lv, Vec3 center, float yawDeg, double tiltDeg, double arcDeg, double reach, int rgb, int dir) {
        if (!(lv instanceof ServerLevel l)) return;
        double yaw = Math.toRadians(yawDeg), tilt = Math.toRadians(tiltDeg);
        Vec3 fwd = new Vec3(-Math.sin(yaw), 0, Math.cos(yaw)), side = new Vec3(Math.cos(yaw), 0, Math.sin(yaw));
        int steps = Math.max(10, (int) (arcDeg / 5));
        DustColorTransitionOptions body = fade(0xffffff, rgb, 1.5f);
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps, a = Math.toRadians((t - 0.5) * arcDeg) * dir;
            double thick = 1 - Math.abs(t - 0.5) * 1.6;                       // crescent: thick middle, thin tips
            for (int k = 0; k < 2; k++) {
                double r = reach * (0.82 + 0.18 * (k == 0 ? 1 : thick * 0.6 + 0.4));
                Vec3 flat = fwd.scale(Math.cos(a) * r).add(side.scale(Math.sin(a) * r));
                // roll the plane about the forward axis: split the sideways part between horizontal and vertical
                Vec3 off = fwd.scale(fwd.dot(flat)).add(side.scale(side.dot(flat) * Math.cos(tilt))).add(0, side.dot(flat) * Math.sin(tilt), 0);
                Vec3 pt = center.add(off);
                if (k == 0) p(l, body, pt, 1, 0.02, 0);
                else if (thick > 0.35) p(l, ParticleTypes.END_ROD, pt, 1, 0.01, 0.01);
            }
            if (i % 3 == 0) p(l, ParticleTypes.CRIT, center.add(fwd.scale(Math.cos(a) * reach)).add(side.scale(Math.sin(a) * reach * Math.cos(tilt))).add(0, Math.sin(a) * reach * Math.sin(tilt), 0), 1, 0.05, 0.1);
        }
    }

    /** Layered beam: bright core line, spiralling outer strand and sparks; flash at both ends. */
    public static void beam(Level lv, Vec3 from, Vec3 to, int rgb, double radius) {
        if (!(lv instanceof ServerLevel l)) return;
        Vec3 d = to.subtract(from); double len = d.length(); if (len < 0.1) return;
        Vec3 dir = d.scale(1 / len);
        Vec3 up = Math.abs(dir.y) > 0.9 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
        Vec3 u = dir.cross(up).normalize(), v = dir.cross(u).normalize();
        DustParticleOptions core = dust(0xffffff, 1.3f), glow = dust(rgb, 1.8f), edge = dust(rgb, 0.8f);
        int n = (int) (len * 3);
        long time = lv.getGameTime();
        for (int i = 0; i <= n; i++) {
            double s = i / (double) n; Vec3 c = from.add(d.scale(s));
            p(l, core, c, 1, 0, 0);
            if (i % 2 == 0) p(l, glow, c, 1, radius * 0.25, 0);
            for (int k = 0; k < 2; k++) {                                           // double helix
                double a = s * len * 2.2 + time * 0.7 + k * Math.PI;
                p(l, edge, c.add(u.scale(Math.cos(a) * radius)).add(v.scale(Math.sin(a) * radius)), 1, 0, 0);
            }
            if (i % 5 == 0) p(l, ParticleTypes.ELECTRIC_SPARK, c.add(u.scale((l.random.nextDouble() - 0.5) * radius * 2)), 1, 0.05, 0.05);
        }
        p(l, ParticleTypes.END_ROD, from, 5, 0.12, 0.06);
        burst(lv, to, rgb, 6);
    }

    /** Expanding ground shockwave ring (one frame: call per tick with growing radius, or once with a final radius). */
    public static void shock(Level lv, Vec3 c, double r, int rgb) {
        if (!(lv instanceof ServerLevel l)) return;
        DustColorTransitionOptions o = fade(0xffffff, rgb, 1.6f);
        int n = Mth.clamp((int) (r * 10), 12, 80);
        for (int i = 0; i < n; i++) {
            double a = i * Math.PI * 2 / n;
            Vec3 pt = c.add(Math.cos(a) * r, 0.12, Math.sin(a) * r);
            p(l, o, pt, 1, 0, 0);
            if (i % 3 == 0) p(l, ParticleTypes.CLOUD, pt, 1, 0.05, 0.02);
        }
    }

    /** Radial spark burst + flash. */
    public static void burst(Level lv, Vec3 c, int rgb, int n) {
        if (!(lv instanceof ServerLevel l)) return;
        p(l, fade(0xffffff, rgb, 1.1f), c, n, 0.3, 0);
        p(l, ParticleTypes.CRIT, c, n, 0.3, 0.35);
        p(l, ParticleTypes.ELECTRIC_SPARK, c, n / 2, 0.2, 0.4);
    }

    /** Vertical column of light with rising motes. */
    public static void pillar(Level lv, Vec3 base, double height, double r, int rgb) {
        if (!(lv instanceof ServerLevel l)) return;
        DustColorTransitionOptions o = fade(0xffffff, rgb, 1.4f);
        long time = lv.getGameTime();
        for (double y = 0; y < height; y += 0.5)
            for (int k = 0; k < 3; k++) {
                double a = time * 0.3 + y * 0.8 + k * Math.PI * 2 / 3;
                p(l, o, base.add(Math.cos(a) * r, y, Math.sin(a) * r), 1, 0, 0);
            }
        p(l, ParticleTypes.END_ROD, base.add(0, height * 0.5, 0), 4, r * 0.5, 0.05);
    }
}
