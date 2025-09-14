#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define f(x) x
extern void abort(void);

int main() {
  if (f(
#if f(1)
          0)) {
#else
          1))
#endif
    abort();
  }

  if (1 != f(2
#undef f
#define f -1
             f)) {
    abort();
  }

  return 0;
}
