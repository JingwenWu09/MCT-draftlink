#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int x;
};
struct A b[2];
struct A *c = b, *d = b;
int e;

int foo() {
  for (e = 0; e < 1; e++) {
    int i[1];
    i;
  }
  for (int h = 0; h < 3; h++) {
    *c = *d;
  }
  *c = *(b + 3);
  return c->x;
}

int main() {
  foo();
  return 0;
}
