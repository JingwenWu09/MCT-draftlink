#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int b;
  int c;
  int d;
} e;

struct A foo(void) {
  struct A h[30] = {{0, 0, 0}};
  return h[29];
}

int main() {
  e = foo();
  return e.b;
}
