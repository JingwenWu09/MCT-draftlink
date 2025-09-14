#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdio.h>

int main() {
  const double negval = -1.0;
  const double nanval = 0.0 / 0.0;
  const double val = __builtin_fmax(negval, nanval);
  const double absval = __builtin_fabs(val);
  printf("fabs(%.16e) = %.16e\n", val, absval);
  return absval >= 0 ? 0 : 1;
}
