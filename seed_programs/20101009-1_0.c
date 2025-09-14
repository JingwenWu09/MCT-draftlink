#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static inline void bar(unsigned *u) {
  __asm__("" : "=r"(*u));
}

void foo(void) {
  int i;
  bar(&i);
}

int main() {
  return 0;
}
