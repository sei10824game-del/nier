import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_marx.png')
t = Tex(128, 128, seed=14)
OCHRE, RUST, DARK, STEEL, TREAD = (150, 118, 64), (120, 70, 40), (38, 36, 34), (112, 110, 106), (30, 28, 26)
t.box(0, 0, 18, 6, 22, OCHRE, noise=9)                    # chassis
for f, w in (('front', 18), ('back', 18), ('left', 22), ('right', 22)): t.face(0, 0, 18, 6, 22, f, 0, 4, w, 2, RUST)
t.box(0, 28, 4, 6, 24, TREAD, noise=4)                    # treads with track plates
for x in range(0, 24, 3):
    t.face(0, 28, 4, 6, 24, 'right', x, 0, 1, 6, (60, 58, 54)); t.face(0, 28, 4, 6, 24, 'left', x, 0, 1, 6, (60, 58, 54))
t.box(0, 58, 14, 8, 12, OCHRE, noise=9)                   # turret
t.face(0, 58, 14, 8, 12, 'front', 0, 0, 14, 1, (210, 180, 40)); t.face(0, 58, 14, 8, 12, 'front', 0, 2, 14, 1, DARK)
t.box(52, 58, 6, 6, 6, STEEL)                             # cab with red eye
t.face(52, 58, 6, 6, 6, 'front', 1, 1, 4, 2, (60, 80, 90)); t.face(52, 58, 6, 6, 6, 'front', 2, 3, 2, 2, (255, 40, 40))
t.box(0, 80, 4, 4, 20, OCHRE, noise=8)                    # boom
for z in range(0, 20, 4): t.face(0, 80, 4, 4, 20, 'right', z, 0, 2, 4, (200, 170, 40)); t.face(0, 80, 4, 4, 20, 'left', z, 0, 2, 4, (200, 170, 40))
t.box(56, 80, 3, 16, 16, STEEL)                           # saw wheel
for f in ('right', 'left'):
    t.face(56, 80, 3, 16, 16, f, 6, 6, 4, 4, DARK)
    for i in range(0, 16, 4): t.face(56, 80, 3, 16, 16, f, i, 0, 2, 16, (140, 138, 134))
t.box(96, 80, 4, 2, 2, (200, 200, 205)); t.box(96, 84, 4, 2, 2, (200, 200, 205))   # teeth
t.save(OUT)
