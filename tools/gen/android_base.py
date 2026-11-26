"""Shared painter for the android units (yorha_2b / yorha_9s / a2). UV layout matches AndroidModel.base()."""
import os, sys
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex  # noqa: E402

OUT = os.path.join(os.path.dirname(__file__), '..', '..', 'src/main/resources/assets/nierautomata/textures/entity')
SKIN = (234, 216, 198)


def body(t, p):
    """p: dict hair, suit, trim, legs, boots, eye, pod, podDark. Paints head/torso/arms/legs/pod."""
    # head: hair everywhere, face on front
    t.box(0, 0, 8, 8, 8, p['hair'], noise=6)
    t.rect(8, 8, 8, 8, SKIN, noise=2)                      # face
    t.rect(8, 8, 8, 2, p['hair'], noise=6)                 # fringe
    t.rect(8, 10, 1, 3, p['hair']); t.rect(15, 10, 1, 3, p['hair'])
    t.rect(10, 11, 1, 1, p['eye']); t.rect(13, 11, 1, 1, p['eye'])
    t.face_detail(SKIN, p['eye'], eyes=((9, 11), (13, 11)))
    t.strands(24, 8, 8, 8, p['hair']); t.strands(0, 8, 8, 4, p['hair']); t.strands(16, 8, 8, 4, p['hair']); t.strands(8, 0, 8, 8, p['hair'])
    t.rect(11, 14, 2, 1, (200, 150, 140))                  # mouth
    # torso
    t.box(32, 0, 8, 12, 4, p['suit'], noise=4)
    t.rect(36, 4, 8, 1, p['trim']); t.rect(36, 10, 8, 1, p['trim'])          # collar / belt
    t.rect(39, 5, 2, 5, p['trim'])                                            # centre seam
    t.rect(36, 4, 8, 2, SKIN) if p.get('openCollar') else None
    # arms (sleeves + gloves)
    for u in (56, 72):
        t.box(u, 0, 3, 12, 4, p['suit'], noise=4)
        t.rect(u, 4 + 9, 14, 3, p.get('glove', p['boots']))
        t.rect(u, 4 + 2, 14, 1, p['trim'])
    # legs (thigh + boots)
    for u in (88, 104):
        t.box(u, 0, 4, 12, 4, p['legs'], noise=4)
        t.rect(u, 4 + 5, 16, 7, p['boots'], noise=3)
        t.rect(u, 4 + 5, 16, 1, p['trim'])
    # pod: body, fins, barrel
    t.box(0, 16, 6, 6, 8, p['pod'], noise=4)
    t.rect(8, 24, 6, 1, p['podDark']); t.rect(8, 28, 6, 1, p['podDark'])
    t.rect(10, 26, 2, 2, (40, 40, 40)); t.rect(10, 26, 1, 1, p['podEye'])  # front lens
    t.box(44, 16, 8, 1, 4, p['podDark'], noise=3)
    t.box(28, 16, 2, 2, 6, (60, 60, 64), noise=3)


def sword(t, u, v, length, blade, edge, hilt=(40, 40, 46), guard=(150, 150, 160)):
    t.box(u, v, 1, 1, 3, hilt); t.box(u + 8, v, 1, 3, 1, guard)
    t.box(u, v + 4, 1, 2, length, blade, noise=3)
    t.rect(u, v + 4 + length, 2 * length + 2, 1, edge)    # one edge row on sides


def great(t, len_, blade, line, hilt=(35, 35, 40), guard=(120, 110, 90)):
    """Held greatsword (grip 32,48 / guard 76,48 / blade 32,54)."""
    t.box(32, 48, 1, 1, 4, hilt); t.box(76, 48, 1, 5, 2, guard)
    t.box(32, 54, 1, 4, len_, blade, noise=3)
    t.rect(32, 54 + len_ + 1, 2 * len_ + 2, 1, line)


def back_great(t, h, blade, line):
    """Sheathed greatsword on the back (84,48) box 4 x h x 1."""
    t.box(84, 48, 4, h, 1, blade, noise=3)
    t.rect(86, 49, 1, h, line); t.rect(91, 49, 1, h, line)
    t.rect(85, 49, 4, 3, (35, 35, 40)); t.rect(90, 49, 4, 3, (35, 35, 40))  # hilt end


def spear(t, shaft, head):
    t.box(0, 80, 1, 1, 28, shaft, noise=4)
    t.box(60, 80, 1, 3, 5, head, noise=2)


def new():
    return Tex(128, 128)


def save(t, name):
    os.makedirs(OUT, exist_ok=True); t.save(os.path.join(OUT, name + '.png'))
