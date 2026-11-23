import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_adam.png')
t = Tex(64, 64, seed=11)
COAT, DARK, SKIN, RED, GOLD = (150, 148, 142), (28, 26, 30), (214, 206, 196), (235, 40, 55), (190, 160, 90)
CROWN = (220, 220, 225)
t.box(0, 0, 8, 8, 8, SKIN, top=CROWN)                       # head with a platinum plated crown
for f in ('right', 'left', 'back'): t.face(0, 0, 8, 8, 8, f, 0, 0, 8, 4, CROWN)
t.face(0, 0, 8, 8, 8, 'front', 0, 0, 8, 2, CROWN)
t.face(0, 0, 8, 8, 8, 'front', 1, 3, 6, 1, RED)              # glowing visor band
t.face(0, 0, 8, 8, 8, 'front', 3, 6, 2, 1, (150, 120, 120))
t.box(16, 16, 8, 12, 4, COAT)                                # torso: coat
t.face(16, 16, 8, 12, 4, 'front', 3, 0, 2, 12, DARK)         # inner shirt
t.face(16, 16, 8, 12, 4, 'front', 2, 0, 1, 12, GOLD); t.face(16, 16, 8, 12, 4, 'front', 5, 0, 1, 12, GOLD)
t.face(16, 16, 8, 12, 4, 'front', 0, 9, 8, 1, DARK)          # belt
for u, v in ((40, 16), (40, 32)):                            # arms: sleeves + black gloves
    t.box(u, v, 4, 12, 4, COAT); t.rect(u, v + 12, 16, 4, DARK, noise=3); t.rect(u, v + 10, 16, 1, GOLD)
for u, v in ((0, 16), (0, 32)):                              # legs: black trousers, boots
    t.box(u, v, 4, 12, 4, DARK); t.rect(u, v + 13, 16, 3, (12, 12, 14))
t.box(16, 34, 8, 10, 1, COAT); t.rect(17, 44, 8, 1, GOLD)    # coat tail
t.box(56, 0, 1, 16, 2, (200, 205, 215)); t.rect(58, 2, 1, 16, (240, 60, 70))   # blade with red edge
t.box(34, 48, 4, 1, 2, GOLD)                                 # guard
t.save(OUT)
