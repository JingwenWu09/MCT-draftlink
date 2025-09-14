#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, c, *d = &c, **e = &d, *g = &a;
static int ***b, **f = &d;

int main() {
  **f = 0;
  int ****h = 0;
  if (c) {
    *h = &e;
    ***b = 0;
  }
  *e = g;

  if (d != &a) {
    __builtin_abort();
  }

  return 0;
}
