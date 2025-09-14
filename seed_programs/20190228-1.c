#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a = 1;

int main(void) {
  a = !(a && 1);
  if (a < -1) {
    a = ~a;
  }

  if (!a) {
    __builtin_abort();
  }

  return 0;
}
