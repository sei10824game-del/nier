"""YoRHa No.9 Type S fan model: short white hair, black blindfold, black jacket/shorts/boots, Pod 153."""
from android_base import body, sword, great, back_great, spear, new, save, SKIN

SLATE, CYAN, HAIR = (24, 24, 30), (235, 235, 242), (232, 232, 240)

t = new()
body(t, dict(hair=HAIR, suit=SLATE, trim=CYAN, legs=(20, 20, 26), boots=(14, 14, 18), eye=(60, 150, 170),
             pod=(70, 72, 80), podDark=(40, 42, 50), podEye=(255, 150, 60), glove=(40, 44, 52)))
for u in (88, 104): t.rect(u, 4, 16, 2, SKIN)              # bare knees between shorts and boots
t.rect(37, 5, 1, 6, (50, 50, 60)); t.rect(42, 5, 1, 6, (50, 50, 60))
BL=(10, 10, 12)
t.rect(0, 11, 32, 3, BL)   # jacket zip folds
t.box(0, 32, 9, 4, 2, SLATE, noise=4)                        # hood (0,32) 9x4x2
t.box(24, 32, 9, 3, 9, HAIR, noise=7)                        # hair cap
t.box(60, 32, 2, 2, 3, HAIR, noise=7)                        # spiky tuft
t.box(72, 32, 9, 2, 1, BL)        # visor band
t.box(92, 32, 8, 6, 1, SLATE, noise=4); t.rect(93, 38, 8, 1, CYAN)      # jacket tail
sword(t, 0, 48, 12, (230, 232, 240), (255, 255, 255))
great(t, 18, (225, 228, 238), (30, 30, 36))
back_great(t, 20, (225, 228, 238), (30, 30, 36))
spear(t, (70, 74, 84), (220, 236, 240))
save(t, 'yorha_9s')
