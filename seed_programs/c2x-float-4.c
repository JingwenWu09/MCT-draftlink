#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <float.h>

#ifndef NAN
#error "NAN undefined"
#endif

volatile float f = NAN;

extern void abort(void);
extern void exit(int);

int main(void) {
  (void)_Generic(NAN, float : 0);
  if (!__builtin_isnan(NAN)) {
    abort();
  }
  if (!__builtin_isnan(f)) {
    abort();
  }
  if (!__builtin_isnan(f + f)) {
    abort();
  }
  exit(0);
}
