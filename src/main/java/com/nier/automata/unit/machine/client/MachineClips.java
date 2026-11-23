package com.nier.automata.unit.machine.client;

import static com.nier.automata.client.AnimClip.Ease.*;

import com.nier.automata.client.AnimClip;

/** Reusable multi-bone attack clips shared by machine models (bones: body, head, rightArm, leftArm, rightLeg, leftLeg, weapon). */
final class MachineClips {
    private MachineClips() {}

    /** Jump body-press: crouch until `launch`, airborne tuck, belly flop at `impact`. */
    public static AnimClip press(int len, int launch, int impact, float spread) {
        return AnimClip.of(len)
            .bone("body").rot(0, 0, 0, 0).rot(launch - 1, 0.35f, 0, 0).rot(launch + 1, -0.25f, 0, 0, OUT).rot(impact - 2, -0.3f, 0, 0).rot(impact, 0.55f, 0, 0, IN).rot(impact + 6, 0.4f, 0, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(launch - 1, 0, 2.5f, 0).pos(launch + 1, 0, -1.5f, 0, OUT).pos(impact - 1, 0, -1, 0).pos(impact, 0, 3, -1, IN).pos(impact + 6, 0, 2.5f, -1).pos(len, 0, 0, 0)
                .scale(0, 1, 1, 1).scale(launch - 1, 1.1f, 0.88f, 1.1f).scale(launch + 1, 0.92f, 1.1f, 0.92f).scale(impact - 1, 1, 1, 1).scale(impact + 1, 1.18f, 0.84f, 1.18f).scale(impact + 6, 1, 1, 1)
            .bone("rightArm").rot(0, 0, 0, 0).rot(launch - 1, 0.9f, 0, 0.2f).rot(launch + 2, -2.9f, 0, 0.35f, OUT).rot(impact - 1, -2.6f, 0, 0.5f).rot(impact, -0.3f - (1.3f - spread) * 0.3f, 0, spread, IN).rot(impact + 6, -0.2f, 0, spread * 0.85f).rot(len, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(launch - 1, 0.9f, 0, -0.2f).rot(launch + 2, -2.9f, 0, -0.35f, OUT).rot(impact - 1, -2.6f, 0, -0.5f).rot(impact, -0.3f - (1.3f - spread) * 0.3f, 0, -spread, IN).rot(impact + 6, -0.2f, 0, -spread * 0.85f).rot(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(launch - 1, 0.25f, 0, 0).rot(launch + 2, -0.4f, 0, 0).rot(impact - 1, -0.3f, 0, 0).rot(impact, 0.35f, 0, 0, IN).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(launch - 1, -0.5f, 0, 0.15f).rot(launch + 2, 0.6f, 0, 0).rot(impact - 3, -0.9f, 0, 0.2f).rot(impact, -0.2f, 0, 0.35f).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(launch - 1, -0.5f, 0, -0.15f).rot(launch + 2, 0.6f, 0, 0).rot(impact - 3, -0.9f, 0, -0.2f).rot(impact, -0.2f, 0, -0.35f).rot(len, 0, 0, 0).done();
    }

    /** Two-handed overhead weapon chop landing at `hit`; `bury` = weapon tip angle into the ground. */
    public static AnimClip overhead(int len, int hit, float raise, float bury, float weight, String weapon) {
        return AnimClip.of(len)
            .bone("rightArm").rot(0, 0, 0, 0).rot(hit - 2, raise, 0.2f, 0.15f).rot(hit - 1, raise - 0.1f, 0.2f, 0.15f).rot(hit, -0.15f, 0, 0, IN).rot(hit + 6, -0.2f, 0, 0).rot(len, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(hit - 2, raise + 0.4f, -0.5f, -0.2f).rot(hit, -0.5f, -0.6f, 0, IN).rot(hit + 6, -0.5f, -0.6f, 0).rot(len, 0, 0, 0)
            .bone(weapon).rot(0, 0, 0, 0).rot(hit - 2, -0.5f, 0, 0).rot(hit, bury, 0, 0, IN).rot(hit + 6, bury, 0, 0).rot(hit + 9, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(hit - 2, -0.35f * weight, 0.15f, 0).rot(hit, 0.5f * weight, -0.05f, 0, IN).rot(hit + 3, 0.45f * weight, 0, 0.04f).rot(hit + 4, 0.45f * weight, 0, -0.04f)
                .rot(hit + 6, 0.45f * weight, 0, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(hit - 2, 0, -1 * weight, 0.5f).pos(hit, 0, 1.8f * weight, -2, IN).pos(hit + 6, 0, 1.5f * weight, -1.5f).pos(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(hit - 2, -0.35f, 0, 0).rot(hit, 0.3f, 0, 0, IN).rot(hit + 6, 0.2f, 0, 0).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(hit - 2, 0.25f, 0, 0).rot(hit, -0.65f, 0, 0.1f, IN).rot(hit + 6, -0.6f, 0, 0.1f).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(hit - 2, -0.2f, 0, 0).rot(hit, 0.45f, 0, -0.1f, IN).rot(hit + 6, 0.4f, 0, -0.1f).rot(len, 0, 0, 0).done();
    }

    /** Horizontal right-to-left sweep landing at `hit`; `reach` scales the twist. */
    public static AnimClip sweep(int len, int hit, float reach, String weapon) {
        return AnimClip.of(len)
            .bone("body").rot(0, 0, 0, 0).rot(hit - 2, 0.1f, 0.9f * reach, 0).rot(hit, 0.15f, -0.9f * reach, 0, OUT).rot(hit + 4, 0.1f, -1.35f * reach, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(hit - 2, 0, 1, 0.5f).pos(hit, 0, 1, -1.5f, OUT).pos(len, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(hit - 2, -1.4f, 1.3f, 0.2f).rot(hit, -1.5f, -1.0f, 0, OUT).rot(hit + 4, -1.2f, -1.6f, 0).rot(len, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(hit - 2, -1.0f, 0.7f, 0).rot(hit, -1.1f, -0.4f, 0, OUT).rot(hit + 4, -0.6f, -0.6f, -0.3f).rot(len, 0, 0, 0)
            .bone(weapon).rot(0, 0, 0, 0).rot(hit - 2, 0, 0.4f, 0).rot(hit, 0, -0.3f, 0, OUT).rot(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(hit - 2, 0, -0.6f * reach, 0).rot(hit, 0.1f, 0.5f * reach, 0, OUT).rot(hit + 4, 0, 0.8f * reach, 0).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(hit - 2, 0.35f, 0, 0.1f).rot(hit, -0.4f, 0, 0.1f).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(hit - 2, -0.35f, 0, -0.1f).rot(hit, 0.3f, 0, -0.1f).rot(len, 0, 0, 0).done();
    }

    /** Both fists raised overhead on tiptoe, slam at `hit` (t14 in the original 36-tick timing). */
    public static AnimClip pound(int len, int hit) {
        return AnimClip.of(len)
            .bone("rightArm").rot(0, 0, 0, 0).rot(hit - 2, -3.0f, 0, 0.25f).rot(hit - 1, -3.05f, 0, 0.2f).rot(hit, -0.55f, 0, 0.15f, IN).rot(hit + 8, -0.6f, 0, 0.2f).rot(len, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(hit - 2, -3.0f, 0, -0.25f).rot(hit - 1, -3.05f, 0, -0.2f).rot(hit, -0.55f, 0, -0.15f, IN).rot(hit + 8, -0.6f, 0, -0.2f).rot(len, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(hit - 1, -0.35f, 0, 0).rot(hit, 0.6f, 0, 0, IN).rot(hit + 8, 0.55f, 0, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(hit - 1, 0, -1.5f, 0.5f).pos(hit, 0, 2.5f, -1, IN).pos(hit + 8, 0, 2, -1).pos(len, 0, 0, 0)
                .scale(hit - 1, 1, 1, 1).scale(hit + 1, 1.12f, 0.9f, 1.12f).scale(hit + 6, 1, 1, 1)
            .bone("head").rot(0, 0, 0, 0).rot(hit - 1, -0.5f, 0, 0).rot(hit, 0.35f, 0, 0, IN).rot(hit + 8, 0.3f, 0, 0).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(hit - 1, 0.2f, 0, 0.1f).rot(hit, -0.5f, 0, 0.25f).rot(hit + 8, -0.5f, 0, 0.25f).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(hit - 1, 0.2f, 0, -0.1f).rot(hit, -0.5f, 0, -0.25f).rot(hit + 8, -0.5f, 0, -0.25f).rot(len, 0, 0, 0).done();
    }

    /** Left-arm shield squared up, shoulder charge starting at `hit`. */
    public static AnimClip shieldBash(int len, int hit) {
        return AnimClip.of(len)
            .bone("leftArm").rot(0, 0, 0, 0).rot(hit - 1, -1.35f, -0.5f, 0).rot(hit + 2, -1.5f, -0.3f, 0, OUT).rot(hit + 6, -1.4f, -0.3f, 0).rot(len, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(hit - 1, 0.6f, 0, 0.3f).rot(hit + 2, 0.8f, 0, 0.4f).rot(len, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(hit - 1, 0.3f, 0.5f, 0).rot(hit + 2, 0.45f, 0.35f, 0, OUT).rot(hit + 6, 0.3f, 0.3f, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(hit - 1, 0, 1.5f, 1).pos(hit + 2, 0, 1, -3, OUT).pos(hit + 6, 0, 1, -2).pos(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(hit - 1, 0.1f, -0.4f, 0).rot(hit + 6, 0.1f, -0.3f, 0).rot(len, 0, 0, 0)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(hit - 1, 0.4f, 0, 0).rot(hit + 1, -0.8f, 0, 0).rot(hit + 3, 0.6f, 0, 0).rot(hit + 6, -0.3f, 0, 0).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(hit - 1, -0.5f, 0, 0).rot(hit + 1, 0.7f, 0, 0).rot(hit + 3, -0.6f, 0, 0).rot(hit + 6, 0.2f, 0, 0).rot(len, 0, 0, 0).done();
    }

    /** Right knee raised high, stamp at `hit`. */
    public static AnimClip stomp(int len, int hit) {
        return AnimClip.of(len)
            .bone("rightLeg").rot(0, 0, 0, 0).rot(hit - 3, -1.5f, 0, 0.1f).rot(hit - 1, -1.6f, 0, 0.1f).rot(hit, 0.05f, 0, 0, IN).rot(hit + 7, 0.05f, 0, 0).rot(len, 0, 0, 0)
            .bone("leftLeg").rot(0, 0, 0, 0).rot(hit - 1, 0.1f, 0, -0.05f).rot(hit, 0.35f, 0, -0.1f, IN).rot(hit + 7, 0.3f, 0, -0.1f).rot(len, 0, 0, 0)
            .bone("body").rot(0, 0, 0, 0).rot(hit - 1, -0.2f, 0, -0.15f).rot(hit, 0.25f, 0, 0.05f, IN).rot(hit + 7, 0.2f, 0, 0).rot(len, 0, 0, 0)
                .pos(0, 0, 0, 0).pos(hit - 1, 0, -1, 0).pos(hit, 0, 1.8f, 0, IN).pos(hit + 7, 0, 1.5f, 0).pos(len, 0, 0, 0)
            .bone("rightArm").rot(0, 0, 0, 0).rot(hit - 1, -0.6f, 0, 0.9f).rot(hit, 0.2f, 0, 0.5f).rot(len, 0, 0, 0)
            .bone("leftArm").rot(0, 0, 0, 0).rot(hit - 1, -0.6f, 0, -0.9f).rot(hit, 0.2f, 0, -0.5f).rot(len, 0, 0, 0)
            .bone("head").rot(0, 0, 0, 0).rot(hit - 1, -0.25f, 0, 0.1f).rot(hit, 0.3f, 0, 0).rot(hit + 7, 0.2f, 0, 0).rot(len, 0, 0, 0).done();
    }
}
