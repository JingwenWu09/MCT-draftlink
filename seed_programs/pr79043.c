#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int val;

int *ptr = &val;
float *ptr2 = &val;

static __attribute__((always_inline, optimize("-fno-strict-aliasing"))) typepun() {
  *ptr2 = 0;
}

main() {
  *ptr = 1;
  typepun();
  if (*ptr) {
    __builtin_abort();
  }
}
