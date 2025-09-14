#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>
#include <stdlib.h>

#define N 1600
#define DIFF 2558402

unsigned int ub[N];
unsigned int uc[N];

void main1(unsigned int x, unsigned int max_result, unsigned int min_result) {
  int i;
  unsigned int udiff = 2;
  unsigned int umax = x;
  unsigned int umin = x;

  for (i = 0; i < N; i++) {
    udiff += (ub[i] - uc[i]);
  }

  for (i = 0; i < N; i++) {
    umax = umax < uc[i] ? uc[i] : umax;
  }

  for (i = 0; i < N; i++) {
    umin = umin > uc[i] ? uc[i] : umin;
  }

  if (udiff != DIFF) {
    abort();
  }
  if (umax != max_result) {
    abort();
  }
  if (umin != min_result) {
    abort();
  }
}

__attribute__((noinline)) void init_arrays() {
  int i;

  ub[0] = 1;
  uc[0] = 1;
  for (i = 1; i < N; i++) {
    ub[i] = i * 3;
    uc[i] = i;
  }
}

int main(void) {
  init_arrays();
  main1(2000, 2000, 1);
  main1(0, 1599, 0);
  return 0;
}
