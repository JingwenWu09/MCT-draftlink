#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char a, f = 1;
int b, c, e[8];
short d;

short foo(short x) {
  return x >= 2 || x >> c ? x : x << c;
}

int main() {
  while (f) {
    for (d = 0; d <= 7; d++) {
      f = 7 >> b ? a : a << b;
      e[d] = foo(f);
    }
  }
  return 0;
}
