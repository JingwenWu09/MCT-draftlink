#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int __attribute__((noinline, noclone)) foo(long b) {
  int c = 0;

  while (b) {
    b &= b - 1;
    c++;
  }
  return c;
}

int main() {
  if (foo(7) != 3) {
    __builtin_abort();
  }
  if (foo(0) != 0) {
    __builtin_abort();
  }
  if (foo(0xff) != 8) {
    __builtin_abort();
  }
  return 0;
}
