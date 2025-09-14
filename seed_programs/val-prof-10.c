#include <math.h>
#include <stdio.h>
#include <stdlib.h>

long buffer1[128], buffer2[128];
char *x;

void foo(long *r) {
  x = (char *)r;
  asm volatile("" ::: "memory");
}

void __attribute__((noinline)) compute() {
  volatile int n = 24;
  __builtin_memcpy(buffer1, buffer2, n);
  foo(&buffer1[0]);
}

int main() {
  for (unsigned i = 0; i < 10000; i++) {
    compute();
  }

  return 0;
}
