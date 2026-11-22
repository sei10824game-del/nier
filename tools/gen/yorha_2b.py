"""Battle android B (original design): navy plated suit, ochre trim, ash-violet braid, crimson visor band, ochre pod.
Writes yorha_2b.png and yorha_2b_overload.png (form 1: scorched, glowing cracks, broken visor)."""
from android_base import body, sword, great, back_great, new, save, SKIN

NAVY, OCHRE, HAIR, VISOR = (30, 36, 54), (201, 163, 90), (172, 160, 192), (190, 30, 44)


def paint(over=False):
    t = new()
    body(t, dict(hair=HAIR, suit=NAVY, trim=OCHRE, legs=(24, 28, 42), boots=(18, 20, 28), eye=(120, 60, 160),
                 pod=(190, 152, 84), podDark=(120, 92, 48), podEye=(255, 120, 60)))
    # chest plate
    t.rect(37, 6, 6, 3, (52, 60, 84))
    # hair cap (8,32) 9x4x9 + visor band (44,32) 9x2x1
    t.box(8, 32, 9, 4, 9, HAIR, noise=7)
    t.box(44, 32, 9, 2, 1, VISOR, noise=3); t.rect(45, 33, 9, 1, (255, 90, 90))
    # braid (0,32) 2x12x2 with bands
    t.box(0, 32, 2, 12, 2, HAIR, noise=8)
    for y in (36, 40, 44): t.rect(0, y, 8, 1, (140, 128, 160))
    t.rect(0, 45, 8, 1, OCHRE)
    # tassets: front (64,32) 9x7x1, back (84,32) 9x8x1
    t.box(64, 32, 9, 7, 1, NAVY, noise=4); t.rect(65, 39, 9, 1, OCHRE); t.rect(69, 33, 1, 7, OCHRE)
    t.box(84, 32, 9, 8, 1, NAVY, noise=4); t.rect(85, 40, 9, 1, OCHRE)
    # weapons
    sword(t, 0, 48, 14, (205, 210, 220), (255, 150, 60))
    great(t, 20, (62, 64, 72), OCHRE)
    back_great(t, 24, (62, 64, 72), OCHRE)
    if over:   # scorch, cracks with orange glow, shattered visor revealing a glowing eye
        import random
        r = random.Random(7)
        for _ in range(500):
            x, y = r.randrange(0, 128), r.randrange(0, 80)
            px = t.px[x, y]
            if px[3]: t.px[x, y] = tuple(int(c * 0.65) for c in px[:3]) + (255,)
        for _ in range(60):
            x, y = r.randrange(32, 120), r.randrange(4, 16)
            for i in range(r.randrange(2, 5)):
                t.rect(x + i, y + r.choice((-1, 0, 1)), 1, 1, (255, 140 - i * 20, 30))
        t.rect(48, 33, 3, 2, (40, 20, 20)); t.rect(13, 11, 1, 1, (255, 170, 40))
        t.rect(8, 16, 4, 1, (255, 140, 30))  # glowing seam at pod
    return t


save(paint(), 'yorha_2b')
save(paint(True), 'yorha_2b_overload')
