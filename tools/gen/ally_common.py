"""Textures for the android roster (com.nier.automata.unit.ally). Original designs; UVs mirror AllyModel / *Client.java.
Run any tools/gen/ally_*.py, or this file directly to regenerate all of them."""
import os, sys
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
from boxtex import Tex

OUT = os.path.join(os.path.dirname(__file__), '..', '..', 'src/main/resources/assets/nierautomata/textures/entity')

def band(t, u, v, w, h, d, y0, y1, c):
    """Paint rows y0..y1 (face pixels from the top) around the four side faces of a box."""
    t.rect(u, v + d + y0, 2 * (d + w), y1 - y0, c, 4)

def humanoid(t, skin, suit, legs, boots, hair, eye, gloves=None, trim=None):
    t.box(0, 0, 8, 8, 8, skin, top=hair)                                     # head
    t.face(0, 0, 8, 8, 8, 'back', 0, 0, 8, 8, hair)
    for side in ('right', 'left'): t.face(0, 0, 8, 8, 8, side, 0, 0, 8, 4, hair)
    t.face(0, 0, 8, 8, 8, 'front', 0, 0, 8, 2, hair)
    t.face(0, 0, 8, 8, 8, 'front', 1, 4, 2, 1, eye); t.face(0, 0, 8, 8, 8, 'front', 5, 4, 2, 1, eye)
    t.face(0, 0, 8, 8, 8, 'front', 3, 6, 2, 1, tuple(int(c * .8) for c in skin))
    t.face_detail(skin, eye)
    t.strands(24, 8, 8, 8, hair); t.strands(0, 8, 8, 4, hair); t.strands(16, 8, 8, 4, hair); t.strands(8, 0, 8, 8, hair)
    t.box(16, 16, 8, 12, 4, suit)                                            # torso
    if trim:
        t.face(16, 16, 8, 12, 4, 'front', 3, 0, 2, 12, trim); band(t, 16, 16, 8, 12, 4, 10, 11, trim)
    for u in (0, 12):                                                        # arms
        t.box(u, 32, 3, 12, 3, suit); band(t, u, 32, 3, 12, 3, 9, 12, gloves or skin)
        t.rect(u + 3, 32, 3, 3, gloves or skin)
    for u in (24, 36):                                                       # legs
        t.box(u, 32, 3, 12, 3, legs); band(t, u, 32, 3, 12, 3, 7, 12, boots)
        t.rect(u + 6, 32, 3, 3, boots)

def pod(t, body=(200, 186, 150), dark=(70, 64, 56)):
    t.box(96, 0, 4, 5, 6, body); t.face(96, 0, 4, 5, 6, 'front', 1, 1, 2, 1, dark)
    t.box(116, 0, 1, 3, 4, dark)

def sword(t, blade=(200, 205, 215), hilt=(30, 30, 34), guard=(40, 40, 44), l=14):
    t.box(82, 32, 1, 4, 1, hilt); t.box(70, 32, 4, 1, 2, guard); t.box(64, 32, 1, l, 2, blade)
    t.face(64, 32, 1, l, 2, 'right', 0, 0, 1, l, tuple(min(255, c + 30) for c in blade))

def great_sword(t, blade=(70, 70, 78), edge=(210, 210, 220), l=20):
    t.box(64, 48, 4, l, 1, blade); t.face(64, 48, 4, l, 1, 'front', 0, 0, 1, l, edge); t.face(64, 48, 4, l, 1, 'back', 3, 0, 1, l, edge)

def spear(t, shaft=(110, 80, 50), tip=(190, 195, 200)):
    t.box(86, 32, 1, 24, 1, shaft); t.box(90, 32, 2, 5, 1, tip)

def rifle(t, body=(45, 45, 48), metal=(120, 120, 126)):
    t.box(96, 32, 2, 10, 3, body); t.box(106, 32, 1, 4, 1, metal)

BLACK, CLOTH, SKIN, PALE, DARKSKIN = (24, 24, 28), (34, 34, 40), (236, 208, 186), (240, 226, 214), (150, 104, 78)

