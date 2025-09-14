#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int foo(unsigned b, int c) {
  return b / c;
}

int main() {
  if (foo(1, 2) != 0) {
    __builtin_abort();
  }
  return 0;
}
