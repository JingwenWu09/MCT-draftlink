#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdint.h>

int i;
uintptr_t __attribute__((noinline, const)) bar(int ***p) {
  return (uintptr_t)p;
}
void __attribute__((noinline)) foo(void) {
  int *y;
  int **a = &y, **x;
  int ***p;
  uintptr_t b;
  b = bar(&a);
  p = (int ***)b;
  x = *p;
  *x = &i;
  *y = 0;
}
extern void abort(void);
int main() {
  i = 1;
  foo();
  if (i != 0) {
    abort();
  }
  return 0;
}
