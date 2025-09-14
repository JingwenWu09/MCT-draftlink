#include <float.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  volatile float f = FLT_TRUE_MIN;
  volatile double d = DBL_TRUE_MIN;
  volatile long double l = LDBL_TRUE_MIN;
  if (f == 0 || d == 0 || l == 0) {
    __builtin_abort();
  }
  return 0;
}
