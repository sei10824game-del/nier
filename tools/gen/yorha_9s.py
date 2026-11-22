"""Scanner android S (original design): slate hooded jacket, dark shorts/boots, charcoal-green fringe, cyan visor."""
from android_base import body, sword, great, back_great, spear, new, save, SKIN

SLATE, CYAN, HAIR = (102, 112, 128), (60, 210, 226), (44, 56, 50)

t = new()
body(t, dict(hair=HAIR, suit=SLATE, trim=CYAN, legs=(36, 40, 48), boots=(28, 30, 36), eye=(60, 150, 170),
             pod=(142, 148, 156), podDark=(80, 86, 94), podEye=CYAN, glove=(40, 44, 52)))
for u in (88, 104): t.rect(u, 4, 16, 2, SKIN)              # bare knees between shorts and boots
t.rect(37, 5, 1, 6, (70, 78, 92)); t.rect(42, 5, 1, 6, (70, 78, 92))   # jacket zip folds
t.box(0, 32, 9, 4, 2, SLATE, noise=4)                        # hood (0,32) 9x4x2
t.box(24, 32, 9, 3, 9, HAIR, noise=7)                        # hair cap
t.box(60, 32, 2, 2, 3, HAIR, noise=7)                        # spiky tuft
t.box(72, 32, 9, 2, 1, (20, 30, 36)); t.rect(73, 33, 9, 1, CYAN)        # visor band
t.box(92, 32, 8, 6, 1, SLATE, noise=4); t.rect(93, 38, 8, 1, CYAN)      # jacket tail
sword(t, 0, 48, 12, (214, 232, 238), CYAN)
great(t, 18, (90, 96, 108), CYAN)
back_great(t, 20, (90, 96, 108), CYAN)
spear(t, (70, 74, 84), (220, 236, 240))
save(t, 'yorha_9s')
