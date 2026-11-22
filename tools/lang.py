"""Merge assets/nierautomata/lang/parts/*.json (en) and *.ja.json into en_us.json / ja_jp.json."""
import json, glob, os
d = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/nierautomata/lang')
en = {"itemGroup.nierautomata": "NieR:Automata Units"}; ja = {"itemGroup.nierautomata": "ニーア オートマタ ユニット"}
for f in sorted(glob.glob(d + '/parts/*.json')):
    (ja if f.endswith('.ja.json') else en).update(json.load(open(f)))
for n, m in (('en_us', en), ('ja_jp', ja)):
    json.dump(m, open(f'{d}/{n}.json', 'w'), ensure_ascii=False, indent=1, sort_keys=True)
