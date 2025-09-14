#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define BIG_CONSTANT 0xFFFFFFFF80000000ULL

int main(void) {
  unsigned long long OneULL = 1ULL;
  unsigned long long result;

  result = OneULL / BIG_CONSTANT;
  if (result) {
    abort();
  }
  exit(0);
}
