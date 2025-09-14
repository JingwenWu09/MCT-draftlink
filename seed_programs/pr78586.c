#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void foo(unsigned long x) {
  char a[30];
  unsigned long b = __builtin_sprintf(a, "%lu", x);
  if (b != 4)
    __builtin_abort();
}

int main() {
  foo(1000);
  return 0;
}
