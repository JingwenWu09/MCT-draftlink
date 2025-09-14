#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct A {
  int a, b, c, d, e, f;
};

void foo(struct A *v, int w, int x, int *y, int *z) {
}

int main() {
  struct A ww = {100, 110, 20, 30, -1, -1};

  struct A *v = &ww;
  int x = 400;
  int y = 420;
  int w = 50;
  int h = 70;
  if (v->a != x || v->b != y) {
    int oldw = w;
    int oldh = h;
    int e = v->e;
    int f = v->f;
    int dx, dy;
    foo(v, 0, 0, &w, &h);
    dx = (oldw - w) * (double)e / 2.0;
    dy = (oldh - h) * (double)f / 2.0;
    x += dx;
    y += dy;
    v->a = x;
    v->b = y;
    v->c = w;
    v->d = h;
  }

  if (ww.d != 70) {
    abort();
  }
  exit(0);
}
