"""Machine-lifeform geometry + textures + lang parts (one source of truth).
Run from repo root: python3 tools/gen/machine.py
Emits: src/main/java/com/nier/automata/unit/machine/client/MachineGeo.java (LayerDefinitions + bone paths),
       textures/entity/machine_<arch>[_gold|_hacked|_burn].png, lang/parts/<id>.json + .ja.json
"""
import json, os, sys
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex

ROOT = os.path.join(os.path.dirname(__file__), '..', '..')
RES = os.path.join(ROOT, 'src/main/resources/assets/nierautomata')

# part = (name, parent, pivot, rot, [box = (x,y,z,w,h,d, mat, [deco=(face,x,y,w,h,mat)])])
EYE1 = lambda w, y=2: [('front', 1, y, 3, 3, 'white'), ('front', 2, y + 1, 1, 1, 'eye'), ('front', w - 4, y, 3, 3, 'white'), ('front', w - 3, y + 1, 1, 1, 'eye')]
def P(name, parent, pivot, boxes, rot=(0, 0, 0)): return (name, parent, pivot, rot, boxes)
def B(x, y, z, w, h, d, mat, *deco): return (x, y, z, w, h, d, mat, list(deco))
EYES2 = lambda w, y=3, s=2, inset=1: [('front', inset, y, s, s, 'eye'), ('front', w - inset - s, y, s, s, 'eye')]

