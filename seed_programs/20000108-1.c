#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

void foo() {
}

int main() {
  int i;

  asm("" : "=r"(i) : "0"(-1));
  asm("" : "=r"(i) : "0"(i ? 1 : 2));
  if (i != 1) {
    abort();
  }
  return 0;
}
