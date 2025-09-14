#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned long long foo(unsigned long long c) {
  return c ? __builtin_ffs(-(unsigned short)c) : 0;
}

int main() {
  if (foo(2) != 2) {
    __builtin_abort();
  }
  return 0;
}
