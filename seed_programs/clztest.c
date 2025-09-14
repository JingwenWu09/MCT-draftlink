#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int vv;

__attribute__((noinline, noclone)) long foo(long x) {
  long f = __builtin_clzl(x);
  long g = f;
  asm volatile("" : "+r"(f));
  vv++;
  return f;
}

__attribute__((noinline, noclone)) long bar(long x) {
  long f = __builtin_clzl(x);
  long g = f;
  asm volatile("" : "+r"(f));
  vv++;
  return f;
}

int main() {
  long x = vv;
  foo(x + 0x123456UL);
  bar(x + 0x7fffffffUL);
  return 0;
}
