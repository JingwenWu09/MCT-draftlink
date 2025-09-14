#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define DEBUG 0
#if DEBUG
#include <stdio.h>
#endif

#define N 200

unsigned int A[N][N], B[N][N], C[N][N];

static unsigned int __attribute__((noinline)) matmult(void) {
  int i, j, k;

  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
      A[i][j] = 0;
      for (k = 0; k < N; k++) {
        A[i][j] += B[i][k] * C[k][j];
      }
    }
  }

  return A[0][0] + A[N - 1][N - 1];
}

extern void abort();

int main(void) {
  int i, j;
  unsigned int res;

  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
      A[i][j] = 0;
      B[i][j] = i - j;
      C[i][j] = i + j;
    }
  }

  res = matmult();

#if DEBUG
  fprintf(stderr, "res = %d \n", res);
#endif

  if (res != 2626800) {
    abort();
  }

  return 0;
}
