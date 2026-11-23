"""Attacker android A (original design): long auburn hair + red headband, tan cloth wraps, iron gauntlets, amber eyes.
Writes a2.png and a2_berserk.png (form 1: red glow veins, red eyes, burning hair tips)."""
from android_base import body, sword, great, back_great, spear, new, save, SKIN

HAIR, WRAP, IRON, BAND = (112, 46, 30), (128, 100, 72), (58, 58, 64), (170, 40, 36)


def paint(berserk=False):
    t = new()
    eye = (255, 40, 30) if berserk else (230, 160, 40)
    body(t, dict(hair=HAIR, suit=WRAP, trim=(90, 70, 50), legs=SKIN, boots=(60, 48, 38), eye=eye,
                 pod=(186, 150, 86), podDark=(118, 90, 48), podEye=(255, 120, 60), glove=IRON))
    t.rect(36, 4, 8, 2, SKIN)                                    # bare shoulders
    for y in (7, 9, 12, 14): t.rect(36, y, 8, 1, (100, 78, 54))  # wrap bands
    for u in (56, 72): t.rect(u, 4, 14, 8, SKIN)                 # bare upper arms
    for u in (88, 104):
        for y in (6, 8): t.rect(u, y, 16, 1, WRAP)               # thigh wraps
    t.box(0, 32, 9, 4, 9, HAIR, noise=8)                         # hair cap
    t.box(56, 32, 9, 1, 1, BAND)                                 # headband
    t.box(36, 32, 9, 14, 1, HAIR, noise=9)                       # long back hair
    t.box(76, 32, 2, 8, 1, BAND, noise=6)                        # scarf tail
    t.box(84, 32, 4, 7, 5, IRON, noise=5); t.rect(89, 37, 4, 1, BAND)       # left gauntlet
    t.box(102, 32, 4, 7, 5, IRON, noise=5); t.rect(107, 37, 4, 1, BAND)     # right gauntlet
    sword(t, 0, 48, 15, (190, 196, 204), (240, 240, 250), hilt=(70, 30, 26))
    great(t, 20, (74, 70, 68), BAND)
    back_great(t, 24, (74, 70, 68), BAND)
    spear(t, (96, 70, 50), (200, 204, 210))
    if berserk:
        import random
        r = random.Random(3)
        for _ in range(70):   # glowing red veins on skin/wraps/gauntlets
            x, y = r.randrange(32, 122), r.randrange(4, 44)
            if t.px[x, y][3]: t.rect(x, y, 1, r.randrange(1, 3), (255, 30, 20))
        for x in range(36, 56): t.rect(x, 45, 1, 2, (255, 60, 30))        # burning hair tips
        t.rect(10, 11, 1, 1, (255, 0, 0)); t.rect(13, 11, 1, 1, (255, 0, 0))
    return t


save(paint(), 'a2')
save(paint(True), 'a2_berserk')