def ally_yorha_soldier(hair=(232, 232, 240), blind=(10, 10, 12), seed=11):
    t = Tex(128, 128, seed)
    humanoid(t, SKIN, BLACK, BLACK, (16, 16, 18), hair, (80, 80, 90), gloves=(20, 20, 22), trim=(235, 235, 240))
    t.box(48, 16, 9, 4, 9, hair); t.box(48, 0, 9, 3, 2, hair)          # hair
    t.box(84, 16, 8, 2, 1, blind); t.face(84, 16, 8, 2, 1, 'front', 0, 0, 8, 2, blind)   # blindfold / visor
    t.box(0, 64, 9, 7, 5, CLOTH); band(t, 0, 64, 9, 7, 5, 0, 1, (180, 170, 150))      # coat skirt
    t.box(28, 64, 9, 3, 5, (20, 20, 24))                                               # collar
    pod(t); sword(t); spear(t, (40, 40, 44)); rifle(t)
    return t

def _betrayer(hair, band, seed):
    t = ally_yorha_soldier(hair, band, seed)
    for u, v, w, h, d in ((16, 16, 8, 12, 4), (0, 64, 9, 7, 5)):                     # black plate armour, shoulder + thigh guards
        band_(t, u, v, w, h, d)
    return t
def band_(t, u, v, w, h, d):
    band(t, u, v, w, h, d, 1, 5, (52, 54, 62)); band(t, u, v, w, h, d, 5, 6, (150, 130, 80))

def ally_yorha_8b(): return _betrayer((232, 232, 240), (10, 10, 12), 31)
def ally_yorha_22b(): return _betrayer((196, 96, 106), (10, 10, 12), 32)
def ally_yorha_64b(): return _betrayer((122, 88, 66), (60, 220, 240), 33)

def _hum(seed, skin, suit, legs, boots, hair, eye, gloves, trim, long_=False, blind=None):
    t = Tex(128, 128, seed)
    humanoid(t, skin, suit, legs, boots, hair, eye, gloves=gloves, trim=trim)
    t.box(48, 16, 9, 4, 9, hair); t.box(48, 0, 9, 3, 2, hair)
    if long_: t.box(84, 16, 9, 12, 1, hair)
    if blind: t.box(100, 16, 8, 2, 1, blind)
    t.box(0, 64, 9, 7, 5, suit)
    pod_dummy = None
    sword(t); great_sword(t)
    return t

def ally_nier_brother():
    t = _hum(41, (240, 214, 190), (236, 236, 240), (196, 198, 206), (28, 28, 34), (232, 232, 238), (90, 110, 150), (46, 46, 54), (60, 60, 70))
    band(t, 16, 16, 8, 12, 4, 3, 8, (44, 46, 54)); return t
def ally_nier_father():
    t = _hum(42, (226, 196, 170), (226, 196, 170), (40, 44, 58), (30, 32, 44), (236, 236, 240), (90, 110, 150), (60, 62, 74), (230, 230, 236), long_=True)
    band(t, 16, 16, 8, 12, 4, 8, 11, (230, 230, 236)); band(t, 32, 16, 8, 12, 4, 0, 12, (226, 196, 170)); return t
def ally_kaine():
    t = _hum(43, (240, 214, 196), (50, 62, 98), (240, 240, 244), (30, 30, 40), (236, 236, 242), (200, 70, 80), (44, 50, 80), (60, 76, 120), long_=True)
    band(t, 24, 32, 3, 12, 3, 2, 12, (240, 240, 244)); band(t, 36, 32, 3, 12, 3, 4, 12, (240, 240, 244)); return t
def _shadow(seed, skin):
    t = _hum(seed, skin, (38, 42, 40), (34, 36, 40), (24, 24, 28), (52, 60, 52), (130, 150, 150), (30, 30, 34), (70, 80, 70))
    band(t, 16, 16, 8, 12, 4, 0, 3, (60, 70, 60)); return t
