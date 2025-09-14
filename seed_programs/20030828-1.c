#include <math.h>
#include <stdio.h>
#include <stdlib.h>
const int *p;

int bar(void) {
  return *p + 1;
}

main() {

  const int i = 5;
  p = &i;
  if (bar() != 6) {
    abort();
  }
  exit(0);
}
