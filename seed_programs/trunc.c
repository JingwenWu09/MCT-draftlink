#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  printf("%x, %x\n", (unsigned char)main, main);
}

foo(p) char *p;
{
  p[0] = (char)foo;
  p[1] = (char)foo;
}