def ally_shadowlord_brother(): return _shadow(45, (236, 214, 196))
def ally_shadowlord_father(): return _shadow(46, (214, 188, 166))

def ally_king_of_facade():
    t = _hum(47, (190, 156, 112), (30, 28, 34), (30, 28, 34), (44, 38, 30), (24, 22, 26), (110, 124, 140), (30, 28, 34), (214, 176, 80), long_=True)
    band(t, 16, 16, 8, 12, 4, 0, 2, (214, 176, 80)); band(t, 16, 16, 8, 12, 4, 10, 11, (214, 176, 80)); t.box(100, 16, 4, 6, 1, (214, 176, 80))
    return t

def ally_fyra():
    t = _hum(61, (200, 170, 130), (240, 238, 232), (240, 238, 232), (210, 190, 150), (30, 28, 34), (110, 124, 140), (240, 238, 232), (214, 176, 80), long_=True)
    band(t, 16, 16, 8, 12, 4, 0, 1, (214, 176, 80)); band(t, 16, 16, 8, 12, 4, 10, 11, (214, 176, 80)); return t
def ally_yonah():
    return _hum(62, (244, 220, 200), (236, 230, 220), (200, 200, 210), (120, 90, 70), (96, 66, 50), (80, 70, 60), (244, 220, 200), (200, 190, 170), long_=True)
def ally_tyrann():
    t = _hum(63, (232, 208, 196), (28, 24, 40), (240, 240, 244), (24, 20, 36), (30, 26, 44), (230, 60, 90), (24, 20, 40), (120, 70, 190), long_=True)
    band(t, 24, 32, 3, 12, 3, 2, 12, (230, 230, 236)); return t
def ally_halua():
    return _hum(64, (240, 216, 196), (150, 190, 170), (150, 190, 170), (210, 190, 170), (130, 100, 76), (90, 110, 100), (240, 216, 196), (190, 220, 200))

def ally_emil_replicant():
    return _hum(44, (240, 214, 190), (110, 70, 150), (90, 56, 124), (60, 40, 80), (120, 84, 60), (60, 40, 60), (230, 230, 236), (210, 170, 90), blind=(14, 12, 18))

def _book(seed, cover, trim, plate):
    t = Tex(128, 128, seed)
    t.box(0, 0, 10, 14, 3, cover, noise=6)
    for f in ('front',): t.face(0, 0, 10, 14, 3, f, 0, 0, 10, 1, trim); t.face(0, 0, 10, 14, 3, f, 0, 13, 10, 1, trim); t.face(0, 0, 10, 14, 3, f, 0, 0, 1, 14, trim); t.face(0, 0, 10, 14, 3, f, 9, 0, 1, 14, trim)
    t.box(0, 24, 2, 2, 2, trim); t.box(16, 24, 1, 12, 3, (226, 220, 200))
    t.box(36, 24, 6, 6, 1, plate)
    t.face(36, 24, 6, 6, 1, 'front', 1, 1, 1, 2, (20, 20, 24)); t.face(36, 24, 6, 6, 1, 'front', 4, 1, 1, 2, (20, 20, 24)); t.face(36, 24, 6, 6, 1, 'front', 2, 4, 2, 1, (20, 20, 24))
    return t
def ally_grimoire_weiss(): return _book(51, (176, 180, 188), (230, 232, 238), (214, 218, 226))
def ally_grimoire_noir(): return _book(52, (70, 96, 100), (150, 176, 176), (160, 190, 190))
def ally_grimoire_rubrum(): return _book(53, (150, 100, 56), (230, 190, 110), (214, 170, 90))

def ally_yorha_heavy():
    t = ally_yorha_soldier()
    for u, v, w, h, d in ((16, 16, 8, 12, 4), (0, 64, 9, 7, 5)):                     # grey armour plates over the coat
        band(t, u, v, w, h, d, 2, 6, (96, 98, 108)); band(t, u, v, w, h, d, 6, 7, (200, 40, 40))
    t.box(48, 16, 9, 4, 9, (60, 60, 70))                                              # helmet
    return t

