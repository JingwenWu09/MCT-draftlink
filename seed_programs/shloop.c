#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  int volatile p;
  int i;
  for (i = 10000000; i > 0; i--) {
    p = i >> 10;
  }
}
