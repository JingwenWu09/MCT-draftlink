#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int x = 1;
  while (x) {
    x <<= 1;
  }
  return x;
}
