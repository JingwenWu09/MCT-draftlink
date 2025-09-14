#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct a {
  unsigned int bitfield : 1;
};

unsigned int x;

main() {
  struct a a = {0};
  x = 0xbeef;
  a.bitfield |= x;
  if (a.bitfield != 1) {
    abort();
  }
  exit(0);
}
