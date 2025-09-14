#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int a, b, c, d, e;
};
struct S t;
struct S s;

__attribute__((used, noinline, noclone)) void bar(void) {
  int *x = &t.c;
  int *y = &s.c;
  asm volatile("" : : "g"(x), "g"(y) : "memory");
  if (*x != 1 || *y != 2) {
    __builtin_abort();
  }
}

int main() {
  t.c = 1;
  s.c = 2;
  bar();
  return 0;
}
