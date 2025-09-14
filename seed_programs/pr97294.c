#include <math.h>
#include <stdio.h>
#include <stdlib.h>
__attribute__((noipa)) void foo(int *p, int n) {
  int i;
#pragma omp parallel for num_threads(2) reduction(+ : p[:n])
  for (i = 0; i < 10; i++) {
    p[0]++;
    p[n - 1] += 2;
  }
}

__attribute__((noipa)) void bar(void) {
  unsigned char buf[1024];
  int i;
  asm volatile("" : : "r"(&buf[0]) : "memory");
  for (i = 0; i < 1024; i++) {
    buf[i] = i;
  }
  asm volatile("" : : "r"(&buf[0]) : "memory");
}

int main() {
  int p[50], i;
  for (i = 0; i < 50; i++) {
    p[i] = 0;
  }
  foo(p, 50);
  bar();
  if (p[0] != 10 || p[49] != 20) {
    __builtin_abort();
  }
  return 0;
}
