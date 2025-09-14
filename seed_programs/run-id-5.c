#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>

extern void abort();
#define N 40

int a[N];

__attribute__((noinline)) int foo(int n) {
  int i, j;
  int sum;

  if (n <= 0) {
    return 0;
  }

  for (i = 0; i < N; i++) {
    sum = 0;
    for (j = 0; j < n; j += 2) {
      sum += j;
    }
    a[i] = sum + j;
  }
}

int main(void) {
  int i, j;
  int sum;

  for (i = 0; i < N; i++) {
    a[i] = i;
  }

  foo(N);

  for (i = 0; i < N; i++) {
    sum = 0;
    for (j = 0; j < N; j += 2) {
      sum += j;
    }
    if (a[i] != sum + j) {
      abort();
    }
  }

  return 0;
}
