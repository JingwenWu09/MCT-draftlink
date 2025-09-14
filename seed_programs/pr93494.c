#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned short a;

int main() {
  register unsigned long long y = 0;
  int x = __builtin_add_overflow(y, 0ULL, &a);
  if (x || a) {
    __builtin_abort();
  }
  return 0;
}
