#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  if (1.17549435e-38F <= 1.1) {
    exit(0);
  }
  abort();
}
