#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

#if __INT_MAX__ == 2147483647
typedef unsigned int uint32_t;
#elif __LONG_MAX__ == 2147483647
typedef unsigned long uint32_t;
#else
#error unable to find 32-bit integer type
#endif

#define rotl(x, n) (((x) << ((int)(n))) | ((x) >> (32 - (int)(n))))

uint32_t f(uint32_t a, uint32_t b) {

  b = rotl(a, b & 31);
  return b;
}

int main() {
  if (f(2, 31) != 1) {
    abort();
  }
  exit(0);
}
