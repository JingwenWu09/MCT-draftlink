#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct {
  int a : 8;
  int b : 24;
} c = {0, 1};

int main() {
  if (c.b && !c.b) {
    __builtin_abort();
  }
  return 0;
}
