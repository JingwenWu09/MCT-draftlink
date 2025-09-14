#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
int a[7][8];
int main() {
  int b, j;
  b = 0;
  for (; b < 7; b++) {
    j = 0;
    for (; j < 8; j++) {
      a[b][j] = 0;
    }
  }

  optimize_me_not();

  return 0;
}
