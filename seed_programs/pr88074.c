#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <complex.h>

int main() {
  _Complex double x;
  __real x = 3.091e+8;
  __imag x = -4.045e+8;

  volatile _Complex double y = ctan(x);
  return 0;
}
