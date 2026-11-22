import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_hegel.png')
t = Tex(64, 64, seed=15)
GRAY, DARK, RUST, RED = (110, 106, 100), (44, 42, 40), (122, 84, 56), (255, 40, 40)
t.box(0, 0, 12, 12, 12, GRAY, noise=8)                                   # core
for f in ('front', 'back', 'left', 'right'):
    t.face(0, 0, 12, 12, 12, f, 0, 5, 12, 2, DARK); t.face(0, 0, 12, 12, 12, f, 0, 10, 12, 2, RUST)
t.face(0, 0, 12, 12, 12, 'front', 3, 3, 6, 6, DARK)
t.box(48, 0, 4, 4, 1, RED)                                               # lens
t.box(0, 24, 8, 8, 8, (128, 122, 112), noise=8)                          # spherical unit (rounded by darker corners)
for f in ('front', 'back', 'left', 'right', 'top', 'bottom'):
    for x, y in ((0, 0), (7, 0), (0, 7), (7, 7)): t.face(0, 24, 8, 8, 8, f, x, y, 1, 1, DARK)
    t.face(0, 24, 8, 8, 8, f, 0, 3, 8, 1, RUST)
t.box(32, 24, 2, 2, 1, RED)                                              # unit eye
t.box(0, 40, 16, 2, 16, DARK, noise=6)                                   # underside thruster plate
t.face(0, 40, 16, 2, 16, 'bottom', 5, 5, 6, 6, (255, 150, 80))
t.save(OUT)
