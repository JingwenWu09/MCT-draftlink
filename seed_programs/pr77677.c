#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b;

static void fn1(short p1) {
  a = -p1;
  if (a || b) {
    __builtin_printf("%d\n", b);
  }
}

int main() {
  int c[] = {40000};
  fn1(c[0]);
  return 0;
}
