"""YoRHa No.2 Type B fan model: white bob, black blindfold, black gothic dress with white trim, thigh-high boots, Pod 042, Virtuous Contract."""
from android_base import body, sword, great, back_great, new, save, SKIN

BLACK, TRIM, HAIR = (20, 20, 24), (232, 232, 238), (236, 236, 242)


def paint(over=False):
    t = new()
    body(t, dict(hair=HAIR, suit=BLACK, trim=TRIM, legs=BLACK, boots=(12, 12, 16), eye=BLACK,
                 pod=(70, 72, 80), podDark=(40, 42, 50), podEye=(255, 140, 60)))
    t.rect(8, 11, 24, 3, (10, 10, 12))                       # blindfold wraps head front/sides/back
    t.rect(0, 11, 8, 3, (10, 10, 12)); t.rect(16, 11, 8, 3, (10, 10, 12))
    t.rect(14, 14, 1, 1, (90, 60, 60))                        # beauty mark
    t.rect(88, 4, 32, 3, SKIN); t.rect(88, 7, 32, 1, TRIM)    # bare thighs, boot trim
    t.rect(36, 4, 8, 1, TRIM); t.rect(36, 10, 5, 1, TRIM)
    t.box(8, 32, 9, 4, 9, HAIR, noise=4)
    t.box(44, 32, 9, 3, 1, (10, 10, 12), noise=0)
    t.box(0, 96, 9, 6, 1, HAIR, noise=4); t.box(24, 96, 1, 6, 9, HAIR, noise=4); t.box(48, 96, 1, 6, 9, HAIR, noise=4)
    t.box(0, 32, 2, 1, 2, HAIR)
    t.box(64, 32, 10, 5, 1, BLACK, noise=2); t.rect(65, 37, 22, 1, TRIM)
    t.box(88, 32, 10, 5, 1, BLACK, noise=2); t.rect(89, 37, 22, 1, TRIM)
    t.box(100, 96, 1, 5, 5, BLACK, noise=2); t.box(100, 110, 1, 5, 5, BLACK, noise=2)
    sword(t, 0, 48, 14, (230, 232, 240), (255, 255, 255))
    great(t, 20, (225, 228, 238), (30, 30, 36))
    back_great(t, 24, (225, 228, 238), (30, 30, 36))
    if over:
        import random
        r = random.Random(7)
        for _ in range(500):
            x, y = r.randrange(0, 128), r.randrange(0, 80)
            px = t.px[x, y]
            if px[3]: t.px[x, y] = tuple(int(c * 0.65) for c in px[:3]) + (255,)
        for _ in range(60):
            x, y = r.randrange(32, 120), r.randrange(4, 16)
            for i in range(r.randrange(2, 5)): t.rect(x + i, y + r.choice((-1, 0, 1)), 1, 1, (255, 140 - i * 20, 30))
        t.rect(8, 11, 8, 3, (60, 20, 20)); t.rect(10, 12, 1, 1, (255, 60, 40)); t.rect(13, 12, 1, 1, (255, 60, 40))  # blindfold torn
    return t


save(paint(), 'yorha_2b')
save(paint(True), 'yorha_2b_overload')
