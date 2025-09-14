#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

__attribute__((noinline)) void f(int *p, int **q) {
  int i;
  for (i = 0; i < 40; i++) {
    *q++ = &p[i];
  }
}

int main() {
  void *p;
  int *q[40];
  __SIZE_TYPE__ start;

  if (sizeof(start) == sizeof(int)) {
    start = (__SIZE_TYPE__)__INT_MAX__;
  } else if (sizeof(start) == sizeof(long)) {
    start = (__SIZE_TYPE__)__LONG_MAX__;
  } else if (sizeof(start) == sizeof(long long)) {
    start = (__SIZE_TYPE__)__LONG_LONG_MAX__;
  } else {
    return 0;
  }

  start &= -32;

  p = (void *)start;

  q[39] = 0;
  f(p, q);
  if (q[39] != (int *)p + 39) {
    abort();
  }

  return 0;
}
