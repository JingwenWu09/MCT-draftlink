#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  if (__builtin_strcmp(__DATE__, "Dec 22 1989") == 0 && __builtin_strcmp(__TIME__, "12:34:56") == 0) {
    __builtin_abort();
  }
  return 0;
}
