#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int a;
};

int main() {
  struct S s;
  struct S *p = &s;
  return p->a;
}
