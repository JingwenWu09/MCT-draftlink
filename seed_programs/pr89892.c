#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
volatile int a;
static short b[3][9][1] = {5};
int c;
int main() {
  int i, d;
  i = 0;
  for (; i < 3; i++) {
    c = 0;
    for (; c < 9; c++) {
      d = 0;
      for (; d < 1; d++) {
        a = b[i][c][d];
      }
    }
  }
  i = c = 0;
  for (; c < 7; c++) {
    for (; d < 6; d++) {
      a;
    }
  }

  optimize_me_not();
}
