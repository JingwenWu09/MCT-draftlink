#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void foo(int len) {
  char array[1000];
  __builtin_memset(array, 0, len);
}

int main() {
  int i;
  for (i = 0; i < 1000; i++) {
    foo(8);
  }
  return 0;
}
