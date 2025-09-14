#include <math.h>
#include <stdio.h>
#include <stdlib.h>

signed char __attribute__((noipa)) foo(signed char how) {
  switch (how) {
  case -4:
    how = 96;
    break;
  case -3:
    how = -120;
    break;
  case -2:
    how = -80;
    break;
  case -1:
    how = -40;
    break;
  case 0:
    how = 0;
    break;
  case 1:
    how = 40;
    break;
  }
  return how;
}

int main() {
  if (foo(-4) != 96) {
    __builtin_abort();
  }

  if (foo(-3) != -120) {
    __builtin_abort();
  }

  if (foo(0) != 0) {
    __builtin_abort();
  }

  if (foo(123) != 123) {
    __builtin_abort();
  }

  return 0;
}
