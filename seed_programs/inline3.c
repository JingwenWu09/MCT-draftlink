#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  const long i;
  const long j;
};

static inline long foo(void) {
  const struct A baz = {.i = 2, .j = 21};

  const struct A *p = &baz;
  asm volatile("" : : : "memory");
  return baz.i * baz.j;
}

int main() {
  return foo() - 42;
}
