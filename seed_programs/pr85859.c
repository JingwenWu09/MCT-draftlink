#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int b, c, d, e;

__attribute__((noinline, noclone)) int foo(short f) {
  f %= 0;
  return f;
}

int main(void) {
  b = (unsigned char)__builtin_parity(d);
  e ? foo(0) : (__INTPTR_TYPE__)&c;
  return 0;
}
