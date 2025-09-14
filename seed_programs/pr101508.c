#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  unsigned i;
  for (i = 0; i < 3; ++i) {
    if (i > i * 2) {
      __builtin_abort();
    }
  }
  return 0;
}
