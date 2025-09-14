#include <math.h>
#include <stdio.h>
#include <stdlib.h>
__attribute__((noinline, noclone)) void foo(char *p) {
  asm volatile("" : : "r"(p) : "memory");
}

__attribute__((noinline, noclone)) void bar(void) {
  char buf[131072];
  foo(buf);
}

__attribute__((noinline, noclone)) void baz(void) {
  char buf[12000];
  foo(buf);
}

int main() {
  bar();
  baz();
  return 0;
}
