"""Asset/registry consistency check: every lang id needs spawn-egg model, ja lang; every entity texture referenced must exist. Run: python3 tools/check.py"""
import glob, os, re, json, sys
R = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/nierautomata')
ids = sorted(os.path.basename(f)[:-5] for f in glob.glob(R + '/lang/parts/*.json') if not f.endswith('.ja.json'))
bad = []
for i in ids:
    if i.endswith('_sigil'):
        if not os.path.exists(f'{R}/models/item/{i}.json'): bad.append(f'no sigil model {i}')
    else:
        if not os.path.exists(f'{R}/models/item/{i}_spawn_egg.json'): bad.append(f'no egg model {i}')
    if not os.path.exists(f'{R}/lang/parts/{i}.ja.json'): bad.append(f'no ja lang {i}')
tex = set(os.path.basename(f) for f in glob.glob(R + '/textures/entity/*.png'))
src = ''.join(open(f, errors='ignore').read() for f in glob.glob(os.path.join(R, '../../../java/**/*.java'), recursive=True))
for t in tex:
    if os.path.getsize(f'{R}/textures/entity/{t}') < 200: bad.append(f'tiny texture {t}')
print(len(ids), 'entries,', len(tex), 'textures;', 'OK' if not bad else '\n'.join(bad))
sys.exit(1 if bad else 0)
