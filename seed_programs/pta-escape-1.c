#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int *p;
void __attribute__((noinline, noclone)) bar(void) {
  *p = 1;
}
int __attribute__((noinline, noclone)) foo(__INTPTR_TYPE__ addr) {
  int i;

  int **q = (int **)addr;

  *q = &i;
  i = 0;

  bar();
  return i;
}
extern void abort(void);
int main() {
  if (foo((__INTPTR_TYPE__)&p) != 1) {
    abort();
  }
  return 0;
}
