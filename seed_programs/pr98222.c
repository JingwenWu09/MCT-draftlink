#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b, *c;

int f(int j, int k) {
  b = k / j;
  if (a) {
    f(0, 0);
  }
  *c = f(b & a, 0);
  return 0;
}

int main() {
  if (a) {
    while (1) {
      f(0, 0);
    }
  }
  return 0;
}
