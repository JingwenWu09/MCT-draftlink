#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <math.h>

extern void abort(void);
void __attribute__((noinline)) f(double x) {
  double pluszero = pow(x, 0.5);
  double minuszero = sqrt(x);
  if (signbit(pluszero) == signbit(minuszero)) {
    abort();
  }
}

int main(void) {
  f(-0.0);
  return 0;
}
