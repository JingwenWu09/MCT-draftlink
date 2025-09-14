#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int cond;
int abs(int);

static __attribute__((noinline, noclone)) int foo(int i) {
  if (i >= 10) {
    __builtin_abort();
  }
  return 0;
}

static __attribute__((noinline, noclone)) int bar(int j) {
  foo(~j);
  foo(abs(j));
  foo(j);
  return 0;
}

int main() {
  for (unsigned int i = 0; i < 10; ++i) {
    bar(i);
  }

  return 0;
}
