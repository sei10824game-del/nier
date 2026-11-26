import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_simone.png')
t = Tex(128, 256, seed=17)
IVORY, ROSE, GOLD, DARK, STEEL = (176, 166, 150), (150, 40, 44), (120, 108, 92), (60, 54, 50), (160, 156, 156)
RED = (200, 30, 36); SKINC = (150, 148, 152)
# 4 octagonal truss tiers (silver-bronze lattice panels)
for (u, v, w, h) in ((0, 0, 12, 5), (0, 16, 16, 5), (0, 37, 20, 5), (0, 130, 24, 5)):
    t.box(u, v, w, h, w, IVORY, top=(150, 140, 126), noise=6)
    for f in ('front', 'back', 'left', 'right'):
        t.face(u, v, w, h, w, f, 0, 0, w, 1, GOLD); t.face(u, v, w, h, w, f, 0, h - 1, w, 1, GOLD)
        for x in range(0, w, 4):
            t.face(u, v, w, h, w, f, x, 1, 1, 1, DARK); t.face(u, v, w, h, w, f, x + 1, 2, 1, 1, DARK); t.face(u, v, w, h, w, f, x + 2, 3, 1, 1, DARK); t.face(u, v, w, h, w, f, x + 3, 2, 1, 1, DARK)
t.box(0, 160, 20, 3, 20, RED, noise=10)                           # tattered red cloth
for x in range(0, 80, 5): t.rect(x, 183 - 1 - (x * 7) % 3, 2, 2, (0, 0, 0, 0))
t.box(100, 130, 2, 8, 1, SKINC, noise=4)                          # android corpses
t.box(0, 190, 14, 2, 1, (170, 120, 90)); t.box(40, 190, 7, 14, 1, RED, noise=8); t.box(60, 190, 4, 4, 4, IVORY, noise=5)
t.box(48, 0, 6, 10, 4, IVORY)                                     # torso: corset plates
t.face(48, 0, 6, 10, 4, 'front', 0, 6, 6, 1, GOLD); t.face(48, 0, 6, 10, 4, 'front', 2, 1, 2, 4, ROSE)
t.box(68, 0, 6, 6, 6, (170, 164, 152))                            # porcelain head with a single rose lens
t.face(68, 0, 6, 6, 6, 'front', 2, 2, 2, 2, (210, 160, 60)); t.face(68, 0, 6, 6, 6, 'front', 1, 5, 4, 1, STEEL)
t.box(92, 0, 8, 3, 2, GOLD)                                       # crown
for u in (80, 88): t.box(u, 16, 2, 16, 2, STEEL); t.rect(u, 18, 8, 2, GOLD); t.rect(u, 30, 8, 4, IVORY)   # long arms, ivory gloves
t.box(0, 62, 12, 16, 1, (240, 200, 220), noise=4)                 # fan plates
for x in range(0, 12, 2): t.face(0, 62, 12, 16, 1, 'front', x, 0, 1, 16, GOLD); t.face(0, 62, 12, 16, 1, 'back', x, 0, 1, 16, ROSE)
t.rect(75, 7, 4, 4, (40, 34, 30)); t.rect(76, 8, 2, 2, (214, 160, 60)); t.rect(76, 8, 1, 1, (255, 240, 200))   # lens socket + glint
t.strands(46, 191, 14, 14, RED)
t.save(OUT)
