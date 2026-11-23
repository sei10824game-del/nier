import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_simone.png')
t = Tex(128, 128, seed=17)
IVORY, ROSE, GOLD, DARK, STEEL = (226, 218, 208), (176, 60, 96), (214, 176, 84), (40, 34, 38), (150, 146, 150)
# tiered bell skirt: ivory metal with rose bands and gold trim at each hem
for (u, v, w, h), band in (((0, 0, 12, 4), 1), ((0, 16, 16, 5), 2), ((0, 37, 20, 5), 3)):
    t.box(u, v, w, h, w, IVORY, top=(200, 192, 184), noise=5)
    for f in ('front', 'back', 'left', 'right'):
        t.face(u, v, w, h, w, f, 0, h - 1, w, 1, GOLD)
        for x in range(band % 2, w, 3): t.face(u, v, w, h, w, f, x, 0, 1, h - 1, ROSE)
t.box(48, 0, 6, 10, 4, IVORY)                                     # torso: corset plates
t.face(48, 0, 6, 10, 4, 'front', 0, 6, 6, 1, GOLD); t.face(48, 0, 6, 10, 4, 'front', 2, 1, 2, 4, ROSE)
t.box(68, 0, 6, 6, 6, (236, 230, 224))                            # porcelain head with a single rose lens
t.face(68, 0, 6, 6, 6, 'front', 2, 2, 2, 2, (255, 70, 140)); t.face(68, 0, 6, 6, 6, 'front', 1, 5, 4, 1, STEEL)
t.box(92, 0, 8, 3, 2, GOLD)                                       # crown
for u in (80, 88): t.box(u, 16, 2, 16, 2, STEEL); t.rect(u, 18, 8, 2, GOLD); t.rect(u, 30, 8, 4, IVORY)   # long arms, ivory gloves
t.box(0, 62, 12, 16, 1, (240, 200, 220), noise=4)                 # fan plates
for x in range(0, 12, 2): t.face(0, 62, 12, 16, 1, 'front', x, 0, 1, 16, GOLD); t.face(0, 62, 12, 16, 1, 'back', x, 0, 1, 16, ROSE)
t.save(OUT)
