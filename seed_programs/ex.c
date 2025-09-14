#include <math.h>
#include <stdio.h>
#include <stdlib.h>

foo(a, b) {
  if ((a & (1 << b)) == 0) {
    return 1;
  }
  return 0;
}

main() {
  printf("%d\n", foo());
}
