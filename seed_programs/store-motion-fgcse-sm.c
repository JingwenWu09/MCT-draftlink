#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) f(unsigned int *__restrict__ a, unsigned int *__restrict__ sum, unsigned int n) {
  unsigned int i;
  for (i = 0; i < n; ++i) {
    *sum += a[i];
  }
}

int main() {
  unsigned int a[] = {1, 10, 100};
  unsigned sum = 1000;

  f(a, &sum, 3);
  if (sum != 1111) {
    abort();
  }

  return 0;
}
