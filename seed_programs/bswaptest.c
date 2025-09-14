#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int vv;

__attribute__((noclone, noinline)) long foo(long x) {
  long f = __builtin_bswap64(x);
  long g = f;
  asm volatile("" : "+r"(f));
  vv++;
  return f;
}

__attribute__((noclone, noinline)) int bar(int x) {
  int f = __builtin_bswap32(x);
  int g = f;
  asm volatile("" : "+r"(f));
  vv++;
  return f;
}

int main() {
  foo(0x123456789abcde0fUL);
  bar(0x12345678);
  return 0;
}
