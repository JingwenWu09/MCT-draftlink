#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int x;
int main() {
  x = 0;
  int z = x;
  int w = 1;
  for (int i = 0; i < 32; ++i) {
    if (z) {
      w = 2;
    } else {
      w = 1;
    }
    if (w == 2) {
      __builtin_abort();
    }
  }
  return w;
}
