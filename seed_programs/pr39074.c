#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __INTPTR_TYPE__ intptr_t;

int i;
void __attribute__((noinline)) foo(long b, intptr_t q) {
  int *y;
  int **a = &y, **x;
  int ***p;
  if (b) {
    p = (int ***)q;
  } else {
    p = &a;
  }
  x = *p;
  *x = &i;
  *y = 0;
}
extern void abort(void);
int main() {
  i = 1;
  foo(0, 0);
  if (i != 0) {
    abort();
  }
  return 0;
}
