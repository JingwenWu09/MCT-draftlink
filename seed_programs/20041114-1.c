#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

void abort(void);
void link_failure(void);

volatile int v;

void foo(int var) {
  if (!(var <= 0 || ((long unsigned)(unsigned)(var - 1) < UINT_MAX))) {
    link_failure();
  }
}

int main(int argc, char **argv) {
  foo(v);
  return 0;
}

#ifndef __OPTIMIZE__
void link_failure(void) {
  abort();
}
#endif
