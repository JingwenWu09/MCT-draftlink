#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const int a;

int main() {
  if (a != 0) {
    __builtin_abort();
  }
  return 0;
}
