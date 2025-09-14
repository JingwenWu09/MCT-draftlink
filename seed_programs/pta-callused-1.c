#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int i;
int **__attribute__((noinline, pure)) foo(int **p) {
  i;
  return p;
}
int bar(void) {
  int i = 0, j = 1;
  int *p, **q;
  p = &i;
  q = foo(&p);
  *q = &j;
  return *p;
}
extern void abort(void);
int main() {
  if (bar() != 1) {
    abort();
  }
  return 0;
}
