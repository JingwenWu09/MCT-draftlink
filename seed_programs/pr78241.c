#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static __attribute__((noinline, noclone)) unsigned foo(unsigned x) {
  do {
    x++;
  } while (x <= 15);
  return x;
}

int main() {
  unsigned x = foo(-2);
  if (x != (unsigned)-1) {
    __builtin_abort();
  }
  return 0;
}
