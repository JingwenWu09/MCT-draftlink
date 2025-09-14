#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define FORTIFY_SOURCE 2
#include <stdarg.h>
#include <stdio.h>

static char buf[4096];
static char gfmt[] = "%Lg";

static int __attribute__((noipa)) foo(char *str, const char *fmt, ...) {
  int ret;
  va_list ap;
  va_start(ap, fmt);
  ret = vsnprintf(str, 4096, fmt, ap);
  va_end(ap);
  return ret;
}

int main() {
  long double dval = 128.0L;
  int ret = foo(buf, gfmt, dval);
  if (ret != 3 || __builtin_strcmp(buf, "128") != 0) {
    __builtin_abort();
  }
  return 0;
}
