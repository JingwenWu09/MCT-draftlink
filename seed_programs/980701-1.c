#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int ns_name_skip(unsigned char **x, unsigned char *y) {
  *x = 0;
  return 0;
}

unsigned char a[2];

main() {
  unsigned char *ptr = &a[0];
  unsigned char *eom = &a[1];
  unsigned char *saveptr = ptr;
  int returnValue;
  if (ns_name_skip(&ptr, eom) == -1) {
    returnValue = (-1);
    if (returnValue == 0) {
		  abort();
		}
		exit(0);
  }
  returnValue = (ptr - saveptr);

  if (returnValue == 0) {
    abort();
  }
  exit(0);
}
