#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
volatile long a;
int b[9][1];
static short c[2][1] = {3};
int main() {
  int i, d, e;
  i = 0;
  for (; i < 9; i++) {
    a = b[i][0];
  }
  i = 0;
  for (; i < 2; i++) {
    d = 0;
    for (; d < 1; d++) {
      e = 0;
      for (; e < 1; e++) {
        a = c[i][e];
      }

      optimize_me_not();
    }
  }
  return 0;
}
