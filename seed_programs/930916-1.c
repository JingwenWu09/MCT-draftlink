#include <math.h>
#include <stdio.h>
#include <stdlib.h>
f(n) unsigned n;
{
  if ((int)n >= 0) {
    abort();
  }
}

main() {
  unsigned x = ~0;
  f(x);
  exit(0);
}
