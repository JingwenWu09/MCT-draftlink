#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct T {
  int a, b;
} t, q;

int main(void) {
  struct T *p;

  t.a = 1;
  t.b = 2;
  q = t;
  t.a = 3;

  if (q.a != 1) {
    __builtin_abort();
  }

  return 0;
}
