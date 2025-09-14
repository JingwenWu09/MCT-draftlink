#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int a = -42;
  int b = -43;
  volatile int c = 129;
  1 << c;
  1 << (c + 1);
  a << 1;
  b << 1;
}
