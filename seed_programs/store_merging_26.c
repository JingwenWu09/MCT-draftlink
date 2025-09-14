#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noipa)) void foo(int *x) {
  asm volatile("" : : "r"(x) : "memory");
}

__attribute__((noipa)) int bar() {
  int x;
  foo(&x);
  x = 3;
  ((char *)&x)[1] = 1;
  foo(&x);
  return x;
}

int main() {
  int x;
  foo(&x);
  x = 3;
  foo(&x);
  ((char *)&x)[1] = 1;
  foo(&x);
  if (x != bar()) {
    __builtin_abort();
  }
  return 0;
}
