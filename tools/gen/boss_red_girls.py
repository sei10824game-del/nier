import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(net):
    t = Tex(64, 64, seed=18)
    BODY = (40, 6, 12) if net else (196, 24, 40)
    TRIM = (255, 40, 60) if net else (110, 10, 22)
    SHELL = (24, 4, 8) if net else (236, 226, 226)
    t.box(0, 0, 5, 5, 5, SHELL)                                   # faceless mask-head
    t.face(0, 0, 5, 5, 5, 'front', 1, 2, 3, 1, TRIM)             # single glyph slit
    t.box(20, 0, 6, 5, 3, BODY)                                   # hood plate
    t.box(20, 8, 7, 1, 7, TRIM, top=(255, 60, 80))                # glyph halo
    t.face(20, 8, 7, 1, 7, 'top', 2, 2, 3, 3, (0, 0, 0, 0))       # hollow centre
    t.box(0, 16, 4, 7, 3, BODY); t.face(0, 16, 4, 7, 3, 'front', 1, 1, 2, 5, TRIM)
    t.box(0, 26, 6, 5, 5, BODY)                                   # flared hem
    for f in ('front', 'back', 'left', 'right'): t.face(0, 26, 6, 5, 5, f, 0, 4, 6, 1, TRIM)
    for u in (24, 32): t.box(u, 16, 2, 7, 2, SHELL); t.rect(u, 18, 8, 1, TRIM)
    if net:                                                       # glowing network circuitry
        for x, y in ((1, 18), (3, 21), (9, 20), (5, 28), (11, 30), (14, 32)): t.rect(x, y, 1, 1, (255, 60, 70))
    return t

paint(False).save(D + 'boss_red_girls.png')
paint(True).save(D + 'boss_red_girls_network.png')
