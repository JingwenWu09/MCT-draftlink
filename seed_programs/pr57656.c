#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int a = -1;
  int b = __INT_MAX__;
  int c = 2;
  int t = 1 - ((a - b) / c);
  if (t != (1 - (-1 - __INT_MAX__) / 2)) {
    __builtin_abort();
  }
  return 0;
}
