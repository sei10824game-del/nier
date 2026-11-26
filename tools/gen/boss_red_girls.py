import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(net):
    t = Tex(64, 64, seed=18)
    DRESS = (96, 8, 18) if net else (176, 28, 40)
    HAIR, BLACK, SKIN = (22, 20, 26), (18, 16, 20), (240, 226, 222)
    GLOW = (255, 50, 70)
    t.box(0, 0, 5, 5, 5, SKIN)                                    # head: pale skin
    t.face(0, 0, 5, 5, 5, 'front', 0, 0, 5, 2, HAIR)              # blunt bangs
    t.face(0, 0, 5, 5, 5, 'front', 1, 2, 1, 1, GLOW if net else (90, 120, 200)); t.face(0, 0, 5, 5, 5, 'front', 3, 2, 1, 1, GLOW if net else (90, 120, 200))
    t.face(0, 0, 5, 5, 5, 'front', 1, 4, 3, 1, (150, 60, 70))     # smile
    for f in ('right', 'left', 'back', 'top'): t.face(0, 0, 5, 5, 5, f, 0, 0, 5, 3 if f != 'top' else 5, HAIR)
    t.box(20, 0, 6, 11, 2, HAIR, noise=4); t.box(36, 0, 1, 9, 4, HAIR, noise=4)   # long hair
    t.strands(22, 2, 12, 11, HAIR); t.strands(36, 4, 10, 9, HAIR)
    t.rect(6, 6, 1, 1, (60, 50, 60)); t.rect(8, 6, 1, 1, (60, 50, 60))             # brows
    t.rect(6, 7, 1, 1, (250, 250, 252) if not net else (255, 170, 180)); t.rect(8, 7, 1, 1, (250, 250, 252) if not net else (255, 170, 180))
    t.rect(7, 8, 1, 1, tuple(int(c * 0.85) for c in SKIN)); t.rect(7, 9, 1, 1, (186, 90, 100)); t.rect(5, 8, 1, 1, tuple(int(c * 0.93) for c in SKIN)); t.rect(9, 8, 1, 1, tuple(int(c * 0.93) for c in SKIN))
    t.box(0, 48, 7, 1, 7, (255, 40, 60), top=(255, 90, 100))             # glyph halo
    t.box(20, 40, 6, 1, 6, (210, 20, 34))                          # red headband
    t.box(0, 16, 4, 7, 3, DRESS); t.face(0, 16, 4, 7, 3, 'front', 0, 0, 4, 1, BLACK); t.face(0, 16, 4, 7, 3, 'front', 1, 1, 2, 1, (214, 176, 84))   # collar + brooch
    t.box(0, 26, 6, 5, 5, DRESS)                                  # flared skirt
    for f in ('front', 'back', 'left', 'right'): t.face(0, 26, 6, 5, 5, f, 0, 4, 6, 1, BLACK)
    for u in (24, 32): t.box(u, 16, 2, 7, 2, SKIN)                # bare arms
    t.box(0, 40, 3, 3, 3, BLACK)                                  # puffed black sleeves
    if net:
        for x, y in ((1, 18), (3, 21), (9, 20), (5, 28), (11, 30), (14, 32)): t.rect(x, y, 1, 1, GLOW)
    return t

paint(False).save(D + 'boss_red_girls.png')
paint(True).save(D + 'boss_red_girls_network.png')
