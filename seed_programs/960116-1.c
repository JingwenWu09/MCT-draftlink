#include <math.h>
#include <stdio.h>
#include <stdlib.h>
static inline int p(int *p) {
  return !((long)p & 1);
}

main() {
  int *q = (int *)0xffffffff;
  int returnValue;
  if (p(q) && *q) {
    returnValue = 1;
    if (returnValue != 0) {
		  abort();
		}
		exit(0);
  }
  returnValue = 0;

  if (returnValue != 0) {
    abort();
  }
  exit(0);
}
