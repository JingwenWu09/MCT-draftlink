#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  long double x;

  x = 0x1.0p-500L;
  x *= 0x1.0p-522L;
  if (x != 0x1.0p-1022L) {
    abort();
  }
  exit(0);
}
