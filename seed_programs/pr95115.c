#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <fenv.h>
#include <stdlib.h>

double x(void) {
  double d = __builtin_inf();
  return d / d;
}

int main(void) {
  double r = x();
  if (!__builtin_isnan(r)) {
    abort();
  }
  if (!fetestexcept(FE_INVALID)) {
    abort();
  }
  exit(0);
}
