#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int p;
int k;
unsigned int n = 30;

void x() {
  unsigned int h;

  h = n <= 30;
  if (h) {
    p = 1;
  } else {
    p = 0;
  }

  if (h) {
    k = 1;
  } else {
    k = 0;
  }
}

main() {
  x();
  if (p != 1 || k != 1) {
    abort();
  }
  exit(0);
}
