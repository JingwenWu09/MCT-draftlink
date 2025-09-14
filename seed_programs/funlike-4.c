#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#define f(y) y

int main() {
#if f(1) == f(1)
  int x;
#endif

  x = 0;
  if (f(0)) {
    abort();
  }

  return 0;
}
