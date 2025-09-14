#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int a = 0;
  unsigned long long one = 1;
  ((-1ULL / one) < a++, one);
  if (a != 1) {
    __builtin_abort();
  }
  return 0;
}
