#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noinline, noclone)) void bar(void *x) {
  asm volatile("" : : "r"(x) : "memory");
}

__attribute__((noinline, noclone)) void baz(void) {
  asm volatile("" : : : "memory");
}

__attribute__((noinline, noclone)) int foo(int x) {
  __label__ lab;
  if (__builtin_expect(x, 0)) {
  lab:
    baz();
    return 2;
  }
  bar(&&lab);
  return 1;
}

int main(void) {
  int x, i;
  asm volatile("" : "=r"(x) : "0"(0));
  for (i = 0; i < 1000000; i++) {
    foo(x);
  }
  return 0;
}
