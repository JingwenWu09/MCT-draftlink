#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  if ((__complex__ double)0.0 != (__complex__ double)(-0.0)) {
    abort();
  }

  if (0.0 != -0.0) {
    abort();
  }
  exit(0);
}
