#include <math.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

double a = 40.0;

void test(int x, ...) {
  va_list ap;
  int i;
  va_start(ap, x);
  if (va_arg(ap, int) != 1) {
    abort();
  }
  if (va_arg(ap, int) != 2) {
    abort();
  }
  if (va_arg(ap, int) != 3) {
    abort();
  }
  if (va_arg(ap, int) != 4) {
    abort();
  }
}

int main(int argc, char *argv[]) {
  test(0, 1, 2, 3, (int)(a / 10.0));
  exit(0);
}
