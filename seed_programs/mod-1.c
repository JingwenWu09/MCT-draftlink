#include <math.h>
#include <stdio.h>
#include <stdlib.h>
f(x, y) {
  if (x % y != 0) {
    abort();
  }
}

main() {
  f(-5, 5);
  exit(0);
}
