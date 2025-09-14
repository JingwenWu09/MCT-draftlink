#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>

static void __attribute__((noinline, noclone)) foo(int i, ...) {
  va_list ap;
  int *p;
  va_start(ap, i);
  p = va_arg(ap, int *);
  *p = 1;
  va_end(ap);
}
extern void abort(void);
int main() {
  int i = 0;
  foo(0, &i);
  if (i != 1) {
    abort();
  }
  return 0;
}
