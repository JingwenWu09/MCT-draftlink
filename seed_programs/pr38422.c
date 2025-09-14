#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

struct S {
  int s : (sizeof(int) * __CHAR_BIT__ - 2);
} s;

void foo(void) {
  s.s *= 2;
}

int main() {
  s.s = 24;
  foo();
  if (s.s != 48) {
    abort();
  }
  return 0;
}
