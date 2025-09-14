#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int i : 8;
};

signed char c1, c2;
struct A a;

int main() {
  a.i = c1;
  c2 = a.i;
  return a.i;
}
