#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int v;

__attribute__((noinline, noclone)) int bar(int a, int b) {
#ifdef __x86_64__
  asm volatile("movq %%rsp, %%r10" : : : "r10");
#else
  asm volatile("movl %%esp, %%ecx" : : : "ecx");
#endif
  return 0;
}

__attribute__((noinline, noclone)) int foo(int v0, int v1, int v2, int v3, int v4, int v5, int a, int b) {
  __attribute__((aligned(32))) int c = bar(a, b);
  v++;
  return a + b + c;
}

int main(void) {
  foo(0, 0, 0, 0, 0, 0, 5, 6);
  return 0;
}
