#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((noinline)) optimize_me_not() {
  __asm__ volatile("" : : : "memory");
}
int a;
int main() {
  int i;
  for (; a < 10; a++) {
    i = 0;
  }
  for (; i < 6; i++) {
    ;
  }

  optimize_me_not();
  return 0;
}
