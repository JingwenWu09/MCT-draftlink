#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  int i;
  for (i = 0; i < 10; i++) {
    continue;
  }
  if (i < 10) {
    abort();
  }
  exit(0);
}
