#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>

struct T{
  int A;
};

struct T f(int x, ...) {
  va_list ap;
  struct T X;
  va_start(ap, x);
  X = va_arg(ap, struct T);
  if (X.A != 10) {
    abort();
  }
  X = va_arg(ap, struct T);
  if (X.A != 20) {
    abort();
  }
  va_end(ap);
  return X;
}

main() {
  struct T X, Y;
  int i;
  X.A = 10;
  Y.A = 20;
  f(2, X, Y);
  exit(0);
}
