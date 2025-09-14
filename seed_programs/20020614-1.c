#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

extern void abort(void);
extern void exit(int);

int main(void) {
  int i;
  signed char j;
  unsigned char k;

  i = SCHAR_MAX;

  j = ((signed char)(i << 1)) / 2;

  if (j != -1) {
    abort();
  }

  j = ((signed char)(i * 2)) / 2;

  if (j != -1) {
    abort();
  }

  i = UCHAR_MAX;

  k = ((unsigned char)(i << 1)) / 2;

  if (k != UCHAR_MAX / 2) {
    abort();
  }

  k = ((unsigned char)(i * 2)) / 2;

  if (k != UCHAR_MAX / 2) {
    abort();
  }

  exit(0);
}