def ally_yorha_2e():
    t = Tex(128, 128, 12)
    humanoid(t, PALE, BLACK, BLACK, (12, 12, 14), (40, 40, 46), (180, 40, 40), gloves=(18, 18, 20), trim=(120, 30, 30))
    t.box(48, 16, 9, 5, 9, (232, 232, 240)); t.box(48, 0, 9, 4, 3, (232, 232, 240))          # white hair
    t.box(84, 16, 8, 2, 1, (10, 10, 12)); t.face(84, 16, 8, 2, 1, 'front', 0, 0, 8, 1, (200, 30, 30))  # red visor slit
    t.box(0, 64, 9, 6, 5, (30, 30, 36)); band(t, 0, 64, 9, 6, 5, 5, 6, (150, 30, 30))
    t.box(28, 64, 8, 9, 1, (26, 26, 30)); t.face(28, 64, 8, 9, 1, 'back', 0, 8, 8, 1, (150, 30, 30))
    pod(t, (190, 176, 140)); sword(t, (40, 40, 46), l=13); great_sword(t)
    return t

def ally_commander():
    t = Tex(128, 128, 13)
    white, silver, blonde, black = (240, 238, 234), (200, 200, 208), (236, 218, 168), (24, 24, 28)
    humanoid(t, PALE, white, white, (236, 232, 228), blonde, (110, 140, 190), gloves=(245, 245, 245), trim=silver)
    t.box(48, 16, 9, 2, 9, black); t.box(48, 0, 8, 1, 2, black)                         # black headband / bow
    t.box(84, 16, 8, 9, 2, blonde)                                                     # long blonde hair at back
    band(t, 16, 16, 8, 12, 4, 0, 2, black)                                             # black high collar
    t.face(16, 16, 8, 12, 4, 'front', 3, 3, 2, 1, black); t.face(16, 16, 8, 12, 4, 'front', 3, 3, 1, 3, black); t.face(16, 16, 8, 12, 4, 'front', 4, 5, 1, 1, black)   # chest cross
    t.box(0, 64, 9, 11, 5, white); band(t, 0, 64, 9, 11, 5, 10, 11, silver)             # long white dress
    t.face(0, 64, 9, 11, 5, 'front', 4, 0, 1, 11, silver)
    t.box(32, 64, 3, 1, 4, silver)
    sword(t, (220, 220, 230), (240, 240, 240), silver)
    return t

def operator(t, hair, shade):
    black, leather, gold = (22, 20, 22), (52, 40, 38), (206, 168, 80)
    humanoid(t, PALE, leather, black, (18, 16, 18), hair, (150, 70, 70), gloves=black, trim=gold)
    t.box(48, 16, 9, 9, 9, hair)                                                      # hair box + mask veil
    t.face(48, 16, 9, 9, 9, 'front', 0, 0, 9, 9, (0, 0, 0, 0))
    t.face(48, 16, 9, 9, 9, 'front', 0, 0, 9, 1, black)                               # black hairband
    t.face(48, 16, 9, 9, 9, 'front', 0, 5, 9, 4, black)                               # face mask / high collar
    for f in ('left', 'right', 'back'): t.face(48, 16, 9, 9, 9, f, 0, 5, 9, 4, black); t.face(48, 16, 9, 9, 9, f, 0, 0, 9, 1, black)
    t.box(48, 0, 1, 3, 2, (60, 60, 66)); t.box(54, 0, 1, 1, 3, (60, 60, 66))           # headset
    t.box(0, 64, 9, 11, 6, leather); t.face(0, 64, 9, 11, 6, 'front', 3, 2, 3, 3, gold); t.face(0, 64, 9, 11, 6, 'front', 4, 3, 1, 1, (40, 70, 120))
    band(t, 0, 64, 9, 11, 6, 9, 10, black)
    pod(t, (70, 72, 80), (40, 42, 50))
    return t

def ally_operator_6o(): return operator(Tex(128, 128, 14), (238, 230, 202), 0)
def ally_operator_21o(): return operator(Tex(128, 128, 15), (218, 212, 192), 0)

