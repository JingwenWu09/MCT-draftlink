#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int cond;

static __attribute__((noinline, noclone)) int foo(int i) {
  if (i < 5) {
    __builtin_abort();
  }
  return 0;
}

static __attribute__((noinline, noclone)) int bar(int j) {
  if (cond) {
    foo(j);
  }
  return 0;
}

int main() {
  for (unsigned int i = 0; i < 10; ++i) {
    bar(i);
  }

  return 0;
}
