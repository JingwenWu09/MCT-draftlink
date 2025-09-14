#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int buffer[256 * 256];
int main(void) {
  int *dest = buffer;
  int x, y;
  for (x = 0; x < 256; x++) {
    for (y = 0; y < 256; y++) {
      *dest++ = 0;
    }
  }
  return 0;
}
