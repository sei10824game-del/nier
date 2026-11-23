package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;
import static com.nier.automata.unit.machine.SphereUnit.*;

import com.nier.automata.client.AnimClip;
import com.nier.automata.client.NierModel;
import com.nier.automata.unit.machine.MachineUnit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Sphere: armoured ball with a single eye, side fins and a top fin. Rolls when moving. */
public class SphereModel extends NierModel<MachineUnit> {
    private static final float PI = Mth.PI;

    public SphereModel(ModelPart root) {
        super(root);
        bind(MachineGeo.SPHERE_PATHS);

        // Rolling Ram: fins fold, spin-up 0-9, roll 10-29 (3 turns), brake wobble
        clip(ROLL, AnimClip.of(40)
            .bone("ball").rot(0, 0, 0, 0).rot(9, 2 * PI, 0, 0, IN).rot(29, 8 * PI, 0, 0, LINEAR).rot(30, 0, 0, 0.15f, SNAP).rot(33, 0, 0, -0.12f).rot(36, 0, 0, 0.06f).rot(40, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(8, 0, 0.5f, 1).pos(10, 0, 0, 0).pos(40, 0, 0, 0)
            .bone("rightFin").scale(0, 1, 1, 1).scale(8, 1, 0.5f, 0.5f).scale(29, 1, 0.5f, 0.5f).scale(33, 1, 1, 1)
            .bone("leftFin").scale(0, 1, 1, 1).scale(8, 1, 0.5f, 0.5f).scale(29, 1, 0.5f, 0.5f).scale(33, 1, 1, 1)
            .bone("topFin").scale(0, 1, 1, 1).scale(8, 0.4f, 1, 0.4f).scale(29, 0.4f, 1, 0.4f).scale(33, 1, 1, 1).done());

        // Bounce Slam: squash 0-7, stretch launch t8 + forward flip, squash impact ~t20, ripple
        clip(BOUNCE, AnimClip.of(34)
            .bone("ball").rot(0, 0, 0, 0).rot(8, 0, 0, 0).rot(19, 2 * PI, 0, 0).rot(20, 0, 0, 0, SNAP)
                .pos(0, 0, 0, 0).pos(7, 0, 2, 0).pos(9, 0, -2, 0, OUT).pos(19, 0, -1, 0).pos(20, 0, 2.5f, 0, IN).pos(26, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(7, 1.22f, 0.78f, 1.22f).scale(9, 0.85f, 1.2f, 0.85f).scale(18, 1, 1, 1).scale(20, 1.3f, 0.7f, 1.3f).scale(23, 0.95f, 1.06f, 0.95f).scale(27, 1, 1, 1)
            .bone("rightFin").rot(0, 0, 0, 0).rot(7, 0, 0, -0.5f).rot(9, 0, 0, 0.6f).rot(20, 0, 0, 1.0f).rot(28, 0, 0, 0)
            .bone("leftFin").rot(0, 0, 0, 0).rot(7, 0, 0, 0.5f).rot(9, 0, 0, -0.6f).rot(20, 0, 0, -1.0f).rot(28, 0, 0, 0).done());

        // Orb Burst: fins flare wide, core swells 0-11, pulses t12 and t20 while turning
        clip(BURST, AnimClip.of(32)
            .bone("ball").rot(0, 0, 0, 0).rot(11, 0, 0, 0).rot(24, 0, PI / 2, 0, LINEAR).rot(32, 0, PI / 2, 0)
                .scale(0, 1, 1, 1).scale(11, 1.08f, 1.08f, 1.08f).scale(12, 1.18f, 1.18f, 1.18f).scale(15, 1.02f, 1.02f, 1.02f).scale(20, 1.18f, 1.18f, 1.18f).scale(24, 1, 1, 1)
            .bone("rightFin").rot(0, 0, 0, 0).rot(10, 0, 0.9f, 0).rot(12, 0, 1.3f, 0, OUT).rot(16, 0, 0.9f, 0).rot(20, 0, 1.3f, 0, OUT).rot(32, 0, 0, 0)
            .bone("leftFin").rot(0, 0, 0, 0).rot(10, 0, -0.9f, 0).rot(12, 0, -1.3f, 0, OUT).rot(16, 0, -0.9f, 0).rot(20, 0, -1.3f, 0, OUT).rot(32, 0, 0, 0)
            .bone("topFin").rot(0, 0, 0, 0).rot(10, -0.6f, 0, 0).rot(12, 0.4f, 0, 0).rot(20, -0.4f, 0, 0).rot(32, 0, 0, 0).done());

        // Fin Spin: fins lock out horizontally 0-5, three spins 6-25 (hits every 5), wobble
        clip(FIN_SPIN, AnimClip.of(30)
            .bone("ball").rot(0, 0, 0, 0).rot(6, 0, 0, 0).rot(25, 0, 6 * PI, 0, LINEAR).rot(26, 0, 0, 0.12f, SNAP).rot(28, 0, 0, -0.08f).rot(30, 0, 0, 0)
            .bone("rightFin").rot(0, 0, 0, 0).rot(5, 0, 0, 1.5f).rot(25, 0, 0, 1.5f).rot(30, 0, 0, 0)
            .bone("leftFin").rot(0, 0, 0, 0).rot(5, 0, 0, -1.5f).rot(25, 0, 0, -1.5f).rot(30, 0, 0, 0).done());

        // Drill Dive: tuck + drill spools up 0-13, plunge nose-down 14-27 with drill spinning, recover
        clip(DRILL, AnimClip.of(44).blend(3)
            .bone("ball").rot(0, 0, 0, 0).rot(10, -0.3f, 0, 0).rot(14, -0.6f, 0, 0, OUT).rot(20, 2.6f, 0, 0, IN).rot(28, 3.14f, 0, 0).rot(34, 3.3f, 0, 0).rot(44, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(10, 0, 1.5f, 0).pos(14, 0, -1, 0).pos(28, 0, 0, 0).pos(44, 0, 0, 0)
            .bone("drill").rot(0, 0, 0, 0).rot(13, 0, 6 * PI, 0, IN).rot(44, 0, 30 * PI, 0, LINEAR)
            .bone("rightFin").rot(0, 0, 0, 0).rot(14, 0, 0, 0.9f).rot(30, 0, 0, 0.9f).rot(44, 0, 0, 0)
            .bone("leftFin").rot(0, 0, 0, 0).rot(14, 0, 0, -0.9f).rot(30, 0, 0, -0.9f).rot(44, 0, 0, 0).done());

        // Saw Rush: blade spools up while the ball tilts forward 0-9, charges with the saw whirling and the ball tilting into the cut
        clip(SAW, AnimClip.of(40).blend(3)
            .bone("ball").rot(0, 0, 0, 0).rot(9, -0.5f, 0, 0).rot(12, 0.5f, 0, 0, OUT).rot(32, 0.5f, 0, 0).rot(40, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(9, 0, 1.2f, 1).pos(12, 0, 0, -1, OUT).pos(40, 0, 0, 0)
            .bone("saw").rot(0, 0, 0, 0).rot(9, 0, 8 * PI, 0, IN).rot(40, 0, 40 * PI, 0, LINEAR)
            .bone("rightFin").rot(0, 0, 0, 0).rot(10, 0, 0, 0.9f).rot(32, 0, 0, 0.9f).rot(40, 0, 0, 0)
            .bone("leftFin").rot(0, 0, 0, 0).rot(10, 0, 0, -0.9f).rot(32, 0, 0, -0.9f).rot(40, 0, 0, 0).done());

        // Self-Destruct: overheating shudder while rushing 0-39, swelling 40-49
        AnimClip sd = AnimClip.of(50).blend(2);
        AnimClip.Bone b = sd.bone("ball").pos(0, 0, 0, 0).scale(0, 1, 1, 1).scale(39, 1.05f, 1.05f, 1.05f).scale(49, 1.5f, 1.5f, 1.5f);
        for (int t = 1; t <= 49; t++) b.pos(t, (t % 2 == 0 ? 0.4f : -0.4f) * (t > 39 ? 2 : 1), 0, 0);
        AnimClip.Bone rf = sd.bone("rightFin").rot(0, 0, 0, 0), lf = sd.bone("leftFin").rot(0, 0, 0, 0);
        for (int t = 2; t <= 48; t += 2) { rf.rot(t, 0, 0, t % 4 == 0 ? 0.6f : -0.2f); lf.rot(t, 0, 0, t % 4 == 0 ? -0.6f : 0.2f); }
        clip(SELF_DESTRUCT, sd);
    }

    @Override protected void idle(MachineUnit e, float ls, float la, float age, float headYaw, float headPitch) {
        ModelPart ball = bone("ball");
        ball.xRot = ls * 0.9f;                          // rolls as it travels
        ball.yRot = headYaw * Mth.DEG_TO_RAD * (1 - la);
        ball.y += Mth.sin(age * 0.2f) * 0.3f * (1 - la);
        bone("rightFin").zRot = Mth.sin(age * 0.25f) * 0.12f; bone("leftFin").zRot = -Mth.sin(age * 0.25f) * 0.12f;
        bone("topFin").xRot = Mth.sin(age * 0.18f) * 0.1f;
        boolean drill = e.v.weapon() == com.nier.automata.unit.machine.MachineVariant.Weapon.DRILL;
        bone("drill").xScale = bone("drill").yScale = bone("drill").zScale = drill ? 1 : 0;
        boolean saw = e.v.weapon() == com.nier.automata.unit.machine.MachineVariant.Weapon.SAW;
        bone("saw").xScale = bone("saw").yScale = bone("saw").zScale = saw ? 1 : 0;
        if (saw && e.getSkillId() == 0) bone("saw").yRot = age * 0.4f;
        if (drill && e.getSkillId() == 0) bone("drill").yRot = age * 0.4f;
    }
}
