#include <math.h>
#include <stdio.h>
#include <stdlib.h>
f(c) unsigned char c;
{
  if (c != 0xFF) {
    abort();
  }
}

main() {
  f(-1);
  exit(0);
}
