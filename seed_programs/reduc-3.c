#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>
#include <stdlib.h>

#define N 1600

unsigned int ub[N];
unsigned int uc[N];

__attribute__((noinline)) int main1(int n, int res) {
  int i;
  unsigned int udiff;

  udiff = 0;
  for (i = 0; i < n; i++) {
    udiff += (ub[i] - uc[i]);
  }

  if (udiff != res) {
    abort();
  }

  return 0;
}

__attribute__((noinline)) void init_arrays() {
  int i;

  for (i = 0; i < N; i++) {
    ub[i] = i * 3;
    uc[i] = i;
  }
}

int main(void) {
  init_arrays();
  main1(N, 2558400);
  main1(N - 1, 2555202);
  return 0;
}
