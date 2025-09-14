#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int __attribute__((noipa)) foo(int how) {
  switch (how) {
  case 2:
    how = 205;
    break;
  case 3:
    how = 305;
    break;
  case 4:
    how = 405;
    break;
  case 5:
    how = 505;
    break;
  case 6:
    how = 605;
    break;
  }
  return how;
}

int main() {
  if (foo(2) != 205) {
    __builtin_abort();
  }

  if (foo(6) != 605) {
    __builtin_abort();
  }

  if (foo(123) != 123) {
    __builtin_abort();
  }

  return 0;
}
