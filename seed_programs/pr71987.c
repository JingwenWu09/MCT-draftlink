#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b, *c, *d;

short fn1(int p1) {
  return a ? p1 : a;
}

void fn2() {
  int e, *f = &e;
  b = fn1(d != &e);
  c = f;
}

int main() {
  fn2();
  return 0;
}
