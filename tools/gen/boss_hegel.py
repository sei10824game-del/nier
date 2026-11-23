import sys, os; sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex
OUT = os.path.join(os.path.dirname(__file__), '../../src/main/resources/assets/nierautomata/textures/entity/boss_hegel.png')
t = Tex(128, 128, seed=15)
CREAM, SEAM, DARK, RED = (204, 190, 156), (150, 134, 104), (30, 28, 28), (255, 40, 40)
def sphere(u, v, w, h, d):
    t.box(u, v, w, h, d, CREAM, noise=5)
    t.rect(u, v + d + h // 2, 2 * (d + w), 1, SEAM)                 # equator seam
    t.rect(u + d + w // 2, v + d, 1, h, SEAM)
# core: three crossed boxes
sphere(0, 0, 12, 8, 8); sphere(40, 0, 8, 12, 8); sphere(72, 0, 8, 8, 12)
t.box(0, 24, 4, 4, 1, (240, 240, 240)); t.rect(1, 25, 2, 2, RED)       # ringed eye
for (u, v, w, h, d) in ((0, 40, 8, 6, 6), (28, 40, 6, 8, 6), (52, 40, 6, 6, 8)): sphere(u, v, w, h, d)
t.box(16, 24, 2, 2, 1, (240, 240, 240)); t.rect(17, 25, 1, 1, RED)
t.box(0, 90, 1, 7, 1, DARK); t.box(8, 90, 2, 3, 2, DARK); t.box(16, 90, 3, 5, 3, DARK, noise=6)   # claw legs, pistons
t.box(0, 64, 16, 2, 16, DARK, noise=6)
t.face(0, 64, 16, 2, 16, 'bottom', 5, 5, 6, 6, (255, 150, 80))
t.save(OUT)
