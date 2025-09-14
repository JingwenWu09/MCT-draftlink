#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern double fabs(double);
extern void link_error(void);

void foo(double x) {
  double p, q;

  p = fabs(x);
  q = 0.0;
  if (p < q) {
    link_error();
  }
}

int main() {
  foo(1.0);
  return 0;
}

#ifndef __OPTIMIZE__
void link_error() {
  abort();
}
#endif
