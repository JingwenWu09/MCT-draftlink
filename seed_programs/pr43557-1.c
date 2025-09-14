#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int v;
} g;

void f1(void) {
  struct S *s = &g;
  s->v = 0;
}

int main() {
  return 0;
}
