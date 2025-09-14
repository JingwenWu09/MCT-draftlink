#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static void __attribute__((noinline)) transpose_vector(unsigned long n) {
  unsigned long data[2 * n];
  for (unsigned long i = 0; i < 2 * n; i++) {
    data[i] = 4 * i + 2;
  }

  unsigned long transposed[n];
  for (unsigned long i = 0; i < n; i++) {
    transposed[i] = data[2 * i];
  }

  for (unsigned long i = 0; i < n; i++) {
    if (transposed[i] != 8 * i + 2) {
      __builtin_abort();
    }
  }
}

int main() {
  transpose_vector(4);
  transpose_vector(120);
  return 0;
}
