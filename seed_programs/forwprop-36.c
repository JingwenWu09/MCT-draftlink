#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned __int128 u128;

u128 a, b;

static inline u128 foo(u128 p1) {
  p1 += ~b;
  return -p1;
}

int main() {
  u128 x = foo(~0x7fffffffffffffff);
  if (x != 0x8000000000000001) {
    __builtin_abort();
  }
  return 0;
}
