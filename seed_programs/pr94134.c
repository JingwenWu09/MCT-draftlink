#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static volatile int a = 0;
static volatile int b = 1;

int main() {
  a++;
  b++;
  if (a != 1 || b != 2) {
    __builtin_abort();
  }
  return 0;
}
