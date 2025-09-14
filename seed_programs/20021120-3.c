#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#if __SH64__ == 32
#define LOC asm("r10")
#else
#define LOC
#endif

unsigned int foo(char *c, unsigned int x, unsigned int y) {
  register unsigned int z LOC;

  sprintf(c, "%d", x / y);
  z = x + 1;
  return z / (y + 1);
}

int main() {
  char c[16];

  if (foo(c, ~1U, 4) != (~0U / 5)) {
    abort();
  }
  exit(0);
}
