#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned a, b;

int main() {
  b = !(0 | ~0);
  a = ~b / ~a;
  return 0;
}
