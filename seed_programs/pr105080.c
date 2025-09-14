#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  char foo[3];
  int i;

  for (i = 0; i < 16; i++) {
    __builtin_snprintf(foo, sizeof(foo), "%d", i);
  }
}
