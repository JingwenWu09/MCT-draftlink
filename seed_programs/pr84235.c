#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  double d = 1.0 / 0.0;
  _Bool b = d == d && (d - d) != (d - d);
  if (!b) {
    __builtin_abort();
  }
  return 0;
}
