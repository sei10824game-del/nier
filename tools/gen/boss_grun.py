import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(exposed):
    t = Tex(128, 128, seed=16)
    HULL, DARK, MOSS, BARN, CYAN = (66, 84, 88), (28, 34, 38), (70, 98, 72), (150, 150, 136), (64, 224, 255)
    CORE = (220, 255, 70) if exposed else CYAN
    t.box(0, 0, 20, 14, 16, HULL, noise=10)                                 # head
    for f, w in (('front', 20), ('back', 20), ('left', 16), ('right', 16)):
        t.face(0, 0, 20, 14, 16, f, 0, 11, w, 3, MOSS)                      # waterline algae
        for x in range(1, w - 1, 5): t.face(0, 0, 20, 14, 16, f, x, 12 - (x % 3), 1, 1, BARN)
        t.face(0, 0, 20, 14, 16, f, 0, 4, w, 1, DARK)
    t.face(0, 0, 20, 14, 16, 'front', 3, 6, 4, 2, (255, 40, 40)); t.face(0, 0, 20, 14, 16, 'front', 13, 6, 4, 2, (255, 40, 40))  # eyes
    if exposed: t.face(0, 0, 20, 14, 16, 'front', 6, 0, 8, 6, (40, 20, 18))  # torn brow plating
    t.box(72, 0, 6, 6, 1, CORE)                                             # EMP core
    t.box(72, 8, 3, 5, 3, (120, 130, 136)); t.rect(75, 8, 3, 3, CYAN)       # laser masts
    t.box(0, 30, 16, 3, 12, DARK, noise=6)                                  # jaw
    for x in range(1, 16, 3): t.face(0, 30, 16, 3, 12, 'top', x, 0, 1, 2, (220, 220, 210))   # teeth
    t.box(56, 30, 12, 1, 9, (255, 60, 30))                                  # maw glow
    for v in (48, 76):
        t.box(0, v, 6, 12, 8, HULL, noise=10); t.rect(0, v + 16, 28, 2, MOSS)
        t.box(28, v, 10, 6, 10, DARK, noise=6)
        for x in range(0, 10, 3): t.face(28, v, 10, 6, 10, 'front', x, 3, 2, 3, (90, 96, 100))   # fingers
    t.box(68, 48, 16, 10, 12, MOSS, noise=12)                               # submerged neck
    return t

paint(False).save(D + 'boss_grun.png')
paint(True).save(D + 'boss_grun_core.png')
