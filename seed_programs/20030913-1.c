#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int g;

void fn2(int **q) {
  *q = &g;
}

void test() {
  int *p;

  fn2(&p);

  *p = 42;
}

int main() {
  test();
  if (g != 42) {
    abort();
  }
  exit(0);
}
