#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define MAX 100
int A[MAX], B[MAX], C[MAX];

extern void abort();

int main(void) {
  int i;

  for (i = 0; i < MAX; i++) {
    A[i] = i;
    B[i] = i + 2;
    C[i] = i + 1;
  }
  for (i = 0; i < MAX; i++) {
    A[i] += B[i];
  }
  for (i = 0; i < MAX; i++) {
    A[i] += C[i];
  }

  for (i = 0; i < MAX; i++) {
    if (A[i] != 3 * i + 3) {
      abort();
    }
  }

  return 0;
}
