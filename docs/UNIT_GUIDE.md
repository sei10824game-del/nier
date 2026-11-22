# Unit authoring guide (read before writing a unit)

Build: `export JAVA_HOME=/opt/jdk17; ./gradlew build --no-daemon --offline -q` (Forge 1.20.1, official mappings, Java 17).
Core API lives in `com.nier.automata.core` (server) and `com.nier.automata.client` (client). Do NOT edit core/client
files or `Modules.java`/`NierAutomata.java`; if you need a core change, make the smallest possible additive change and report it.

Per unit, create ONLY these files:
- `src/main/java/com/nier/automata/unit/<name>/…`   `<Name>Unit extends NierUnit`, `<Name>Module extends UnitModule<..>`,
  `<Name>Model extends NierModel<..>` (+ `<Name>Client` holding client-only registration), optional projectile entities.
- `src/main/resources/assets/nierautomata/textures/entity/<id>*.png` (generate with `tools/boxtex.py`, script kept in `tools/gen/<id>.py`)
- `src/main/resources/assets/nierautomata/lang/parts/<id>.json` (+ `<id>.ja.json`): `{"entity.nierautomata.<id>": "Name", "item.nierautomata.<id>_spawn_egg": "Name Spawn Egg"}`

Concepts
- `Skill(id,name,duration,cooldown,minRange,maxRange,tick)`: tick(self,target,t) runs every game tick, t=0..duration-1.
  Put damage frames / movement / particles / projectiles at exact t. Use `.weight() .noTurn() .when() .forms()`.
- Client animation: `AnimClip.of(duration).bone("rightArm").rot(t,x,y,z).pos(...)...done()`; register with `clip(skill.id, clip)`
  in the model ctor. Clip length == skill duration; keyframes line up with the server damage frames (windup -> strike -> recovery,
  anticipation + overshoot + follow-through, body twist, weight shift, head/torso lead). Never a single arm swing: move torso, legs, head, weapon.
- Bones are bound by path in the model ctor: `bind("body","body/head","body/rightArm",...)`, clip/idle address them by last segment.
- Forms: `setForm(n)`; `NierRenderer` swaps models[form] so give one model per form (or reuse). Use a `Skill` with `.noTarget()` +
  `forcedSkill()` for transformations (e.g. at HP threshold), with its own clip.
- Hostility is automatic (see NierUnit.isEnemy). Faction: MACHINE or ANDROID.
- Textures: vanilla box UV. Use tools/boxtex.py; give units real colour identity (black YoRHa suits, rusty/gray machine plating, red glowing eyes).
- Fidelity: research the original (wiki: nier.fandom.com) and implement EVERY attack/ability and every form/phase of the unit.
