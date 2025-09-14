#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
int *g;
int dummy;

int *__attribute__((noinline, const, noipa)) foo_const(int *p) {
  return p;
}

int *__attribute__((noinline, pure, noipa)) foo_pure(int *p) {
  return p + dummy;
}

int *__attribute__((noinline, noipa)) foo_normal(int *p) {
  g = p;
  return p;
}

void test_const(void) {
  int i;
  int *p = &i;
  int *q_const = foo_const(p);
  *p = 1;
  *q_const = 2;
  if (*p != 2) {
    abort();
  }
}

void test(void) {
  int i;
  int *p = &i;
  int *q_normal = foo_normal(p);
  *p = 1;
  *q_normal = 2;
  if (*p != 2) {
    abort();
  }
}

void test_pure(void) {
  int i;
  int *p = &i;
  int *q_pure = foo_pure(p);
  *p = 1;
  *q_pure = 2;
  if (*p != 2) {
    abort();
  }
}

int main() {
  test_const();
  test();
  test_pure();
  return 0;
}
