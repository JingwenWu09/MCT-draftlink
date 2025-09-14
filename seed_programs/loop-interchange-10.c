#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef int int32_t __attribute__((mode(__SI__)));

#define M 256
int a[M][M], b[M][M];
int32_t __attribute__((noinline)) double_reduc(int n) {
  int32_t sum = 0;
  for (int j = 0; j < n; j++) {
    for (int i = 0; i < n; i++) {
      sum = sum + (int32_t)a[i][j] * (int32_t)b[i][j];
    }
  }
  return sum;
}

extern void abort();

static void __attribute__((noinline)) init(int i) {
  for (int j = 0; j < M; j++) {
    a[i][j] = i;
    b[i][j] = j;
  }
}

int main(void) {
  for (int i = 0; i < M; ++i) {
    init(i);
  }

  int32_t sum = double_reduc(M);

  if (sum != 1065369600) {
    abort();
  }

  return 0;
}
