#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#define N 16

int main1(int *a) {
  int i, j, k;
  int b[N];

  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
      k = i + N;
      a[j] = k;
    }
    b[i] = k;
  }

  for (j = 0; j < N; j++) {
    if (a[j] != i + N - 1) {
      abort();
    }
  }

  for (j = 0; j < N; j++) {
    if (b[j] != j + N) {
      abort();
    }
  }

  return 0;
}

int main(void) {
  int a[N] __attribute__((__aligned__(16)));

  main1(a);

  return 0;
}
