#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a[199];

extern void abort(void);

int main() {
  int i, x;
  for (i = 0; i < 199; i++) {
    x = a[i];
    if (x != i) {
      ;
    }
  }
  return 0;
}
