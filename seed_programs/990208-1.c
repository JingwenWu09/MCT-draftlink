#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static void *ptr1, *ptr2;
static int i = 1;

static __inline__ void doit(void **pptr, int cond) {
  if (cond) {
  here:
    *pptr = &&here;
  }
}

__attribute__((noinline)) static void f(int cond) {
  doit(&ptr1, cond);
}

__attribute__((noinline)) static void g(int cond) {
  doit(&ptr2, cond);
}

__attribute__((noinline)) static void bar(void);

int main() {
  f(i);
  bar();
  g(i);

#ifdef __OPTIMIZE__
  if (ptr1 == ptr2)
    abort();
#endif

  exit(0);
}

void bar(void) {
}
