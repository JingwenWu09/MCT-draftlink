#include <math.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

f(m) {
  int i, s = 0;
  for (i = 0; i < m; i++) {
    s += i;
  }
  return s;
}

main() {
  exit(0);
}
