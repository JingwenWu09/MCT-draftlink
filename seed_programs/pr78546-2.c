#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned __int128 u128;
u128 b;

int main() {
  asm volatile("" : : : "memory");
  u128 x = ((u128)~0x7fffffffffffffffLL) - b;
  u128 y = 1 - x;
  if (y != 0x8000000000000001ULL) {
    __builtin_abort();
  }
  return 0;
}
