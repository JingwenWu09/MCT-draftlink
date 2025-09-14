#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main(void) {
  char *d = (char *)__builtin_alloca(12);

  __builtin_sprintf(d, "%32s", "x");

  return 0;
}
