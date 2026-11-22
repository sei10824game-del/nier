import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(hot):
    t = Tex(128, 128, seed=13)
    WALL, RUST, DARK, STEEL, WIN = (108, 100, 90), (128, 78, 46), (40, 36, 34), (96, 94, 92), (255, 196, 90)
    HEAT = (255, 110, 30) if hot else RUST
    t.box(0, 0, 16, 20, 12, WALL, top=(84, 80, 76), noise=8)          # factory torso
    for f, w in (('front', 16), ('back', 16), ('left', 12), ('right', 12)):
        for x in range(1, w - 2, 4):
            for y in (3, 8, 13): t.face(0, 0, 16, 20, 12, f, x, y, 2, 2, WIN if (x + y) % 3 else DARK)
        t.face(0, 0, 16, 20, 12, f, 0, 17, w, 3, RUST)                # rust skirt
    t.face(0, 0, 16, 20, 12, 'front', 5, 0, 6, 2, DARK)
    for v in (0, 9): t.box(88, v, 3, 6, 3, DARK); t.rect(91, v, 3, 3, (20, 18, 18))   # chimneys
    t.box(56, 0, 8, 5, 8, STEEL)                                      # head
    t.face(56, 0, 8, 5, 8, 'front', 0, 2, 8, 1, (255, 40, 40))        # red visor slit
    for v in (32, 56): t.box(0, v, 6, 14, 6, RUST, noise=8); t.rect(0, v + 6 + 10, 24, 4, HEAT)  # arms; forearms glow when hot
    for v in (32, 56):                                                # wheel discs
        t.box(24, v, 4, 12, 12, STEEL)
        t.face(24, v, 4, 12, 12, 'right', 5, 5, 2, 2, DARK); t.face(24, v, 4, 12, 12, 'left', 5, 5, 2, 2, DARK)
        for i in range(0, 12, 3): t.face(24, v, 4, 12, 12, 'right', i, 0, 1, 12, HEAT if hot else DARK)
    t.box(56, 32, 5, 2, 3, (70, 66, 62)); t.box(56, 40, 5, 3, 2, (70, 66, 62))   # buckets
    for u in (0, 32): t.box(u, 80, 7, 16, 8, DARK, noise=6); t.rect(u, 88, 30, 2, RUST); t.rect(u, 100, 30, 4, (24, 22, 20))
    return t

paint(False).save(D + 'boss_engels.png')
paint(True).save(D + 'boss_engels_hot.png')
