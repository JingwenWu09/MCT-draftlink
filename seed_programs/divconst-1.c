#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct t1 {
  unsigned a, b, c, d;
};

f(struct t1 *ps) {
  ps->a = 10000;
  ps->b = ps->a / 3;
  ps->c = 10000;
  ps->d = ps->c / 3;
}

main() {
  struct t1 s;
  f(&s);
  if (s.a != 10000 || s.b != 3333 || s.c != 10000 || s.d != 3333) {
    abort();
  }
  exit(0);
}
