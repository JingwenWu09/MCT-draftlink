#include <math.h>
#include <stdio.h>
#include <stdlib.h>
foo(a) double a;
{ printf("%d\n", (int)a); }

main() {
  foo(1.6);
  foo(1.4);
  foo(-1.4);
  foo(-1.6);
}
