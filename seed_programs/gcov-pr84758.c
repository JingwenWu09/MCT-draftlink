#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int x, y;

static void foo(int a, int b) {
  {
    if (a == 1 || a == 2) {
      x = 4;
      if (b == 3) {
        x = 6;
      }
    } else {
      x = 15;
    }
  }
}

int main(void) {
  foo(2, 3);
  return 0;
}
