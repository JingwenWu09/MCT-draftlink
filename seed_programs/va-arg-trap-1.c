#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdarg.h>

extern void exit(int);
extern void abort(void);

va_list ap;
float f;

va_list *foo(void) {
  exit(0);
  return &ap;
}

void bar(int i, ...) {
  va_start(ap, i);
  f = va_arg(*foo(), float);
  va_end(ap);
}

int main(void) {
  bar(1, 0);
  abort();
}
