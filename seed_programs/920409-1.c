#include <math.h>
#include <stdio.h>
#include <stdlib.h>
x() {
  signed char c = -1;
  return c < 0;
}
main() {
  if (x() != 1) {
    abort();
  }
  exit(0);
}
