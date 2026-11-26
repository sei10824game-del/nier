import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
D = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/')

def paint(rage):
    t = Tex(128, 128, seed=12)
    PLATE, DARK, STEEL, SKIN = (58, 54, 58), (24, 22, 24), (120, 118, 122), (196, 182, 170)
    GLOW = (255, 50, 30) if rage else (230, 40, 40)
    TAT = (20, 18, 22)
    t.box(0, 0, 8, 8, 8, SKIN, top=(196, 196, 208))                    # head: dark cropped crest
    for f in ('right', 'left', 'back'): t.face(0, 0, 8, 8, 8, f, 0, 0, 8, 3, (196, 196, 208))
    t.face(0, 0, 8, 8, 8, 'front', 0, 0, 8, 1, (196, 196, 208))
    t.face(0, 0, 8, 8, 8, 'front', 1, 3, 2, 1, GLOW); t.face(0, 0, 8, 8, 8, 'front', 5, 3, 2, 1, GLOW)
    t.face(0, 0, 8, 8, 8, 'front', 2, 6, 4, 1, (120, 90, 90))
    t.box(16, 16, 10, 12, 6, SKIN)                                  # torso: shirtless
    pass
    for i in range(0, 10, 2): t.face(16, 16, 10, 12, 6, 'front', i, 1 + i % 4, 1, 6, TAT)   # tattoos
    t.face(16, 16, 10, 12, 6, 'front', 4, 1, 2, 5, (170, 156, 146))
    t.face(16, 16, 10, 12, 6, 'front', 0, 8, 10, 2, DARK)           # belt
    t.face(16, 16, 10, 12, 6, 'front', 4, 8, 2, 2, GLOW)
    for x in (2, 7): t.face(16, 16, 10, 12, 6, 'back', x, 2, 1, 7, GLOW)   # spinal vents
    for u in (0, 20):                                               # bare forearms with plated upper arm
        t.box(u, 34, 5, 13, 5, SKIN); t.rect(u, 38, 20, 1, TAT); t.rect(u, 41, 20, 1, TAT)
        if rage: t.rect(u, 46, 20, 1, GLOW)
    for v in (34, 45):                                              # gauntlets
        t.box(40, v, 6, 5, 6, STEEL); t.rect(46, v + 6, 6, 1, GLOW); t.rect(46, v + 9, 6, 1, DARK)
    for v in (56, 65): t.box(40, v, 6, 3, 6, PLATE, top=(80, 76, 80)); t.rect(46, v + 6, 6, 1, GLOW)
    for u in (0, 20):                                               # trousers + boots
        t.box(u, 52, 5, 12, 5, DARK); t.rect(u, 62, 20, 4, (12, 10, 12)); t.rect(u, 58, 20, 1, PLATE)
    t.box(0, 80, 24, 1, 1, STEEL); t.box(64, 0, 1, 1, 24, STEEL)    # steel pipes
    t.face_detail(SKIN, GLOW, eyes=((9, 11), (13, 11)))
    for r in ((24, 8, 8, 8), (0, 8, 8, 4), (16, 8, 8, 4), (8, 0, 8, 8)): t.strands(*r, (196, 196, 208))
    return t

paint(False).save(D + 'boss_eve.png')
paint(True).save(D + 'boss_eve_rage.png')
