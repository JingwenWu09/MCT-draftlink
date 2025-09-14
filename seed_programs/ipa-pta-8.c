#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int *__attribute__((noinline, noclone)) pass_me(int *p) {
  return p;
}

static int foo(void) {
  int a = 0;
  int *p = pass_me(&a);
  *p = 1;
  return a;
}

extern void abort(void);

int main() {
  if (foo() != 1) {
    abort();
  }

  return 0;
}
