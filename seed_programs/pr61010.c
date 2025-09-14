#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int a = 0;
  unsigned b = (a * 64 & 192) | 63U;
  return 0;
}
