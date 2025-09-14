#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <float.h>

#ifndef INFINITY
#error "INFINITY undefined"
#endif

extern void abort(void);
extern void exit(int);

int main(void) {
  (void)_Generic(INFINITY, float : 0);
  if (!(INFINITY >= FLT_MAX)) {
    abort();
  }
  exit(0);
}
