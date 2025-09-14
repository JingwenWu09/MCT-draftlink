#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int a, b, c;
int *d = &c;
int **e = &d;
int ***f = &e;
int main() {
  int h;
  for (a = 2; a; a--) {
    for (h = 0; h <= 2; h++) {
      for (b = 0; b <= 2; b++) {
        ***f = 6;
      }
    }
  }

  if (b != 3) {
    __builtin_abort();
  }
}
