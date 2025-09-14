#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

struct A {
  int i;
};

struct A __attribute__((noinline)) foo(void) {
  struct A a = {-1};
  return a;
}

void __attribute__((noinline)) bar(struct A *p) {
  *p = foo();
}

int main(void) {
  struct A a;
  bar(&a);
  if (a.i != -1) {
    abort();
  }
  return 0;
}
