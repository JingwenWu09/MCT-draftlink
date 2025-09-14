#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct T{
  int a, b, c, d, e;
};

int h(struct T *);
struct T g(struct T);

#if COMPILER != 2
struct T g(struct T x) {
  if (x.a != 13 || x.b != 47 || x.c != 123456 || x.d != -4711 || x.e != -2) {
    abort();
  }
  x.a = 0;
  x.b = 1;
  x.c = 2;
  x.d = 3;
  x.e = 4;
  struct T *xp = &x;
  if (xp->a != 0 || xp->b != 1 || xp->c != 2 || xp->d != 3 || xp->e != 4) {
    abort();
  }
  return x;
}
#endif

#if COMPILER != 1
f() {
  struct T x;
  x.a = 13;
  x.b = 47;
  x.c = 123456;
  x.d = -4711;
  x.e = -2;
  g(x);
  if (x.a != 13 || x.b != 47 || x.c != 123456 || x.d != -4711 || x.e != -2) {
    abort();
  }
  x = g(x);
  if (x.a != 0 || x.b != 1 || x.c != 2 || x.d != 3 || x.e != 4) {
    abort();
  }
}
#endif

#if COMPILER != 2
main() {
  f();
  exit(0);
}
#endif
