#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <fenv.h>
#include <float.h>

#ifndef NAN
#error "NAN undefined"
#endif

volatile float f = NAN;

extern void abort(void);
extern void exit(int);

int main(void) {
  f += f;
  if (fetestexcept(FE_INVALID)) {
    abort();
  }
  exit(0);
}
