#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noipa)) void foo(unsigned short a) {
  __uint128_t b = 5;
  int size = __SIZEOF_INT128__ * __CHAR_BIT__ - 1;
  a /= 0xfffffffd;
  __uint128_t c = (b << (a & size) | b >> (-(a & size) & size));
  if (c != 5) {
    __builtin_abort();
  }
}

int main() {
  foo(0);
  return 0;
}
