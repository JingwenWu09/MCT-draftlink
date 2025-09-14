#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  unsigned __int128 x;
  x = 0xFFFFFFFFFFFFFFFFULL;
  x /= ~0x7FFFFFFFFFFFFFFFLL;
  if (x != 0) {
    __builtin_abort();
  }
  x = ~0x7FFFFFFFFFFFFFFELL;
  x /= ~0x7FFFFFFFFFFFFFFFLL;
  if (x != 1) {
    __builtin_abort();
  }
  return 0;
}
