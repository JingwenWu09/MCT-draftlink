#include <math.h>
#include <stdio.h>
#include <stdlib.h>
union T{
  struct {
    int a;
    int b;
  } s;
  double d;
};

int h(union T *);
union T g(union T);

#if COMPILER != 2
union T g(union T x) {
  if (x.s.a != 13 || x.s.b != 47) {
    abort();
  }
  x.s.a = 0;
  x.s.b = 1;
  union T *xp = &x;
  if (xp->s.a != 0 || xp->s.b != 1) {
    abort();
  }
  return x;
}
#endif

#if COMPILER != 1
f() {
  union T x;
  x.s.a = 13;
  x.s.b = 47;
  g(x);
  if (x.s.a != 13 || x.s.b != 47) {
    abort();
  }
  x = g(x);
  if (x.s.a != 0 || x.s.b != 1) {
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
