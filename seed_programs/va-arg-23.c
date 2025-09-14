#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>

struct two {
  long x, y;
};

void foo(int a, int b, int c, int d, int e, struct two f, int g, ...) {
  va_list args;
  int h;

  va_start(args, g);
  h = va_arg(args, int);
  if (g != 1 || h != 2) {
    abort();
  }
}

int main() {
  struct two t = {0, 0};
  foo(0, 0, 0, 0, 0, t, 1, 2);
  return 0;
}
