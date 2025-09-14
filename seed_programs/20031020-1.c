#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

extern void abort(void);

void __attribute__((noinline)) foo(long x) {
  if (x >= 1024) {
    abort();
  }
}

int main() {
  foo(LONG_MIN);
  foo(LONG_MIN + 10000);
  return 0;
}
