#include <math.h>
#include <stdio.h>
#include <stdlib.h>

union U {
  signed b : 5;
};
int c;
volatile union U d[7] = {{8}};
short e = 1;

__attribute__((noipa)) void foo() {
  d[6].b = 0;
  d[6].b = 0;
  d[6].b = 0;
  d[6].b = 0;
  d[6].b = 0;
  e = 0;
  c = 0;
}

int main() {
  foo();
  if (e != 0) {
    __builtin_abort();
  }
  return 0;
}
