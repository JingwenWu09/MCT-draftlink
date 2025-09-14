#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int x = -__INT_MAX__ - 1;
  x = (x <= 0 ? x : -x);
  if (x != -__INT_MAX__ - 1) {
    __builtin_abort();
  }
  return 0;
}
