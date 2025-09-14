#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int main(void) {
  int i, j;
  i = 1;
  j = __imag__ ++i;
  if (i != 2 || j != 0) {
    abort();
  }
  return 0;
}
