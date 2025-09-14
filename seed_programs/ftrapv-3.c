#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
unsigned long foo(long i, long j) {

  return -(unsigned long)(i * -j);
}
int main() {
  if (foo(-__LONG_MAX__ - 1, -1) != -(unsigned long)(-__LONG_MAX__ - 1)) {
    abort();
  }
  return 0;
}
