#include <limits.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

long long smod16(long long x) {
  return x % 16;
}

int main(void) {
#if LLONG_MAX > 2147483647L
  if (smod16(0xFFFFFFFF) != 0xF) {
    abort();
  }
#endif

  return 0;
}
