#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b, c;

int fn1(char e, char f) {
  return !f || (e && f == 1);
}

void fn2(char e) {
  while (b) {
    e = 0;
  }
  a = 128;
  c = fn1(e, a == e);
}

int main() {
  fn2(0);
  return 0;
}
