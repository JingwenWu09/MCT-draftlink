#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a;

static int __attribute__((noinline, noclone)) foo(unsigned char c) {
  c <<= (long long)c != a;
  c = c << 7 | c >> 1;
  return c;
}

int main() {
  asm volatile("" : : : "memory");
  if (foo(0) != 0) {
    __builtin_abort();
  }
  return 0;
}
