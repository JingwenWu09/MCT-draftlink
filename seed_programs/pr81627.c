#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b, c, d[6], e = 3, f;

void abort(void);
void fn1() {
  for (b = 1; b < 5; b++) {
    for (c = 0; c < 5; c++) {
      d[b] = e;
    }
    if (a) {
      f++;
    }
    d[b + 1] = 1;
  }
}

int main() {
  fn1();
  if (d[0] != 0 || d[1] != 3 || d[2] != 3 || d[3] != 3 || d[4] != 3 || d[5] != 1) {
    abort();
  }

  return 0;
}
