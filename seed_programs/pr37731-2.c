#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

long long xh = 1;

int main() {
  long long yh = 0xffffffffll;
  long long z = xh * yh;

  if (z != yh) {
    abort();
  }

  return 0;
}
