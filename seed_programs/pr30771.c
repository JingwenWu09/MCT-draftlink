#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a[128];

int main() {
  short i;

  for (i = 0; i < 64; i++) {
    a[i] = (int)i;
  }
  return 0;
}
