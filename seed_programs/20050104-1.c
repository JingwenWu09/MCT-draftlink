#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

static long long min() {
  return -__LONG_LONG_MAX__ - 1;
}

void foo(long long j) {
  if (j > 10 || j < min()) {
    abort();
  }
}

int main(void) {
  foo(10);
  return 0;
}
