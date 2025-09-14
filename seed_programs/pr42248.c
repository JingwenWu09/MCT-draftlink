#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct Scf10 {
  _Complex double a;
  _Complex double b;
};

struct Scf10 g1s;

void check(struct Scf10 x, _Complex double y) {
  if (x.a != y) {
    __builtin_abort();
  }
}

void init(struct Scf10 *p, _Complex double y) {
  p->a = y;
}

int main() {
  init(&g1s, (_Complex double)1);
  check(g1s, (_Complex double)1);

  return 0;
}
