#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

_Complex double v = 3.0 + 1.0iF;

void foo(_Complex double z, int *x) {
  if (z != v) {
    abort();
  }
}

_Complex double bar(_Complex double z) __attribute__((pure));
_Complex double bar(_Complex double z) {
  return v;
}

int baz(void) {
  int a, i;
  for (i = 0; i < 6; i++) {
    foo(bar(1.0iF * i), &a);
  }
  return 0;
}

int main() {
  baz();
  return 0;
}