def ally_resistance_soldier():
    t = Tex(128, 128, 16)
    orange, dark = (190, 118, 54), (110, 62, 34)
    humanoid(t, (236, 208, 184), orange, (112, 110, 118), (60, 44, 30), (26, 24, 32), (60, 170, 170), gloves=(70, 56, 40))
    t.box(48, 16, 10, 8, 10, orange, noise=6)                                          # hooded cloak
    t.face(48, 16, 10, 8, 10, 'front', 1, 2, 8, 6, (0, 0, 0, 0))                       # open face
    t.face(48, 16, 10, 8, 10, 'front', 0, 0, 10, 2, dark)
    t.box(48, 0, 2, 5, 1, orange)
    t.box(0, 64, 9, 13, 5, orange, noise=6); band(t, 0, 64, 9, 13, 5, 10, 11, dark)    # cloak/jacket
    t.rect(0, 64 + 5 + 4, 28, 3, (150, 40, 34))                                        # blood stain
    t.box(28, 64, 9, 2, 6, (226, 214, 190))                                            # scarf
    t.box(100, 16, 3, 3, 2, (70, 56, 40))
    spear(t); rifle(t, (70, 56, 40), (90, 90, 96))
    return t

def ally_anemone():
    t = Tex(128, 128, 17)
    cream, olive, navy, gold, khaki = (234, 228, 208), (96, 104, 60), (58, 62, 100), (206, 168, 80), (120, 114, 98)
    humanoid(t, DARKSKIN, cream, khaki, (24, 24, 26), (30, 32, 52), (70, 110, 150), gloves=(24, 24, 26), trim=gold)
    for u in (24, 36): band(t, u, 32, 3, 12, 3, 4, 6, (20, 20, 22))                   # knee pads
    t.box(48, 16, 9, 4, 9, olive); band(t, 48, 16, 9, 4, 9, 3, 4, gold)                 # olive head wrap + gold band
    t.box(48, 0, 3, 6, 2, olive)
    t.box(0, 64, 9, 10, 5, cream)                                                       # tunic
    t.face(0, 64, 9, 10, 5, 'back', 0, 0, 9, 10, navy); t.face(0, 64, 9, 10, 5, 'left', 0, 0, 5, 10, navy); t.face(0, 64, 9, 10, 5, 'right', 0, 0, 5, 10, navy)   # navy cloak
    band(t, 0, 64, 9, 10, 5, 5, 7, (20, 20, 22)); t.face(0, 64, 9, 10, 5, 'front', 3, 5, 3, 2, gold)   # belt + buckle
    t.box(32, 64, 10, 4, 6, olive); band(t, 32, 64, 10, 4, 6, 3, 4, gold)               # olive mantle
    sword(t, (190, 190, 196), (60, 40, 30), (90, 90, 96)); rifle(t, (150, 110, 60), (60, 60, 64))
    return t

def ally_jackass():
    t = Tex(128, 128, 18)
    tan, navy, scarf, glove = (156, 126, 78), (30, 34, 62), (52, 104, 104), (80, 52, 36)
    humanoid(t, (240, 214, 190), tan, (84, 80, 76), (60, 44, 34), navy, (130, 150, 170), gloves=glove)
    t.box(48, 16, 9, 5, 9, navy)                                                       # short navy hair
    t.face(48, 16, 9, 5, 9, 'front', 0, 3, 4, 2, navy)                                  # long fringe over one eye
    t.box(84, 16, 9, 2, 1, navy); t.face(84, 16, 9, 2, 1, 'front', 0, 0, 4, 2, navy)
    t.box(0, 64, 9, 9, 5, tan); band(t, 0, 64, 9, 9, 5, 0, 3, scarf)                    # tan coat + teal scarf
    t.face(0, 64, 9, 9, 5, 'front', 3, 3, 3, 6, scarf)
    t.box(0, 80, 3, 3, 16, (84, 90, 70)); band(t, 0, 80, 3, 3, 16, 0, 1, (200, 160, 60))
    t.face(0, 80, 3, 3, 16, 'front', 0, 0, 3, 3, (20, 20, 20))                        # launcher tube + bore
    return t

