#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned __int128 u128;

u128 foo(unsigned short a) {
  u128 g;
  __builtin_sub_overflow((unsigned long long)-a, 1, &g);
  return g;
}

int main() {
  if (__SIZEOF_LONG_LONG__ * __CHAR_BIT__ != 64 || __SIZEOF_SHORT__ * __CHAR_BIT__ != 16) {
    return 0;
  }
  if (foo(1) != 0xfffffffffffffffeULL) {
    __builtin_abort();
  }
  return 0;
}
