#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdint.h>
#include <stdlib.h>

int main(void) {
  void *p = main;
  if (!((intptr_t)p & 1)) {
    abort();
  }
  return 0;
}
