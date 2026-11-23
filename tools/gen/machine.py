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
ARCH['stubby'] = (128, 64, [
    P('body', 'root', (0, 18, 0), [B(-4, -7, -3.5, 8, 7, 7, 'plate', ('front', 3, 2, 2, 2, 'steel'), ('front', 3, 3, 2, 1, 'dark'), ('front', 4, 2, 1, 2, 'dark')), B(-4.5, -3, -4, 9, 2, 8, 'dark')]),
    P('head', 'body', (0, -7, 0), [B(-4.5, -8, -4.5, 9, 8, 9, 'plate', *EYE1(9, 2), ('front', 1, 6, 1, 1, 'steel')),
                                   B(-3, -9, -3, 6, 1, 6, 'plate'), B(-5, -2, -5, 10, 1, 10, 'dark')]),
    P('rightArm', 'body', (-4.5, -6, 0), [B(-2.5, -1, -1.5, 3, 8, 3, 'dark'), B(-3, 7, -2, 4, 3, 4, 'plate')]),
    P('leftArm', 'body', (4.5, -6, 0), [B(-0.5, -1, -1.5, 3, 8, 3, 'dark'), B(-1, 7, -2, 4, 3, 4, 'plate')]),
    P('rightLeg', 'root', (-2, 18, 0), [B(-1.5, 0, -1.5, 3, 4, 3, 'dark'), B(-2.5, 4, -3, 5, 2, 6, 'plate', ('top', 1, 1, 3, 1, 'dark'))]),
    P('leftLeg', 'root', (2, 18, 0), [B(-1.5, 0, -1.5, 3, 4, 3, 'dark'), B(-2.5, 4, -3, 5, 2, 6, 'plate', ('top', 1, 1, 3, 1, 'dark'))]),
    P('weapon', 'rightArm', (-1, 8.5, 0), []),
    P('axe', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -12, 1, 1, 14, 'dark'), B(-0.5, -5.5, -13, 1, 5, 5, 'steel'), B(-0.5, 0.5, -12.5, 1, 2, 3, 'steel')]),
    P('spear', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -18, 1, 1, 22, 'dark'), B(-1, -1, -22, 2, 2, 4, 'steel')]),
    P('hammer', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -11, 1, 1, 13, 'dark'), B(-2.5, -2.5, -15, 5, 5, 5, 'steel')]),
    P('staff', 'weapon', (0, 0, 0), [B(-0.5, -0.5, -14, 1, 1, 17, 'dark'), B(-1.5, -1.5, -17, 3, 3, 3, 'glow')]),
    P('shield', 'leftArm', (1, 5, -2), [B(-4, -5, -1.5, 9, 10, 1, 'plate', ('front', 3, 3, 3, 4, 'dark'))]),
])
ARCH['biped'] = (128, 64, [
    P('body', 'root', (0, 12, 0), [B(-3, -1, -2, 6, 3, 4, 'dark'), B(-4, -11, -3, 8, 10, 6, 'plate', ('front', 1, 2, 6, 2, 'dark'), ('front', 3, 5, 2, 3, 'dark'))]),
    P('head', 'body', (0, -11, 0), [B(-4, -7, -4, 8, 7, 8, 'plate', *EYE1(8, 2)), B(-3, -8, -3, 6, 1, 6, 'plate')]),
    P('rightArm', 'body', (-5, -10, 0), [B(-1.5, -1, -1, 2, 12, 2, 'dark'), B(-2.5, -2, -2, 3, 3, 4, 'plate'), B(-2, 11, -1.5, 3, 3, 3, 'plate')]),
    P('leftArm', 'body', (5, -10, 0), [B(-0.5, -1, -1, 2, 12, 2, 'dark'), B(-0.5, -2, -2, 3, 3, 4, 'plate'), B(-1, 11, -1.5, 3, 3, 3, 'plate')]),
    P('rightLeg', 'root', (-2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-1.5, 4, -1.5, 3, 3, 3, 'plate'), B(-1.5, 10, -2.5, 3, 2, 4, 'plate')]),
    P('leftLeg', 'root', (2, 12, 0), [B(-1, 0, -1, 2, 11, 2, 'dark'), B(-1.5, 4, -1.5, 3, 3, 3, 'plate'), B(-1.5, 10, -2.5, 3, 2, 4, 'plate')]),
    P('club', 'rightArm', (-0.5, 12.5, 0), [B(-0.5, -0.5, -5, 1, 1, 6, 'dark'), B(-1.5, -1.5, -15, 3, 3, 10, 'plate', ('top', 0, 2, 3, 1, 'dark'))]),
    P('shield', 'leftArm', (0.5, 8, -1.5), [B(-5, -6, -1, 11, 13, 1, 'plate', ('front', 4, 4, 3, 5, 'dark'), ('front', 5, 6, 1, 1, 'eye'))]),
])
ARCH['flyer'] = (64, 64, [
    P('body', 'root', (0, 12, 0), [B(-4, -4, -4, 8, 8, 8, 'plate', *EYE1(8, 2)), B(-1, 4, -4, 2, 2, 4, 'dark')]),
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
    P('rightPod', 'body', (-8, -14, 1), [B(-4, -4, -4, 4, 4, 8, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('leftPod', 'body', (8, -14, 1), [B(0, -4, -4, 4, 4, 8, 'dark', ('front', 1, 1, 2, 2, 'glow'))]),
    P('rightArm', 'body', (-9, -11, 0), [B(-4, -2, -3, 5, 11, 6, 'plate')]),
    P('rightFist', 'rightArm', (-1.5, 9, 0), [B(-1, -6, -1, 2, 6, 2, 'steel'), B(-3.5, 0, -4, 7, 8, 8, 'plate', ('front', 1, 5, 5, 2, 'dark'))]),
    P('leftArm', 'body', (9, -11, 0), [B(-1, -2, -3, 5, 11, 6, 'plate')]),
    P('leftFist', 'leftArm', (1.5, 9, 0), [B(-1, -6, -1, 2, 6, 2, 'steel'), B(-3.5, 0, -4, 7, 8, 8, 'plate', ('front', 1, 5, 5, 2, 'dark'))]),
    P('rightLeg', 'root', (-4, 10, 0), [B(-3, 0, -3, 6, 12, 6, 'dark'), B(-3.5, 12, -5, 7, 2, 9, 'plate')]),
    P('leftLeg', 'root', (4, 10, 0), [B(-3, 0, -3, 6, 12, 6, 'dark'), B(-3.5, 12, -5, 7, 2, 9, 'plate')]),
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
ARCH['sphere'] = (64, 64, [
    P('ball', 'root', (0, 18, 0), [B(-6, -6, -6, 12, 12, 12, 'plate', ('front', 0, 5, 12, 2, 'dark'), ('front', 4, 4, 4, 4, 'dark'), ('front', 5, 5, 2, 2, 'eye')),
                                   B(-6.5, -1, -6.5, 13, 2, 13, 'dark')]),
    P('rightFin', 'ball', (-6, 0, 0), [B(-1, -3, -3, 1, 6, 6, 'steel')]),
    P('leftFin', 'ball', (6, 0, 0), [B(0, -3, -3, 1, 6, 6, 'steel')]),
    P('topFin', 'ball', (0, -6, 0), [B(-3, -1, -1, 6, 1, 2, 'steel')]),
])

SKINS = {  # plate, dark, steel, eye, glow
    '': ((188, 180, 166), (104, 96, 86), (170, 168, 160), (255, 48, 32), (255, 150, 40)),
    '_gold': ((222, 176, 58), (112, 84, 30), (240, 220, 150), (255, 60, 30), (255, 230, 120)),
    '_hacked': ((128, 120, 104), (52, 56, 60), (150, 160, 170), (70, 230, 255), (90, 200, 255)),
    '_burn': ((176, 76, 44), (70, 34, 26), (200, 110, 80), (255, 220, 70), (255, 200, 60)),
}

ARCH_BASE = {'goliath': ((150, 104, 74), (82, 62, 48), (150, 140, 128), (255, 48, 32), (255, 150, 40)),
             'tank': ((190, 170, 128), (98, 86, 62), (160, 154, 140), (255, 48, 32), (255, 150, 40))}
NEED = {'_gold': ('stubby',), '_hacked': ('stubby', 'biped', 'sphere'), '_burn': ('stubby', 'sphere')}

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
        mats = {'plate': plate, 'dark': dark, 'steel': steel, 'eye': eye, 'glow': glow, 'white': (238, 234, 222)}
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
    'machine_sphere': ('Sphere', '球体'), 'machine_hacked_sphere': ('Hacked Sphere', 'ハッキング球体'),
}
os.makedirs(RES + '/lang/parts', exist_ok=True)
for i, (en, ja) in NAMES.items():
    json.dump({f'entity.nierautomata.{i}': en, f'item.nierautomata.{i}_spawn_egg': en + ' Spawn Egg'},
              open(f'{RES}/lang/parts/{i}.json', 'w'), ensure_ascii=False, indent=1)
    json.dump({f'entity.nierautomata.{i}': ja, f'item.nierautomata.{i}_spawn_egg': ja + 'のスポーンエッグ'},
              open(f'{RES}/lang/parts/{i}.ja.json', 'w'), ensure_ascii=False, indent=1)
print('ok')
