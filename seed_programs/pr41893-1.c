#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int v;
};
struct S s;

void __attribute__((externally_visible)) func1(void) {
  struct S *p = &s;
}

int main() {
  return 0;
}
