#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#define N 16

int main1(int n) {
  int i, j, k;
  int a[N], b[N];

  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
      k = i + n;
      a[j] = k;
    }
    b[i] = k;
  }

  for (j = 0; j < n; j++) {
    if (a[j] != i + n - 1) {
      abort();
    }
  }

  for (i = 0; i < n; i++) {
    if (b[i] != i + n) {
      abort();
    }
  }

  return 0;
}

int main(void) {
  main1(N);
  main1(0);
  main1(1);
  main1(2);
  main1(N - 1);

  return 0;
}
