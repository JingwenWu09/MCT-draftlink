#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __int128 V __attribute__((__vector_size__(16 * sizeof(__int128))));

__int128 __attribute__((noipa)) foo(void) {
  V v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
  return v[5];
}

int main() {
  if (foo() != 6) {
    __builtin_abort();
  }
  return 0;
}
