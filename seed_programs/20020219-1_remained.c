#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#if __INT_MAX__ > 32767

extern void abort(void);
extern void exit(int);
struct A {
  int a[10000][10000];
};
int b[2] = {213151, 0};

void foo(struct A *x, int y) {
  if (x->a[9999][9999] != x->a[y][y]) {
    abort();
  }
  if (x->a[9999][9999] != 213151) {
    abort();
  }
}

int main(void) {
  struct A *x;
  asm("" : "=r"(x) : "0"(&b[1]));
  foo(x - 1, 9999);
  exit(0);
}

#else

int main() {
  return 0;
}

#endif
