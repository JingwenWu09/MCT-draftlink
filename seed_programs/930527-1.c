#include <math.h>
#include <stdio.h>
#include <stdlib.h>
f(unsigned char x) {
  return (0x50 | (x >> 4)) ^ 0xff;
}

main() {
  if (f(0) != 0xaf) {
    abort();
  }
  exit(0);
}
