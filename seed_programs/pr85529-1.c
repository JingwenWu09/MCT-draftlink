#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int a;
};

int b, c = 1, d, e, f;
static int g;
volatile struct S s;

signed char foo(signed char i, int j) {
  return i < 0 ? i : i << j;
}

int main() {
  signed char k = -83;
  if (!d) {
    goto L;
  }
  k = e || f;
L:
  for (; b < 1; b++) {
    s.a |= (k < foo(k, 2) && (c = k = g));
  }
  if (c != 1) {
    __builtin_abort();
  }
  return 0;
}