ARCH = {}
ARCH['stubby'] = (128, 192, [
    P('body', 'root', (0, 18, 0), [B(-4, -7, -3.5, 8, 7, 7, 'plate', ('front', 3, 2, 2, 2, 'steel'), ('front', 3, 3, 2, 1, 'dark'), ('front', 4, 2, 1, 2, 'dark')), B(-4.5, -3, -4, 9, 2, 8, 'dark')]),
    P('head', 'body', (0, -7, 0), [B(-4.5, -8, -4.5, 9, 8, 9, 'plate', *EYE1(9, 2), ('front', 1, 6, 1, 1, 'steel')),
                                   B(-3, -9, -3, 6, 1, 6, 'plate'), B(-5, -2, -5, 10, 1, 10, 'dark')]),
    P('bomb', 'head', (0, -8, 0), [B(-4, -6, -4, 8, 6, 8, 'dark', ('front', 0, 2, 8, 2, 'eye'), ('left', 0, 2, 8, 2, 'eye'), ('right', 0, 2, 8, 2, 'eye'), ('back', 0, 2, 8, 2, 'eye')), B(-0.5, -9, -0.5, 1, 3, 1, 'steel')]),
    P('drum', 'body', (0, -4, 0), [B(-6, -10, -6, 12, 16, 12, 'steel', ('front', 2, 2, 1, 1, 'dark'), ('front', 5, 2, 1, 1, 'dark'), ('front', 8, 2, 1, 1, 'dark'), ('front', 2, 5, 1, 1, 'dark'), ('front', 5, 5, 1, 1, 'dark'), ('front', 8, 5, 1, 1, 'dark'), ('front', 2, 8, 1, 1, 'dark'), ('front', 5, 8, 1, 1, 'dark'), ('front', 8, 8, 1, 1, 'dark'), ('front', 2, 11, 1, 1, 'dark'), ('front', 5, 11, 1, 1, 'dark'), ('front', 8, 11, 1, 1, 'dark'), ('front', 2, 14, 1, 1, 'dark'), ('front', 5, 14, 1, 1, 'dark'), ('front', 8, 14, 1, 1, 'dark'))]),
    P('horns', 'head', (0, -8, 0), [B(-5.5, -4, -1, 1, 4, 1, 'gold'), B(4.5, -4, -1, 1, 4, 1, 'gold'), B(-6.5, -6, -1, 2, 2, 1, 'gold'), B(4.5, -6, -1, 2, 2, 1, 'gold'), B(-5, -1, -5, 10, 1, 10, 'dark'), B(-1.5, -2, -5.5, 3, 2, 1, 'gold')]),
    P('tiers', 'body', (0, 0, 0), [B(-4.5, -9, -4, 9, 3, 8, 'hazard'), B(-4.5, -6, -4, 9, 3, 8, 'black'), B(-4.5, -3, -4, 9, 3, 8, 'hazard'), B(-4.5, 0, -4, 9, 3, 8, 'black')]),
    P('hat', 'head', (0, -8, 0), [B(-6, -1, -6, 12, 1, 12, 'black'), B(-3.5, -5, -3.5, 7, 4, 7, 'black', ('front', 0, 3, 7, 1, 'steel'))]),
    P('ears', 'head', (0, -8, 0), [B(-3.5, -11, -1, 2, 11, 1, 'plate'), B(1.5, -11, -1, 2, 11, 1, 'plate'), B(-5, -2, -5, 10, 2, 10, 'steel')]),
    P('jester', 'head', (0, -8, 0), [B(-4.5, -3, -4.5, 9, 3, 9, 'purple'), B(-4, -6, -1.5, 3, 3, 3, 'purple'), B(1, -6, -1.5, 3, 3, 3, 'purple'), B(-5, -2, -5, 10, 2, 10, 'steel')]),
    P('antlers', 'head', (0, -8, 0), [B(-6, -9, 0, 2, 9, 1, 'steel'), B(4, -9, 0, 2, 9, 1, 'steel'), B(-9, -9, 0, 3, 1, 1, 'steel'), B(6, -9, 0, 3, 1, 1, 'steel')]),
    P('rightArm', 'body', (-4.5, -6, 0), [B(-2.5, -1, -1.5, 3, 8, 3, 'dark'), B(-3, 7, -2, 4, 3, 4, 'plate')]),
    P('leftArm', 'body', (4.5, -6, 0), [B(-0.5, -1, -1.5, 3, 8, 3, 'dark'), B(-1, 7, -2, 4, 3, 4, 'plate')]),
    P('rightLeg', 'root', (-2, 18, 0), [B(-1.5, 0, -1.5, 3, 4, 3, 'dark'), B(-2.5, 4, -3, 5, 2, 6, 'plate', ('top', 1, 1, 3, 1, 'dark'))]),
    P('leftLeg', 'root', (2, 18, 0), [B(-1.5, 0, -1.5, 3, 4, 3, 'dark'), B(-2.5, 4, -3, 5, 2, 6, 'plate', ('top', 1, 1, 3, 1, 'dark'))]),
    P('weapon', 'rightArm', (-1, 8.5, 0), []),
    P('axe', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -12, 1, 1, 14, 'dark'), B(-0.5, -5.5, -13, 1, 5, 5, 'steel'), B(-0.5, 0.5, -12.5, 1, 2, 3, 'steel')]),
    P('katana', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -8, 1, 1, 10, 'dark'), B(-1.5, -1.5, -9, 3, 3, 1, 'gold'), B(-0.5, -1, -31, 1, 2, 22, 'glow')]),
    P('spear', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -18, 1, 1, 22, 'dark'), B(-1, -1, -22, 2, 2, 4, 'steel')]),
    P('hammer', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -11, 1, 1, 13, 'dark'), B(-2.5, -2.5, -15, 5, 5, 5, 'steel')]),
    P('staff', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -14, 1, 1, 17, 'dark'), B(-1.5, -1.5, -17, 3, 3, 3, 'glow')]),
    P('shield', 'leftArm', (1, 5, -2), [B(-4, -5, -1.5, 9, 10, 1, 'plate', ('front', 3, 3, 3, 4, 'dark'))]),
])
ARCH['biped'] = (128, 128, [
    P('body', 'root', (0, 12, 0), [B(-3, -1, -2, 6, 3, 4, 'dark'), B(-4, -11, -3, 8, 10, 6, 'plate', ('front', 1, 2, 6, 2, 'dark'), ('front', 3, 5, 2, 3, 'dark'))]),
    P('head', 'body', (0, -11, 0), [B(-4, -7, -4, 8, 7, 8, 'plate', *EYE1(8, 2)), B(-3, -8, -3, 6, 1, 6, 'plate')]),
    P('hat', 'head', (0, -7, 0), [B(-6, -1, -6, 12, 1, 12, 'black'), B(-3.5, -5, -3.5, 7, 4, 7, 'black', ('front', 0, 3, 7, 1, 'steel'))]),
    P('ears', 'head', (0, -7, 0), [B(-3.5, -11, -1, 2, 11, 1, 'plate'), B(1.5, -11, -1, 2, 11, 1, 'plate'), B(-5, -2, -5, 10, 2, 10, 'steel')]),
    P('jester', 'head', (0, -7, 0), [B(-4.5, -3, -4.5, 9, 3, 9, 'purple'), B(-4, -6, -1.5, 3, 3, 3, 'purple'), B(1, -6, -1.5, 3, 3, 3, 'purple'), B(-5, -2, -5, 10, 2, 10, 'steel')]),
    P('antlers', 'head', (0, -7, 0), [B(-6, -9, 0, 2, 9, 1, 'steel'), B(4, -9, 0, 2, 9, 1, 'steel'), B(-9, -9, 0, 3, 1, 1, 'steel'), B(6, -9, 0, 3, 1, 1, 'steel')]),
    P('helm', 'head', (0, -7, 0), [B(-0.5, -11, -0.5, 1, 5, 1, 'steel'), B(-4.5, -3, -4.5, 9, 1, 9, 'steel'), B(-4, -8, -4, 1, 3, 1, 'steel'), B(3, -8, -4, 1, 3, 1, 'steel')]),
    P('rightArm', 'body', (-5, -10, 0), [B(-1.5, -1, -1, 2, 12, 2, 'dark'), B(-2.5, -2, -2, 3, 3, 4, 'plate'), B(-2, 11, -1.5, 3, 3, 3, 'plate')]),
    P('leftArm', 'body', (5, -10, 0), [B(-0.5, -1, -1, 2, 12, 2, 'dark'), B(-0.5, -2, -2, 3, 3, 4, 'plate'), B(-1, 11, -1.5, 3, 3, 3, 'plate')]),
    P('rightLeg', 'root', (-2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-1.5, 4, -1.5, 3, 3, 3, 'plate'), B(-1.5, 10, -2.5, 3, 2, 4, 'plate')]),
    P('leftLeg', 'root', (2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-1.5, 4, -1.5, 3, 3, 3, 'plate'), B(-1.5, 10, -2.5, 3, 2, 4, 'plate')]),
    P('club', 'rightArm', (-0.5, 12.5, 0), [B(-0.5, -0.5, -5, 1, 1, 6, 'dark'), B(-1.5, -1.5, -15, 3, 3, 10, 'plate', ('top', 0, 2, 3, 1, 'dark'))]),
    P('shield', 'leftArm', (0.5, 8, -1.5), [B(-5, -6, -1, 11, 13, 1, 'plate', ('front', 4, 4, 3, 5, 'dark'), ('front', 5, 6, 1, 1, 'eye'))]),
])
ARCH['flyer'] = (128, 160, [
    P('body', 'root', (0, 12, 0), [B(-4, -4, -4, 8, 8, 8, 'plate', *EYE1(8, 2)), B(-1, 4, -4, 2, 2, 4, 'dark')]),
    P('hat', 'body', (0, -4, 0), [B(-6, -1, -6, 12, 1, 12, 'black'), B(-3.5, -5, -3.5, 7, 4, 7, 'black', ('front', 0, 3, 7, 1, 'steel'))]),
    P('gcore', 'body', (0, 0, 0), [B(-6, -6, -6, 12, 12, 12, 'dark', ('front', 1, 1, 10, 1, 'steel'), ('front', 4, 4, 4, 4, 'plate')), B(-0.5, -14, -0.5, 1, 8, 1, 'steel')]),
    P('gpodR', 'body', (-12, 0, 0), [B(-4, -9, -5, 8, 18, 10, 'plate', ('front', 1, 1, 6, 16, 'dark')), B(-2, 9, -3, 4, 3, 6, 'glow')]),
    P('gpodL', 'body', (12, 0, 0), [B(-4, -9, -5, 8, 18, 10, 'plate', ('front', 1, 1, 6, 16, 'dark')), B(-2, 9, -3, 4, 3, 6, 'glow')]),
    P('jwingR', 'body', (-4, 0, 1), [B(-16, -0.5, -3, 16, 1, 9, 'plate'), B(-16, -1.5, -2, 3, 3, 7, 'steel'), B(-9, -1, -4, 1, 2, 3, 'dark')], rot=(0, 0.45, 0)),
    P('jwingL', 'body', (4, 0, 1), [B(0, -0.5, -3, 16, 1, 9, 'plate'), B(13, -1.5, -2, 3, 3, 7, 'steel'), B(8, -1, -4, 1, 2, 3, 'dark')], rot=(0, -0.45, 0)),
    P('gsword', 'body', (0, 3, 0), [B(-0.5, 2, -22, 1, 2, 28, 'steel'), B(-1.5, 1.5, 4, 3, 3, 4, 'dark')]),
    P('rightWing', 'body', (-4, -1, 0), [B(-6, -0.5, -1, 6, 1, 2, 'dark'), B(-8, -1.5, -1.5, 3, 3, 3, 'plate')]),
    P('rightRotor', 'rightWing', (-6.5, -2, 0), [B(-5, -0.5, -1, 10, 1, 2, 'steel'), B(-1, -0.5, -5, 2, 1, 10, 'steel')]),
    P('leftWing', 'body', (4, -1, 0), [B(0, -0.5, -1, 6, 1, 2, 'dark'), B(5, -1.5, -1.5, 3, 3, 3, 'plate')]),
    P('leftRotor', 'leftWing', (6.5, -2, 0), [B(-5, -0.5, -1, 10, 1, 2, 'steel'), B(-1, -0.5, -5, 2, 1, 10, 'steel')]),
    P('rightArm', 'body', (-3, 4, 0), [B(-0.5, 0, -0.5, 1, 6, 1, 'dark'), B(-1.5, 6, -1.5, 3, 2, 3, 'steel')]),
    P('leftArm', 'body', (3, 4, 0), [B(-0.5, 0, -0.5, 1, 6, 1, 'dark'), B(-1.5, 6, -1.5, 3, 2, 3, 'steel')]),
    P('pod', 'body', (0, -4, 0), [B(-3, -3, -2, 6, 3, 4, 'dark', ('front', 1, 1, 1, 1, 'glow'), ('front', 4, 1, 1, 1, 'glow'))]),
])
ARCH['goliath'] = (128, 128, [
    P('body', 'root', (0, 10, 0), [B(-8, -14, -6, 16, 14, 12, 'plate', ('front', 3, 3, 10, 4, 'dark'), ('front', 6, 8, 4, 3, 'glow')),
                                   B(-6, 0, -5, 12, 3, 10, 'dark'), B(-5, -12, 6, 10, 10, 4, 'dark', ('back', 3, 7, 4, 3, 'glow'))]),
    P('head', 'body', (0, -14, -1), [B(-4, -5, -5, 8, 5, 8, 'plate', ('front', 1, 1, 6, 2, 'dark'))]),
    P('dome', 'head', (0, -5, 0), [B(-5, -4, -5, 10, 4, 10, 'plate', ('front', 1, 1, 8, 1, 'hazard')), B(-2.5, -7, -2.5, 5, 3, 5, 'steel')]),
    P('rightPod', 'body', (-8, -14, 1), [B(-4, -4, -4, 4, 4, 8, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('leftPod', 'body', (8, -14, 1), [B(0, -4, -4, 4, 4, 8, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('rightArm', 'body', (-9, -11, 0), [B(-4, -2, -3, 5, 11, 6, 'plate')]),
    P('rightFist', 'rightArm', (-1.5, 9, 0), [B(-1, -6, -1, 2, 6, 2, 'steel'), B(-3.5, 0, -4, 7, 8, 8, 'plate', ('front', 1, 5, 5, 2, 'dark'))]),
    P('leftArm', 'body', (9, -11, 0), [B(-1, -2, -3, 5, 11, 6, 'plate')]),
    P('leftFist', 'leftArm', (1.5, 9, 0), [B(-1, -6, -1, 2, 6, 2, 'steel'), B(-3.5, 0, -4, 7, 8, 8, 'plate', ('front', 1, 5, 5, 2, 'dark'))]),
    P('rightLeg', 'root', (-4, 10, 0), [B(-2, 0, -2, 4, 12, 4, 'dark'), B(-1.5, 6, -1.5, 3, 2, 3, 'steel'), B(-4, 12, -5.5, 8, 2, 10, 'plate', ('top', 1, 1, 6, 1, 'dark'))]),
    P('leftLeg', 'root', (4, 10, 0), [B(-2, 0, -2, 4, 12, 4, 'dark'), B(-1.5, 6, -1.5, 3, 2, 3, 'steel'), B(-4, 12, -5.5, 8, 2, 10, 'plate', ('top', 1, 1, 6, 1, 'dark'))]),
])
ARCH['tank'] = (256, 128, [
    P('hull', 'root', (0, 24, 0), [B(-10, -12, -14, 20, 6, 28, 'plate', ('front', 2, 1, 16, 2, 'dark')), B(-14, -8, -15, 5, 8, 30, 'dark'),
                                   B(9, -8, -15, 5, 8, 30, 'dark'), B(-8, -17, 6, 16, 5, 8, 'dark', ('back', 2, 1, 12, 1, 'plate'))]),
    P('turret', 'hull', (0, -12, -2), [B(-7, -6, -7, 14, 6, 14, 'plate', ('front', 2, 2, 10, 2, 'dark'), ('front', 4, 2, 2, 2, 'eye'), ('front', 8, 2, 2, 2, 'eye'))]),
    P('cannon', 'turret', (0, -3, -7), [B(-1.5, -1.5, -18, 3, 3, 18, 'dark'), B(-2, -2, -20, 4, 4, 2, 'steel', ('front', 1, 1, 2, 2, 'glow'))]),
    P('rightPod', 'turret', (-7, -4, 2), [B(-4, -3, -3, 4, 4, 6, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('leftPod', 'turret', (7, -4, 2), [B(0, -3, -3, 4, 4, 6, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('arm', 'hull', (0, -9, -14), [B(-2, -2, -6, 4, 4, 6, 'dark')]),
    P('blade', 'arm', (0, 0, -6), [B(-1, -1, -12, 2, 2, 12, 'dark'), B(-0.5, 1, -13, 1, 6, 9, 'steel')]),
    P('crane', 'hull', (0, -12, 12), [B(-1, -10, -1, 2, 10, 2, 'dark')]),
    P('boom', 'crane', (0, -10, 0), [B(-1, -1, 0, 2, 2, 14, 'dark')]),
    P('hammer', 'boom', (0, 0, 13), [B(-0.5, 0, -0.5, 1, 5, 1, 'dark'), B(-3, 5, -3, 6, 5, 6, 'steel')]),
])

def _multileg():
    parts = [P('body', 'root', (0, 12, 0), [B(-5, -3, -5, 10, 6, 10, 'plate', ('front', 1, 1, 8, 1, 'dark'), ('front', 4, 3, 2, 2, 'steel')), B(-6, 3, -6, 12, 1, 12, 'dark')]),
             P('head', 'body', (0, -3, 0), [B(-3.5, -7, -3.5, 7, 7, 7, 'plate', *EYE1(7, 2)), B(-2.5, -8, -2.5, 5, 1, 5, 'plate')])]
    for side, sx in (('R', -1), ('L', 1)):
        for i, z in enumerate((-3.5, -1.2, 1.2, 3.5)):
            n = f'leg{side}{i}'
            parts.append(P(n, 'body', (sx * 5, 3, z), [B(-0.5, 0, -0.5, 1, 5, 1, 'dark'), B(-1, 4, -1, 2, 2, 2, 'steel')], rot=(0, 0, sx * -1.0 * 1.0)))
            parts.append(P(f'low{side}{i}', n, (0, 5, 0), [B(-0.5, 0, -0.5, 1, 7, 1, 'dark'), B(-1, 6, -1, 2, 1, 2, 'steel')], rot=(0, 0, sx * 1.0)))
        parts.append(P(f'arm{side}', 'body', (sx * 5, -1, 0), [B(-0.5 if sx > 0 else -3, -1, -1, 3, 2, 2, 'dark')]))
        parts.append(P(f'panel{side}', f'arm{side}', (sx * 3, 0, 0), [B(-0.5, -8, -3.5, 1, 16, 7, 'plate', ('front', 0, 0, 1, 1, 'dark'))]))
    parts.append(P('handR', 'body', (-5, -2, -3), [B(-2, -1, -5, 4, 3, 6, 'dark'), B(-2.5, -2, -9, 5, 5, 4, 'plate', ('front', 1, 1, 1, 1, 'white'), ('front', 3, 1, 1, 1, 'white'), ('front', 1, 3, 3, 1, 'steel'))]))
    parts.append(P('handL', 'body', (5, -2, -3), [B(-2, -1, -5, 4, 3, 6, 'dark'), B(-2.5, -2, -9, 5, 5, 4, 'plate', ('front', 1, 1, 1, 1, 'white'), ('front', 3, 1, 1, 1, 'white'), ('front', 1, 3, 3, 1, 'steel'))]))
    return parts
ARCH['multileg'] = (128, 64, _multileg())
def _quad():
    parts = [P('body', 'root', (0, 12, 0), [B(-4, -3, -7, 8, 6, 14, 'plate', ('top', 2, 4, 4, 1, 'dark')), B(-5, 3, -6, 10, 1, 12, 'dark')]),
             P('head', 'body', (0, -1, -7), [B(-3, -4, -5, 6, 6, 6, 'plate', *EYE1(6, 1)), B(-2, 0, -7, 4, 2, 2, 'dark')]),
             P('antlers', 'head', (0, -4, -2), [B(-6, -9, 0, 2, 9, 1, 'steel'), B(4, -9, 0, 2, 9, 1, 'steel'), B(-9, -9, 0, 3, 1, 1, 'steel'), B(6, -9, 0, 3, 1, 1, 'steel')]),
    P('tail', 'body', (0, -2, 7), [B(-0.5, -1, 0, 1, 2, 5, 'dark'), B(-0.5, -4, 4, 1, 6, 6, 'steel')])]
    for side, sx in (('R', -1), ('L', 1)):
        for i, z in enumerate((-5, 5)):
            n = f'leg{side}{i}'
            parts.append(P(n, 'body', (sx * 4, 3, z), [B(-1, 0, -1, 2, 5, 2, 'dark')], rot=(0, 0, sx * -0.3)))
            parts.append(P(f'low{side}{i}', n, (0, 5, 0), [B(-0.5, 0, -0.5, 1, 6, 1, 'dark'), B(-1.5, 5, -2, 3, 1, 4, 'steel')], rot=(0, 0, sx * 0.3)))
    return parts
ARCH['quad'] = (128, 128, _quad())
ARCH['shade'] = (128, 64, [
    P('body', 'root', (0, 15, 0), [B(-5, -6, -4, 10, 9, 8, 'plate', ('front', 1, 2, 3, 3, 'white'), ('front', 2, 3, 1, 1, 'eye'), ('front', 6, 2, 3, 3, 'white'), ('front', 7, 3, 1, 1, 'eye'), ('front', 3, 7, 4, 1, 'dark')),
                                   B(-4, -8, -3, 8, 3, 6, 'plate'), B(-4, 3, -3, 8, 2, 6, 'dark')]),
    P('armR', 'body', (-5, -2, 0), [B(-2, -1, -1.5, 2, 9, 3, 'dark'), B(-3, 8, -2, 3, 3, 4, 'steel')]),
    P('armL', 'body', (5, -2, 0), [B(0, -1, -1.5, 2, 9, 3, 'dark'), B(0, 8, -2, 3, 3, 4, 'steel')]),
    P('legR', 'root', (-2.5, 18, 0), [B(-1.5, 0, -1.5, 3, 6, 3, 'dark'), B(-2, 5, -3, 4, 1, 5, 'steel')]),
    P('legL', 'root', (2.5, 18, 0), [B(-1.5, 0, -1.5, 3, 6, 3, 'dark'), B(-2, 5, -3, 4, 1, 5, 'steel')]),
    P('crest', 'body', (0, -8, 0), [B(-3, -3, -1, 1, 3, 2, 'steel'), B(-0.5, -4, -1, 1, 4, 2, 'steel'), B(2, -3, -1, 1, 3, 2, 'steel')]),
])
ARCH['exploder'] = (128, 64, [
    P('body', 'root', (0, 12, 0), [B(-4, -10, -3, 8, 10, 6, 'plate', ('front', 1, 3, 6, 1, 'dark'), ('front', 3, 6, 2, 2, 'steel')),
        B(-4, -14, -3, 8, 4, 6, 'hazard', ('front', 0, 0, 2, 4, 'black'), ('front', 4, 0, 2, 4, 'black'), ('back', 0, 0, 2, 4, 'black'), ('back', 4, 0, 2, 4, 'black'),
            ('left', 0, 0, 2, 4, 'black'), ('left', 4, 0, 2, 4, 'black'), ('right', 0, 0, 2, 4, 'black'), ('right', 4, 0, 2, 4, 'black'))]),
    P('antenna', 'body', (0, -14, 0), [B(-0.5, -5, -0.5, 1, 5, 1, 'steel')]),
    P('legR', 'root', (-2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-2, 11, -3, 4, 1, 5, 'plate')]),
    P('legL', 'root', (2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-2, 11, -3, 4, 1, 5, 'plate')]),
])
ARCH['monster'] = (128, 128, [
    P('body', 'root', (0, 14, 0), [B(-6, -6, -5, 12, 10, 10, 'plate', ('front', 3, 3, 6, 5, 'dark')), B(-4, -9, -1, 8, 3, 2, 'steel'), B(-5, 4, -4, 10, 1, 8, 'dark')]),
    P('head', 'body', (0, -4, -5), [B(-4, -5, -9, 8, 5, 10, 'plate', ('front', 1, 1, 2, 2, 'eye'), ('front', 5, 1, 2, 2, 'eye')), B(-3, -8, -5, 6, 2, 3, 'steel')]),
    P('jaw', 'head', (0, 0, -1), [B(-3.5, 0, -9, 7, 2, 9, 'dark'), B(-3, -1, -9, 6, 1, 1, 'steel')]),
    P('armR', 'body', (-6, -3, -2), [B(-1, 0, -1, 2, 6, 2, 'dark'), B(-2, 6, -3, 4, 1, 4, 'steel'), B(-0.5, 7, -4, 1, 1, 3, 'steel')]),
    P('armL', 'body', (6, -3, -2), [B(-1, 0, -1, 2, 6, 2, 'dark'), B(-2, 6, -3, 4, 1, 4, 'steel'), B(-0.5, 7, -4, 1, 1, 3, 'steel')]),
    P('legR', 'root', (-3, 14, 1), [B(-1, 0, -1, 2, 8, 2, 'dark'), B(-3, 8, -5, 6, 1, 7, 'plate'), B(-0.5, 8, -7, 1, 1, 2, 'steel'), B(-2.5, 8, -7, 1, 1, 2, 'steel'), B(1.5, 8, -7, 1, 1, 2, 'steel')]),
    P('legL', 'root', (3, 14, 1), [B(-1, 0, -1, 2, 8, 2, 'dark'), B(-3, 8, -5, 6, 1, 7, 'plate'), B(-0.5, 8, -7, 1, 1, 2, 'steel'), B(-2.5, 8, -7, 1, 1, 2, 'steel'), B(1.5, 8, -7, 1, 1, 2, 'steel')]),
])
ARCH['sphere'] = (128, 128, [
    P('ball', 'root', (0, 18, 0), [B(-6, -4, -4, 12, 8, 8, 'plate'), B(-4, -6, -4, 8, 12, 8, 'plate'),
                                   B(-4, -4, -6, 8, 8, 12, 'plate', ('front', 0, 3, 8, 2, 'dark'), ('front', 1, 1, 6, 6, 'dark'), ('front', 2, 2, 4, 4, 'white'), ('front', 3, 3, 2, 2, 'eye')),
                                   B(-6.5, -1, -6.5, 13, 2, 13, 'dark')]),
    P('disc', 'root', (0, 18, 0), [B(-9, -1, -9, 18, 5, 18, 'plate', ('front', 6, 2, 6, 2, 'steel')), B(-10, 2, -10, 20, 1, 20, 'steel')]),
    P('hhead', 'root', (0, 18, 0), [B(-4, -9, -4, 8, 8, 8, 'plate', *EYE1(8, 3)), B(-1, -11, -1, 2, 2, 2, 'steel')]),
    P('rightFin', 'ball', (-6, 0, 0), [B(-1, -3, -3, 1, 6, 6, 'steel')]),
    P('leftFin', 'ball', (6, 0, 0), [B(0, -3, -3, 1, 6, 6, 'steel')]),
    P('topFin', 'ball', (0, -6, 0), [B(-3, -1, -1, 6, 1, 2, 'steel')]),
    P('saw', 'ball', (0, -6, 0), [B(-7, -2, -7, 14, 1, 14, 'steel'), B(-1.5, -7, -1.5, 3, 5, 3, 'dark'), B(-8, -2, -1, 1, 1, 2, 'dark'), B(7, -2, -1, 1, 1, 2, 'dark'), B(-1, -2, -8, 2, 1, 1, 'dark'), B(-1, -2, 7, 2, 1, 1, 'dark')]),
    P('link1', 'ball', (0, 0, 10), [B(-4, -4, -4, 8, 8, 8, 'plate'), B(-1, -1, -6, 2, 2, 3, 'dark')]),
    P('link2', 'link1', (0, 0, 8), [B(-3, -3, -3, 6, 6, 6, 'plate'), B(-0.5, -0.5, -5, 1, 1, 3, 'dark')]),
    P('link3', 'link2', (0, 0, 6), [B(-2.5, -2.5, -2.5, 5, 5, 5, 'plate'), B(-0.5, -0.5, -4, 1, 1, 2, 'dark')]),
    P('drill', 'ball', (0, -6, 0), [B(-3, -7, -3, 6, 7, 6, 'dark'), B(-2, -11, -2, 4, 4, 4, 'steel'), B(-1, -13, -1, 2, 2, 2, 'steel')]),
])

SKINS = {  # plate, dark, steel, eye, glow
    '': ((188, 180, 166), (104, 96, 86), (170, 168, 160), (255, 48, 32), (255, 150, 40)),
    '_gold': ((222, 176, 58), (112, 84, 30), (240, 220, 150), (255, 60, 30), (255, 230, 120)),
    '_hacked': ((128, 120, 104), (52, 56, 60), (150, 160, 170), (70, 230, 255), (90, 200, 255)),
    '_rampage': ((176, 158, 148), (84, 48, 44), (150, 138, 138), (255, 20, 20), (255, 70, 40)),
    '_koshi': ((228, 230, 238), (70, 76, 96), (190, 200, 230), (90, 190, 255), (120, 220, 255)),
    '_roshi': ((34, 32, 42), (14, 12, 20), (90, 70, 130), (190, 90, 255), (210, 120, 255)),
    '_bronze': ((176, 140, 84), (92, 70, 44), (200, 170, 110), (255, 60, 30), (255, 200, 90)),
    '_blood': ((160, 56, 48), (80, 28, 26), (180, 150, 140), (255, 40, 30), (255, 90, 60)),
    '_grave': ((110, 124, 100), (52, 62, 48), (150, 160, 140), (255, 60, 30), (255, 160, 60)),
    '_clown': ((200, 190, 210), (70, 40, 96), (230, 225, 235), (255, 40, 80), (255, 120, 160)),
    '_dying': ((116, 120, 100), (60, 64, 54), (150, 150, 140), (255, 50, 30), (255, 140, 60)),
    '_monster': ((140, 100, 76), (70, 50, 40), (180, 150, 120), (255, 200, 60), (255, 160, 60)),
    '_animal': ((190, 180, 166), (100, 96, 88), (210, 200, 190), (255, 210, 80), (255, 160, 60)),
    '_samurai': ((122, 60, 56), (50, 34, 32), (170, 140, 120), (255, 220, 80), (255, 140, 60)),
    '_flightw': ((236, 238, 242), (120, 126, 136), (205, 210, 220), (120, 255, 160), (160, 230, 255)),
    '_flightb': ((36, 38, 44), (16, 18, 22), (130, 136, 148), (255, 90, 90), (255, 170, 90)),
    '_blue': ((90, 150, 200), (40, 70, 110), (200, 215, 235), (255, 230, 80), (255, 230, 120)),
    '_pale': ((222, 222, 214), (150, 150, 142), (235, 235, 228), (255, 255, 255), (255, 255, 200)),
    '_tier': ((196, 70, 56), (50, 30, 30), (170, 150, 140), (80, 255, 120), (120, 255, 150)),
    '_reverse': ((150, 100, 76), (64, 50, 44), (150, 140, 128), (255, 60, 40), (255, 150, 60)),
    '_shade': ((22, 20, 28), (8, 8, 12), (84, 84, 100), (0, 0, 0), (255, 255, 255)),
    '_wolf': ((150, 112, 72), (84, 60, 40), (210, 190, 160), (255, 220, 80), (255, 200, 80)),
    '_scorpion': ((96, 64, 44), (50, 32, 24), (150, 120, 90), (255, 200, 60), (255, 160, 60)),
    '_silver': ((206, 212, 224), (90, 96, 116), (236, 238, 245), (255, 60, 60), (255, 200, 80)),
    '_roc': ((46, 42, 40), (24, 22, 22), (214, 170, 70), (255, 240, 120), (255, 240, 160)),
    '_jack': ((22, 20, 26), (10, 10, 12), (236, 206, 40), (255, 230, 40), (255, 230, 60)),
    '_louise': ((24, 22, 28), (10, 8, 12), (206, 164, 60), (255, 200, 90), (255, 180, 60)),
    '_wendy': ((30, 40, 48), (12, 16, 20), (180, 44, 56), (255, 255, 255), (255, 120, 120)),
    '_hook': ((70, 84, 96), (30, 38, 46), (186, 198, 208), (255, 255, 255), (200, 230, 255)),
    '_stone': ((152, 150, 144), (84, 84, 80), (196, 194, 188), (0, 0, 0), (255, 255, 255)),
    '_geppetto': ((74, 80, 96), (30, 32, 40), (214, 156, 64), (255, 80, 60), (255, 120, 60)),
    '_beepy': ((70, 112, 176), (30, 46, 80), (236, 206, 60), (255, 240, 120), (255, 230, 100)),
    '_cube': ((124, 114, 104), (62, 58, 54), (186, 176, 164), (255, 90, 200), (255, 90, 210)),
    '_burn': ((176, 76, 44), (70, 34, 26), (200, 110, 80), (255, 220, 70), (255, 200, 60)),
}

ARCH_BASE = {'goliath': ((150, 104, 74), (82, 62, 48), (150, 140, 128), (255, 48, 32), (255, 150, 40)),
             'tank': ((190, 170, 128), (98, 86, 62), (160, 154, 140), (255, 48, 32), (255, 150, 40))}
NEED = {'_beepy': ('stubby',), '_cube': ('goliath',), '_geppetto': ('tank',), '_wendy': ('sphere',), '_hook': ('monster',), '_jack': ('monster',), '_louise': ('multileg',), '_stone': ('sphere', 'monster'), '_silver': ('biped',), '_roc': ('quad',), '_shade': ('shade', 'quad', 'multileg', 'flyer', 'monster'), '_wolf': ('quad',), '_scorpion': ('quad',), '_blue': ('sphere',), '_pale': ('sphere',), '_tier': ('stubby',), '_reverse': ('goliath',), '_samurai': ('stubby',), '_flightw': ('flyer',), '_flightb': ('flyer',), '_bronze': ('stubby',), '_blood': ('stubby',), '_grave': ('stubby',), '_clown': ('biped',), '_dying': ('goliath',), '_monster': ('monster',), '_animal': ('quad',), '_koshi': ('biped',), '_roshi': ('biped',), '_rampage': ('stubby', 'biped'), '_gold': ('stubby', 'goliath', 'tank'), '_hacked': ('stubby', 'biped', 'sphere'), '_burn': ('stubby', 'sphere')}

def pack(w, boxes):
    # shelf packer; footprint of a box = (2(d+w), d+h)
    out, x, y, row = [], 0, 0, 0
    items = sorted(range(len(boxes)), key=lambda i: -boxes[i][1])
    uv = {}
    for i in items:
        bw, bh = boxes[i]
        if x + bw > w: x, y, row = 0, y + row, 0
        uv[i] = (x, y); x += bw; row = max(row, bh)
    return uv, y + row

def fmt(v): return (str(int(v)) if float(v).is_integer() else str(v)) + 'F'

java = ['// GENERATED by tools/gen/machine.py - do not edit by hand.', 'package com.nier.automata.unit.machine.client;', '',
        'import net.minecraft.client.model.geom.PartPose;', 'import net.minecraft.client.model.geom.builders.*;', '',
        'public final class MachineGeo {', '    private MachineGeo() {}']
os.makedirs(RES + '/textures/entity', exist_ok=True)
for arch, (W, H, parts) in ARCH.items():
    flat = [(pi, bi) for pi, p in enumerate(parts) for bi in range(len(p[4]))]
    dims = [(2 * (int(b[5]) + int(b[3])), int(b[5]) + int(b[4])) for b in (parts[pi][4][bi] for pi, bi in flat)]
    uv, used = pack(W, dims)
    assert used <= H, f'{arch}: texture too small ({used} > {H})'
    uvmap = {flat[i]: uv[i] for i in range(len(flat))}
    paths = {}
    for (name, parent, *_rest) in parts: paths[name] = name if parent == 'root' else paths[parent] + '/' + name
    java.append(f'    public static final String[] {arch.upper()}_PATHS = {{' + ', '.join(f'"{paths[p[0]]}"' for p in parts) + '};')
    java.append(f'    public static LayerDefinition {arch}() {{')
    java.append('        MeshDefinition mesh = new MeshDefinition(); PartDefinition root = mesh.getRoot();')
    for pi, (name, parent, pv, rot, boxes) in enumerate(parts):
        cubes = ''.join(f'.texOffs({uvmap[(pi, bi)][0]}, {uvmap[(pi, bi)][1]}).addBox({", ".join(fmt(c) for c in b[:6])})' for bi, b in enumerate(boxes))
        java.append(f'        PartDefinition {name} = {parent}.addOrReplaceChild("{name}", CubeListBuilder.create(){cubes}, '
                    f'PartPose.offsetAndRotation({", ".join(fmt(c) for c in pv)}, {", ".join(fmt(c) for c in rot)}));')
    java.append(f'        return LayerDefinition.create(mesh, {W}, {H});')
    java.append('    }')
    for suffix, (plate, dark, steel, eye, glow) in SKINS.items():
        if suffix and arch not in NEED[suffix]: continue
        if not suffix and arch in ARCH_BASE: plate, dark, steel, eye, glow = ARCH_BASE[arch]
        mats = {'plate': plate, 'dark': dark, 'steel': steel, 'eye': eye, 'glow': glow, 'white': (255, 40, 30) if suffix == '_rampage' else (238, 234, 222), 'black': (22, 20, 24), 'hazard': (200, 48, 40), 'gold': (230, 190, 90), 'purple': (96, 50, 130)}
        t = Tex(W, H, seed=len(arch) * 7919)
        for pi, (name, parent, pv, rot, boxes) in enumerate(parts):
            for bi, b in enumerate(boxes):
                u, v = uvmap[(pi, bi)]; w, h, d = int(b[3]), int(b[4]), int(b[5])
                t.box(u, v, w, h, d, mats[b[6]], noise=7 if b[6] == 'plate' else 4)
                if b[6] == 'plate':  # rust / wear spots and panel seams
                    for _ in range(max(1, (w * h) // 12)):
                        fx, fy = t.rnd.randrange(w), t.rnd.randrange(h)
                        t.face(u, v, w, h, d, t.rnd.choice(['front', 'left', 'right', 'back']), fx, fy, 1, 1,
                               tuple(int(c * 0.72) for c in plate))
                    if h > 4: t.face(u, v, w, h, d, 'front', 0, h - 1, w, 1, tuple(int(c * 0.8) for c in plate))
                for (face, x, y, fw, fh, m) in b[7]:
                    t.face(u, v, w, h, d, face, x, y, fw, fh, mats[m])
        t.save(f'{RES}/textures/entity/machine_{arch}{suffix}.png')
java.append('}')
open(os.path.join(ROOT, 'src/main/java/com/nier/automata/unit/machine/client/MachineGeo.java'), 'w').write('\n'.join(java) + '\n')

# ---- lang parts: id -> (english, japanese) ----
NAMES = {
    'machine_goose': ('Goose', 'ガチョウ'),
    'machine_kalil': ('Kalil', 'カリル'),
    'machine_beepy': ('Beepy (P-33)', 'ビーピー (P-33)'),
    'machine_shahriyar': ('Shahriyar', 'シャフリヤール'),
    'machine_geppetto': ('Defense System: Geppetto', '防衛システム ゲッペット'),
    'machine_jack': ('Jack', 'ジャック'),
    'machine_louise': ('Louise', 'ルイーズ'),
    'machine_wendy': ('Wendy', 'ウェンディ'),
    'machine_mourning_mother': ('Mourning Mother', '嘆きの母'),
    'machine_hansel': ('Hansel', 'ヘンゼル'),
    'machine_gretel': ('Gretel', 'グレーテル'),
    'machine_roc': ('Roc', 'ロック'),
    'machine_hook': ('Hook', 'フック'),
    'machine_shade_small': ('Shade', 'シェイド'),
    'machine_shade_medium': ('Medium Shade', '中型シェイド'),
    'machine_shade_large': ('Large Shade', '大型シェイド'),
    'machine_shade_giant': ('Giant Shade', '巨大シェイド'),
    'machine_shade_beast': ('Shade Beast', 'シェイド・ビースト'),
    'machine_shade_beetle': ('Shade Beetle', 'シェイド・ビートル'),
    'machine_shade_flyer': ('Shade Flyer', 'シェイド・フライヤー'),
    'machine_shade_multi_limb': ('Multi-limb Shade', '多肢シェイド'),
    'machine_wolf': ('Wolf', 'オオカミ'),
    'machine_scorpion': ('Scorpion', 'サソリ'),
    'machine_high_speed': ('High-speed Machine', '高速型'),
    'machine_emil_clone': ('Emil Clones', 'エミールのクローン'),
    'machine_multi_tier': ('Multi-tier Type', '多段型'),
    'machine_multi_tier_village': ('Multi-tier Type (Village)', '多段型 (村)'),
    'machine_reverse_goliath': ('Reverse-jointed Goliath', '逆関節ゴリアテ'),
    'machine_masamune': ('Masamune', 'マサムネ'), 'machine_ho229_white': ('Flight Unit Ho229 Type-B', '飛行ユニット Ho229 Type-B'), 'machine_ho229_black': ('Flight Unit Ho229 Type-B (Black)', '飛行ユニット Ho229 Type-B (黒)'),
    'machine_gunman_stubby': ('Gunman Stubby', 'ガンマン短足'),
    'machine_gunman_biped': ('Gunman Biped', 'ガンマン二足'),
    'machine_gunman_flyer': ('Gunman Flyer', 'ガンマン飛行体'),
    'machine_dying_goliath': ('Dying Goliath Biped', '瀕死の超大型二足'),
    'machine_monster_type': ('Monster Type', '怪物型'),
    'machine_animal_machine': ('Animal Machine', '動物機械'),
    'machine_park_rabbit': ('Amusement Park Rabbit', '遊園地のウサギ'),
    'machine_blood_drenched': ('Blood-drenched Machine', '血まみれの機械'),
    'machine_courageous_brother': ('Courageous Brother', '勇敢な兄'),
    'machine_hateful_sister': ('Hateful Sister', '憎む姉'),
    'machine_vengeful_child': ('Vengeful Child', '復讐する子'),
    'machine_gravekeeper': ('Gravekeeper', '墓守'),
    'machine_zombie_clown': ('Zombie Clown', 'ゾンビ・クラウン'),
    'machine_small_stubby': ('Small Stubby', '小型短足'), 'machine_small_stubby_axe': ('Small Stubby (Axe)', '小型短足 (斧)'),
    'machine_small_stubby_spear': ('Small Stubby (Spear)', '小型短足 (槍)'), 'machine_small_stubby_hammer': ('Small Stubby (Hammer)', '小型短足 (槌)'),
    'machine_small_stubby_staff': ('Small Stubby (Staff)', '小型短足 (杖)'), 'machine_small_stubby_shield': ('Small Stubby (Shield)', '小型短足 (盾)'),
    'machine_gold_stubby_axe': ('Gold Stubby (Axe)', '金色短足 (斧)'), 'machine_gold_stubby_spear': ('Gold Stubby (Spear)', '金色短足 (槍)'),
    'machine_gold_stubby_hammer': ('Gold Stubby (Hammer)', '金色短足 (槌)'), 'machine_gold_stubby_staff': ('Gold Stubby (Staff)', '金色短足 (杖)'),
    'machine_medium_stubby': ('Medium Stubby', '中型短足'), 'machine_large_stubby': ('Large Stubby', '大型短足'),
    'machine_hacked_stubby': ('Hacked Small Stubby', 'ハッキング小型短足'),
    'machine_small_biped': ('Small Biped', '小型二足'), 'machine_medium_biped': ('Medium Biped', '中型二足'),
    'machine_medium_biped_shield': ('Medium Biped (Shield)', '中型二足 (盾)'), 'machine_large_biped': ('Large Biped', '大型二足'),
    'machine_hacked_biped': ('Hacked Medium Biped', 'ハッキング中型二足'),
    'machine_small_flyer': ('Small Flyer', '小型飛行体'), 'machine_medium_flyer': ('Medium Flyer', '中型飛行体'),
    'machine_flyer': ('Machine Flyer', '飛行型機械生命体'),
    'machine_small_goliath': ('Small Goliath Biped', '小型超大型二足'), 'machine_medium_goliath': ('Goliath Biped', '超大型二足'),
    'machine_large_goliath': ('Large Goliath Biped', '大型超大型二足'), 'machine_goliath_tank': ('Goliath Tank', '超大型戦車'),
    'machine_multileg': ('Medium Multi-Leg', '中型多脚'), 'machine_drill_sphere': ('Drill Sphere', 'ドリル球体'),
    'machine_small_exploder': ('Small Exploder', '小型自爆型'), 'machine_saw_sphere': ('Saw Sphere', '丸ノコ球体'),
    'machine_medium_quadruped': ('Medium Quadruped', '中型四足'),
    'machine_gold_goliath': ('Gold Goliath Biped', '金色超大型二足'), 'machine_gold_tank': ('Gold Tank', '金色戦車'),
    'machine_medium_exploder': ('Medium Exploder', '中型自爆型'), 'machine_goliath_flyer': ('Goliath Flyer', '超大型飛行体'),
    'machine_rampaging_small_stubby': ('Rampaging Small Stubby', '暴走小型短足'), 'machine_rampaging_small_biped': ('Rampaging Small Biped', '暴走小型二足'),
    'machine_rampaging_medium_biped': ('Rampaging Medium Biped', '暴走中型二足'),
    'machine_linked_sphere': ('Linked-sphere Type', '連結球体型'),
    'machine_ko_shi': ('Ko-Shi', 'コ・シ'), 'machine_ro_shi': ('Ro-Shi', 'ロ・シ'),
    'machine_sphere': ('Sphere', '球体'), 'machine_hacked_sphere': ('Hacked Sphere', 'ハッキング球体'),
}
os.makedirs(RES + '/lang/parts', exist_ok=True)
for i, (en, ja) in NAMES.items():
    json.dump({f'entity.nierautomata.{i}': en, f'item.nierautomata.{i}_spawn_egg': en + ' Spawn Egg'},
              open(f'{RES}/lang/parts/{i}.json', 'w'), ensure_ascii=False, indent=1)
    json.dump({f'entity.nierautomata.{i}': ja, f'item.nierautomata.{i}_spawn_egg': ja + 'のスポーンエッグ'},
              open(f'{RES}/lang/parts/{i}.ja.json', 'w'), ensure_ascii=False, indent=1)
print('ok')
