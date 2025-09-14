#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int *p = __builtin_malloc(4);
  *p = 4;
  return 0;
}
