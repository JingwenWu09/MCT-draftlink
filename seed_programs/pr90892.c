#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const char *a = "A\0b";

int main() {
  if (__builtin_strncmp(a, "A\0", 2) != 0) {
    __builtin_abort();
  }

  return 0;
}
