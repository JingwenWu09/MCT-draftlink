#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define MAX 100
int A[MAX];

extern void abort();

int main(void) {
  int i;

  for (i = 0; i < MAX; i++) {
    A[i] = i;
  }
  for (int i = 0; i < MAX; i++) {
    A[i] ^= 4;
  }
  for (int i = 0; i < MAX; i++) {
    A[i] ^= 8;
  }

  for (i = 0; i < MAX; i++) {
    if (A[i] != (i ^ 12)) {
      abort();
    }
  }

  return 0;
}
