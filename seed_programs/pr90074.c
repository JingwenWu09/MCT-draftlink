#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
char a;
short b[7][1];
int main() {
  int i, c;
  a = 0;
  i = 0;
  for (; i < 7; i++) {
    c = 0;
    for (; c < 1; c++) {
      b[i][c] = 0;
    }
  }

  optimize_me_not();

  return 0;
}
