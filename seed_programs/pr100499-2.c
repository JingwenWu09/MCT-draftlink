#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned char ag = 55;
unsigned i;
int main() {
  unsigned char c;
  unsigned char a = ag;
d:
  c = a-- * 52;
  if (c) {
    goto d;
  }
  if (a != 255) {
    __builtin_abort();
  }
  return 0;
}
