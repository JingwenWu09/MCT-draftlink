#include <math.h>
#include <stdio.h>
#include <stdlib.h>

union U {
  struct A {
    int : 2;
    int x : 8;
  } a;
  struct B {
    int : 6;
    int x : 8;
  } b;
};

int __attribute__((noipa)) foo(union U *p, union U *q) {
  p->a.x = 1;
  q->b.x = 1;
  return p->a.x;
}

int main() {
  union U x;
  if (foo(&x, &x) != x.a.x) {
    __builtin_abort();
  }
  return 0;
}
