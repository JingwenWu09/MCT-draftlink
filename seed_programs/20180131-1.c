#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

union U{
  signed short ss;
  unsigned short us;
  int x;
};

int f(int x, int y, int z, int a, union U u) __attribute__((noclone, noinline));

int f(int x, int y, int z, int a, union U u) {
  return (u.ss <= 0) + u.us;
}

int main(void) {
  union U u = {.ss = -1};

  if (f(0, 0, 0, 0, u) != (1 << sizeof(short) * 8)) {
    abort();
  }

  return 0;
}
