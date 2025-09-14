#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

extern void abort(void);
extern void exit(int);

int main(void) {
  int a = INT_MAX;
  if ((a < 0) || (a + 1 < 0)) {
    exit(0);
  }

  return 0;
}
