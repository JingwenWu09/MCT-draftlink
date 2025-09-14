#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  char buf[8];
};

__attribute__((noipa)) void bar(struct S *x) {
  int i;
  for (i = 0; i < 8; i++) {
    if (x->buf[i] != ((i == 1) + (i == 3) * 2)) {
      __builtin_abort();
    }
  }
}

int main() {
  __attribute__((aligned(8))) struct S s = {};
  s.buf[1] = 1;
  s.buf[3] = 2;
  bar(&s);
  return 0;
}
