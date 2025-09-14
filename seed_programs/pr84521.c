#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

void broken_longjmp(void *p) {
  __builtin_longjmp(p, 1);
}

volatile int x = 256;
void *volatile p = (void *)&x;
void *volatile p1;

void test(void) {
  void *buf[5];
  void *volatile q = p;

  if (!__builtin_setjmp(buf)) {
    broken_longjmp(buf);
  }

  if (p != q) {
    abort();
  }
}

void test2(void) {
  void *volatile q = p;
  p1 = __builtin_alloca(x);
  test();

  if (p != q) {
    abort();
  }
}

int main(void) {
  void *volatile q = p;
  test();
  test2();

  if (p != q) {
    abort();
  }

  return 0;
}
