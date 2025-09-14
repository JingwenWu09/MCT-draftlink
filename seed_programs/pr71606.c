#include <math.h>
#include <stdio.h>
#include <stdlib.h>
_Complex double a;
void fn1();

int main() {
  fn1(a);
  return 0;
}

void fn1(__complex__ long double p1) {
  __imag__ p1 = 6.0L;
}
