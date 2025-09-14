#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
  float x[argc];
  float y[argc];
  return 0 == __builtin_memcpy(y, x, argc * sizeof(*x));
}
