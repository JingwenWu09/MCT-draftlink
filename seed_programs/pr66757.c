#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b;

int main(void) {
  unsigned int t = (unsigned char)(~b);

  if ((t ^ 1) / 255) {
    __builtin_abort();
  }

  return 0;
}
