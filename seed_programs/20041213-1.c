#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#if defined(__AVR__) && (__SIZEOF_DOUBLE__ == __SIZEOF_FLOAT__)
extern double sqrt(double) __asm("sqrtf");
#else
extern double sqrt(double);
#endif
extern void abort(void);
int once;
double x;

double foo(void) {
  if (once++) {
    abort();
  }
  return 0.0 / 0.0;
}

int main(void) {
  x = sqrt(foo());
  return 0;
}