def ally_pascal():
    t = Tex(128, 128, 19)
    plate, dark, lens, joint = (146, 100, 68), (84, 58, 42), (70, 190, 255), (84, 100, 116)
    t.box(0, 0, 10, 6, 10, plate); t.box(40, 0, 8, 2, 8, dark)                         # cylindrical head + cap
    t.box(72, 0, 3, 3, 1, lens); t.face(72, 0, 3, 3, 1, 'front', 1, 1, 1, 1, (230, 250, 255))   # blue eyes
    t.face(0, 0, 10, 6, 10, 'front', 3, 4, 4, 1, dark)                                 # mouth grille
    t.box(0, 16, 10, 11, 8, plate); band(t, 0, 16, 10, 11, 8, 5, 6, dark)
    t.face(0, 16, 10, 11, 8, 'front', 4, 3, 3, 3, (214, 176, 84)); t.face(0, 16, 10, 11, 8, 'front', 5, 4, 1, 1, (40, 30, 24))   # chest gauge
    t.box(36, 16, 3, 9, 3, joint); t.box(48, 16, 4, 3, 4, plate)
    t.box(36, 32, 3, 9, 3, joint); t.box(48, 32, 4, 2, 5, plate)
    t.box(0, 64, 12, 6, 10, dark)                                                      # back exhaust / shoulder plating
    for x in range(0, 44, 4): t.rect(x, 64 + 10 + 3, 2, 2, (200, 160, 80))
    return t

def twin(t, trim, hair):
    white, black, redp = (240, 238, 234), (24, 22, 26), (150, 28, 36)
    humanoid(t, PALE, white, redp, (96, 66, 44), hair, (150, 200, 150), gloves=black, trim=black)
    t.box(48, 16, 9, 8, 9, hair, noise=6); t.face(48, 16, 9, 8, 9, 'front', 1, 3, 7, 5, (0, 0, 0, 0))   # red hair, open face
    t.face(48, 16, 9, 8, 9, 'front', 0, 0, 9, 3, hair)
    t.box(48, 0, 9, 6, 3, hair, noise=6)                                               # long hair behind
    t.box(0, 64, 9, 13, 6, white); band(t, 0, 64, 9, 13, 6, 0, 1, black)               # white long open coat
    t.face(0, 64, 9, 13, 6, 'front', 3, 0, 3, 13, redp)
    t.box(32, 64, 10, 6, 6, black); band(t, 32, 64, 10, 6, 6, 5, 6, (60, 60, 66))      # black puffed collar/shoulders
    for i in range(12):
        t.rect(12 + i, 84, 1, 1, trim); t.rect(12 + i, 95, 1, 1, trim); t.rect(12, 84 + i, 1, 1, trim); t.rect(23, 84 + i, 1, 1, trim)
    return t

def ally_devola(): return twin(Tex(128, 128, 20), (214, 40, 50), (186, 28, 34))
def ally_popola(): return twin(Tex(128, 128, 21), (214, 40, 50), (176, 24, 30))

def emil_head(t, skin=(204, 200, 188)):
    t.box(0, 0, 12, 12, 12, skin, noise=5)
    dark, teeth = (48, 44, 40), (240, 238, 228)
    for ex in (1, 7):                                                   # concentric ringed eyes
        t.face(0, 0, 12, 12, 12, 'front', ex, 2, 4, 4, dark); t.face(0, 0, 12, 12, 12, 'front', ex + 1, 3, 2, 2, (226, 226, 218)); t.face(0, 0, 12, 12, 12, 'front', ex + 1, 3, 1, 1, (120, 130, 140))
    t.face(0, 0, 12, 12, 12, 'front', 1, 7, 10, 3, dark)                # wide toothy grin
    for x in range(2, 10, 2): t.face(0, 0, 12, 12, 12, 'front', x, 7, 1, 1, teeth); t.face(0, 0, 12, 12, 12, 'front', x, 9, 1, 1, teeth)
    t.face(0, 0, 12, 12, 12, 'front', 1, 8, 10, 1, teeth)
    t.box(48, 0, 3, 1, 1, dark); t.box(64, 0, 4, 4, 4, skin)
    t.face(64, 0, 4, 4, 4, 'front', 0, 1, 4, 1, dark)

