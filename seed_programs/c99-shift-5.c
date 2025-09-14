#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int a = -42;
  int b = -43;
  volatile int c = 129;
  int d = 1;
  a << 1;
  a << 1;
  d <<= 31;
  b << c;
  a << 1;
}
