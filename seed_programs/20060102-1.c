#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort();

int f(int x) {
  return (x >> (sizeof(x) * __CHAR_BIT__ - 1)) ? -1 : 1;
}

volatile int one = 1;
int main(void) {

  if (f(one) == f(-one)) {
    abort();
  }
  return 0;
}
