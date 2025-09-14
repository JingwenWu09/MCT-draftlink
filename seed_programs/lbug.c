#include <math.h>
#include <stdio.h>
#include <stdlib.h>
long long x = 0;
main() {
  if (x--) {
    return 255;
  }
  return 0;
}
