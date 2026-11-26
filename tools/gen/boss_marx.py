import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_marx.png')
t = Tex(128, 128, seed=14)
OCHRE, RUST, DARK, STEEL, TREAD = (228, 112, 46), (170, 76, 34), (38, 36, 34), (92, 90, 94), (122, 126, 130)
t.box(0, 0, 18, 6, 22, OCHRE, noise=9)                    # chassis
for f, w in (('front', 18), ('back', 18), ('left', 22), ('right', 22)): t.face(0, 0, 18, 6, 22, f, 0, 4, w, 2, RUST)
t.box(0, 28, 24, 2, 24, TREAD, noise=6); t.box(0, 104, 20, 2, 20, (104, 108, 112), noise=6)   # stepped base
for f, w in (('front', 24), ('back', 24), ('left', 24), ('right', 24)):
    for x in range(0, w, 4): t.face(0, 28, 24, 2, 24, f, x, 0, 1, 2, (80, 84, 88))
t.box(64, 104, 4, 16, 4, OCHRE, noise=6)                  # truss towers
for y in range(0, 16, 3): t.rect(64, 108 + y, 16, 1, DARK)
t.box(76, 40, 4, 4, 22, OCHRE, noise=6)                   # side conveyor boom
for z in range(0, 22, 3): t.rect(76 + 4 + z, 44, 1, 4, DARK); t.rect(76 + 4 + z, 62, 1, 4, DARK)
t.box(0, 58, 14, 8, 12, OCHRE, noise=9)                   # turret
t.face(0, 58, 14, 8, 12, 'front', 0, 0, 14, 1, DARK); t.face(0, 58, 14, 8, 12, 'front', 0, 2, 14, 1, DARK)
t.box(52, 58, 6, 6, 6, STEEL)                             # cab with red eye
t.face(52, 58, 6, 6, 6, 'front', 1, 1, 4, 2, (60, 80, 90)); t.face(52, 58, 6, 6, 6, 'front', 2, 3, 2, 2, (255, 40, 40))
t.box(0, 80, 4, 4, 20, OCHRE, noise=8)                    # boom
for z in range(0, 20, 4): t.face(0, 80, 4, 4, 20, 'right', z, 0, 2, 4, DARK); t.face(0, 80, 4, 4, 20, 'left', z, 0, 2, 4, DARK)
t.box(56, 80, 3, 16, 16, STEEL)                           # saw wheel
for f in ('right', 'left'):
    t.face(56, 80, 3, 16, 16, f, 6, 6, 4, 4, DARK)
    for i in range(0, 16, 4): t.face(56, 80, 3, 16, 16, f, i, 0, 2, 16, (140, 138, 134))
t.box(96, 80, 4, 2, 2, (200, 200, 205)); t.box(96, 84, 4, 2, 2, (200, 200, 205))   # teeth
t.rect(59, 66, 4, 1, (20, 18, 18)); t.rect(59, 69, 4, 1, (20, 18, 18)); t.rect(60, 67, 2, 2, (255, 50, 50)); t.rect(60, 67, 1, 1, (255, 210, 180))
t.save(OUT)
