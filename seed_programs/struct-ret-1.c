#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct T{
  int re;
  int im;
};

struct T f(int, int);

#if COMPILER != 1
struct T f(int arg1, int arg2) {
  struct T x;
  x.re = arg1;
  x.im = arg2;
  return x;
}
#endif

#if COMPILER != 2
main() {
  struct T result;
  result = f(3, 4);
  if (result.re != 3 || result.im != 4) {
    abort();
  }
  exit(0);
}
#endif
