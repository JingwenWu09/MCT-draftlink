#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

struct S {
  char a;
  long long b;
};

struct S foo(struct S x, struct S y) {
  struct S z;
  z.b = x.b / y.b;
  return z;
}

int main(void) {
  struct S a, b;
  a.b = 32LL;
  b.b = 4LL;
  if (foo(a, b).b != 8LL) {
    abort();
  }
  a.b = -8LL;
  b.b = -2LL;
  if (foo(a, b).b != 4LL) {
    abort();
  }
  return 0;
}
