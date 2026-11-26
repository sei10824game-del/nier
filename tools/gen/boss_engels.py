import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(hot):
    t = Tex(128, 128, seed=13)
    DRUM, DRUMD, SALMON, SALD, LEG, DARK, STEEL = (112, 108, 92), (84, 80, 68), (184, 92, 80), (130, 62, 54), (98, 102, 90), (34, 32, 32), (70, 70, 74)
    HEAT = (255, 110, 30) if hot else SALMON
    t.box(0, 0, 16, 20, 12, DRUM, top=DRUMD, noise=8)                 # cylindrical drum torso
    for f, w in (('front', 16), ('back', 16), ('left', 12), ('right', 12)):
        for y in (4, 10, 15): t.face(0, 0, 16, 20, 12, f, 0, y, w, 1, DRUMD)      # ring seams
        t.face(0, 0, 16, 20, 12, f, 0, 7, w, 3, SALMON)                          # red mid band
        for x in range(1, w, 3): t.face(0, 0, 16, 20, 12, f, x, 1, 1, 1, DARK)   # rivets
    for v in (0, 9): t.box(88, v, 3, 6, 3, DARK); t.rect(91, v, 3, 3, (20, 18, 18))
    t.box(0, 106, 20, 2, 16, DRUMD, top=SALD, noise=6)               # decks
    t.box(56, 0, 8, 5, 8, STEEL)                                      # small skull-like head
    t.face(56, 0, 8, 5, 8, 'front', 1, 2, 2, 1, (255, 40, 40)); t.face(56, 0, 8, 5, 8, 'front', 5, 2, 2, 1, (255, 40, 40))
    t.face(56, 0, 8, 5, 8, 'front', 2, 4, 4, 1, DARK)
    for (u, v) in ((0, 32), (72, 84)):                                # long truss arms
        t.box(u, v, 6, 22, 6, SALMON, noise=8)
        for y in range(0, 22, 3): t.rect(u, v + 6 + y, 24, 1, SALD)
        t.rect(u, v + 6 + 18, 24, 4, HEAT)
    for v in (32, 56):                                                # buzzsaw discs
        t.box(24, v, 4, 12, 12, STEEL)
        t.face(24, v, 4, 12, 12, 'right', 5, 5, 2, 2, DARK); t.face(24, v, 4, 12, 12, 'left', 5, 5, 2, 2, DARK)
        for i in range(0, 12, 3): t.face(24, v, 4, 12, 12, 'right', i, 0, 1, 12, HEAT if hot else DARK)
    for ex in (65, 69):
        t.rect(ex, 9, 2, 1, (20, 18, 18)); t.rect(ex, 11, 2, 1, (40, 20, 20)); t.rect(ex, 10, 2, 1, (255, 50, 50)); t.rect(ex, 10, 1, 1, (255, 210, 180))
    t.box(56, 32, 5, 2, 3, (70, 66, 62)); t.box(56, 40, 5, 3, 2, (70, 66, 62))
    t.box(72, 50, 18, 2, 2, SALMON); t.rect(72, 54, 40, 2, SALD); t.box(72, 72, 18, 2, 2, SALMON); t.rect(72, 76, 40, 2, SALD)   # crane booms
    for u in (0, 32):                                                 # legs: gray-green + red truss edges
        t.box(u, 80, 7, 16, 8, LEG, noise=6)
        for x in range(0, 30, 6): t.rect(u + x, 88, 1, 16, SALMON)
    t.box(84, 112, 10, 3, 12, DRUMD, noise=6)                          # feet
    return t

paint(False).save(D + 'boss_engels.png')
paint(True).save(D + 'boss_engels_hot.png')
