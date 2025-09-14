#include <math.h>
#include <stdio.h>
#include <stdlib.h>
x(const char *s) {
  char a[1];
  const char *ss = s;
  a[*s++] |= 1;
  return (int)ss + 1 == (int)s;
}
main() {
  if (x("") != 1) {
    abort();
  }
  exit(0);
}
