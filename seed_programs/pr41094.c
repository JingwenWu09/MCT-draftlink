#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <math.h>

extern void abort(void);

double foo(void) {
  double x = -4.0;
  return pow(x * x, 0.25);
}

int main() {
  double r = foo();
  if (r != 2.0) {
    abort();
  }
  return 0;
}
