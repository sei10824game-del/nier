"""Texture painter for vanilla-style box UVs (CubeListBuilder.texOffs(u,v).addBox(w,h,d)).
Usage:
  from boxtex import Tex
  t = Tex(64, 64)
  t.box(u, v, w, h, d, base=(r,g,b), top=None, bottom=None, front=None, shade=0.12, noise=6, mirror=False)
  t.rect(x, y, w, h, color)      # raw pixels, for details (eyes, stripes, glow)
  t.face(u, v, w, h, d, 'front', x, y, w2, h2, color)  # paint a detail on a box face (x,y in face pixels)
  t.save('path.png')
Layout of a box at (u,v): top = (u+d, v, w, d); bottom = (u+d+w, v, w, d);
right(-x side)=(u, v+d, d, h); front=(u+d, v+d, w, h); left=(u+d+w, v+d, d, h); back=(u+2d+w, v+d, w, h).
"""
from PIL import Image
import random

def _c(c, f):
    return tuple(max(0, min(255, int(x * f))) for x in c[:3]) + ((c[3],) if len(c) > 3 else (255,))

class Tex:
    def __init__(self, w, h, seed=1):
        self.img = Image.new('RGBA', (w, h), (0, 0, 0, 0)); self.px = self.img.load(); self.rnd = random.Random(seed)
    def rect(self, x, y, w, h, color, noise=0):
        for i in range(x, x + w):
            for j in range(y, y + h):
                if 0 <= i < self.img.width and 0 <= j < self.img.height:
                    f = 1 + (self.rnd.uniform(-noise, noise) / 100 if noise else 0)
                    self.px[i, j] = _c(color if len(color) > 3 else color + (255,), f)
    def _bevel(self, x, y, w, h):
        """Edge/gradient shading on one face: darker rim, light top row, darker bottom row (cheap ambient occlusion)."""
        if w < 3 or h < 3: return
        for i in range(x, x + w):
            for j in range(y, y + h):
                if not (0 <= i < self.img.width and 0 <= j < self.img.height): continue
                r, g, b, a = self.px[i, j]
                if a == 0: continue
                f = 1.0
                if j == y: f *= 1.07
                if j == y + h - 1: f *= 0.86
                if i in (x, x + w - 1): f *= 0.9
                self.px[i, j] = _c((r, g, b, a), f)
    def _detail(self, x, y, w, h):
        """Pixel-level surface detail on one face: corner rivets, panel seam, scratches, grime streaks."""
        if w < 6 or h < 6: return
        W, H = self.img.width, self.img.height
        def put(i, j, f):
            if 0 <= i < W and 0 <= j < H and self.px[i, j][3]: self.px[i, j] = _c(self.px[i, j], f)
        if w >= 8 and h >= 8:
            for (i, j) in ((x + 1, y + 1), (x + w - 2, y + 1), (x + 1, y + h - 2), (x + w - 2, y + h - 2)):
                put(i, j, 1.25); put(i + 1, j + 1, 0.78)                      # rivet: highlight + shadow
        if w >= 10:
            for j in range(y + 1, y + h - 1): put(x + w // 2, j, 0.88)        # panel seam
            if h >= 10:
                for i in range(x + 1, x + w - 1): put(i, y + h // 2, 0.9)
        for _ in range(max(1, (w * h) // 40)):                                  # scratches
            i = x + self.rnd.randrange(w); j = y + self.rnd.randrange(h); n = self.rnd.randrange(2, 5)
            for k in range(n): put(i + k, j + k // 2, 1.18)
        for _ in range(max(1, w // 5)):                                         # grime streaks from the top edge
            i = x + self.rnd.randrange(w); n = self.rnd.randrange(2, max(3, h // 2))
            for k in range(n): put(i, y + k, 0.84 + 0.01 * k)
    def box(self, u, v, w, h, d, base, top=None, bottom=None, front=None, back=None, side=None, shade=0.12, noise=5):
        top = top or _c(base, 1 + shade); bottom = bottom or _c(base, 1 - shade * 2)
        front = front or base; back = back or _c(base, 1 - shade * .5); side = side or _c(base, 1 - shade)
        self.rect(u + d, v, w, d, top, noise); self.rect(u + d + w, v, w, d, bottom, noise)
        self.rect(u, v + d, d, h, side, noise); self.rect(u + d, v + d, w, h, front, noise)
        self.rect(u + d + w, v + d, d, h, side, noise); self.rect(u + 2 * d + w, v + d, w, h, back, noise)
        for fx, fy, fw, fh in ((u, v + d, d, h), (u + d, v + d, w, h), (u + d + w, v + d, d, h), (u + 2 * d + w, v + d, w, h)): (self._bevel(fx, fy, fw, fh), self._detail(fx, fy, fw, fh))
    def face(self, u, v, w, h, d, which, x, y, w2, h2, color):
        ox, oy = {'top': (u + d, v), 'bottom': (u + d + w, v), 'right': (u, v + d), 'front': (u + d, v + d),
                  'left': (u + d + w, v + d), 'back': (u + 2 * d + w, v + d)}[which]
        self.rect(ox + x, oy + y, w2, h2, color)
    def save(self, path): self.img.save(path)
