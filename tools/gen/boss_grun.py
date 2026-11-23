import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(exposed):
    t = Tex(128, 128, seed=16)
    HULL, DARK, MOSS, BARN, CYAN = (50, 48, 52), (24, 22, 26), (66, 62, 64), (90, 86, 88), (64, 224, 255)
    CORE = (220, 255, 70) if exposed else CYAN
    t.box(0, 0, 20, 14, 16, HULL, noise=10)                                 # head
    for f, w in (('front', 20), ('back', 20), ('left', 16), ('right', 16)):
        t.face(0, 0, 20, 14, 16, f, 0, 11, w, 3, MOSS)                      # waterline algae
        for x in range(1, w - 1, 5): t.face(0, 0, 20, 14, 16, f, x, 12 - (x % 3), 1, 1, BARN)
        t.face(0, 0, 20, 14, 16, f, 0, 4, w, 1, DARK)
    for ex, ey, r in ((2, 5, 4), (8, 4, 4), (14, 5, 4)):                       # big ringed red eyes
        t.face(0, 0, 20, 14, 16, 'front', ex, ey, r, r, (230, 40, 50)); t.face(0, 0, 20, 14, 16, 'front', ex + 1, ey + 1, 2, 2, (70, 10, 14)); t.face(0, 0, 20, 14, 16, 'front', ex + 1, ey + 1, 1, 1, (255, 200, 200))
    for ex, ey in ((6, 9), (12, 9), (10, 2)): t.face(0, 0, 20, 14, 16, 'front', ex, ey, 2, 2, (200, 40, 50))
    if exposed: t.face(0, 0, 20, 14, 16, 'front', 6, 0, 8, 6, (40, 20, 18))  # torn brow plating
    t.box(72, 0, 6, 6, 1, CORE)                                             # EMP core
    t.box(72, 8, 3, 5, 3, (120, 130, 136)); t.rect(75, 8, 3, 3, CYAN)       # laser masts
    t.box(0, 30, 16, 3, 12, DARK, noise=6)                                  # jaw
    for x in range(1, 16, 3): t.face(0, 30, 16, 3, 12, 'top', x, 0, 1, 2, (220, 220, 210))   # teeth
    t.box(56, 30, 12, 1, 9, (255, 60, 30))                                  # maw glow
    for v in (48, 76):
        t.box(0, v, 4, 24, 4, HULL, noise=10); t.rect(0, v + 4, 16, 1, DARK); [t.rect(0, v + 4 + k * 3, 16, 1, DARK) for k in range(8)]
        t.box(28, v, 10, 6, 10, DARK, noise=6)
        for x in range(0, 10, 3): t.face(28, v, 10, 6, 10, 'front', x, 3, 2, 3, (90, 96, 100))   # fingers
    t.box(68, 48, 16, 20, 12, HULL, noise=8)                                # ringed torso
for y in range(2, 20, 3):
    for f, w in (('front', 16), ('back', 16), ('left', 12), ('right', 12)): t.face(68, 48, 16, 20, 12, f, 0, y, w, 1, DARK)
for y in (4, 9, 14): t.face(68, 48, 16, 20, 12, 'front', 7, y, 2, 2, (255, 210, 40))   # yellow lamps
    return t

paint(False).save(D + 'boss_grun.png')
paint(True).save(D + 'boss_grun_core.png')
