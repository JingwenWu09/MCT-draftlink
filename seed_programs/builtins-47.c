#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern double sqrt(double);
extern double pow(double, double);
extern void abort(void);

int main() {
  double x = -1.0;
  if (sqrt(pow(x, 2)) != 1.0) {
    abort();
  }
  if (sqrt(x * x) != 1.0) {
    abort();
  }
  return 0;
}