def ally_emil():
    t = Tex(128, 128, 22)
    emil_head(t)
    teal, red, strap = (70, 112, 122), (176, 46, 50), (44, 40, 40)
    t.box(0, 24, 7, 7, 5, teal); t.box(24, 24, 9, 4, 6, red)
    t.box(0, 40, 2, 7, 2, teal); t.box(8, 40, 2, 7, 2, teal); t.box(16, 40, 2, 6, 2, strap)
    t.box(0, 64, 10, 3, 14, teal, noise=6); t.face(0, 64, 10, 3, 14, 'front', 3, 0, 4, 3, (240, 200, 70))   # vehicle body + headlight
    t.box(48, 64, 10, 6, 2, red, noise=6); t.face(48, 64, 10, 6, 2, 'front', 4, 0, 2, 6, strap)           # red bundle + strap
    t.box(72, 64, 12, 1, 1, strap)
    t.box(0, 84, 1, 4, 4, strap); t.box(12, 84, 12, 1, 1, (90, 90, 96))
    return t

def ally_emil_hacked():
    t = Tex(128, 128, 23)
    emil_head(t, (220, 210, 200))
    frame, seam = (36, 34, 40), (200, 30, 30)
    t.box(0, 24, 16, 16, 10, frame); t.face(0, 24, 16, 16, 10, 'front', 7, 0, 2, 16, seam); band(t, 0, 24, 16, 16, 10, 8, 9, seam)
    t.box(52, 24, 12, 5, 8, (60, 56, 62))
    t.box(0, 52, 5, 14, 6, frame); band(t, 0, 52, 5, 14, 6, 6, 7, seam)
    t.box(24, 52, 7, 7, 8, (70, 66, 74)); t.face(24, 52, 7, 7, 8, 'front', 1, 1, 5, 1, seam)
    t.box(56, 52, 6, 16, 6, frame); band(t, 56, 52, 6, 16, 6, 7, 8, seam)
    return t

def pod_unit(body, dark, eye):
    t = Tex(128, 128, 24)
    t.box(0, 0, 8, 9, 12, body); band(t, 0, 0, 8, 9, 12, 6, 7, dark); t.box(40, 0, 6, 1, 8, dark)
    t.box(0, 24, 2, 2, 3, dark); t.box(16, 24, 1, 7, 8, body); t.box(36, 24, 4, 1, 1, eye)
    return t

def ally_pod(): return pod_unit((70, 72, 80), (36, 38, 44), (255, 150, 60))
def ally_pod_153(): return pod_unit((70, 72, 80), (36, 38, 44), (120, 200, 255))

def build(name):
    globals()[name]().save(os.path.join(OUT, name + '.png'))

ALL = ['ally_fyra', 'ally_yonah', 'ally_tyrann', 'ally_halua', 'ally_king_of_facade', 'ally_shadowlord_brother', 'ally_shadowlord_father', 'ally_grimoire_weiss', 'ally_grimoire_noir', 'ally_grimoire_rubrum', 'ally_nier_brother', 'ally_nier_father', 'ally_kaine', 'ally_emil_replicant', 'ally_yorha_soldier', 'ally_yorha_8b', 'ally_yorha_22b', 'ally_yorha_64b', 'ally_yorha_heavy', 'ally_yorha_2e', 'ally_commander', 'ally_operator_6o', 'ally_operator_21o', 'ally_resistance_soldier',
       'ally_anemone', 'ally_jackass', 'ally_pascal', 'ally_devola', 'ally_popola', 'ally_emil', 'ally_emil_hacked', 'ally_pod', 'ally_pod_153']
if __name__ == '__main__':
    os.makedirs(OUT, exist_ok=True)
    for n in ALL: build(n)
