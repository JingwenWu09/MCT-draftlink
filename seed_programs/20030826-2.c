#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

struct S {
  int *a;
  unsigned char *b, c;
};

int u, v, w;

void foo(unsigned short x) {
  u += x;
}

int bar(struct S **x, int *y) {
  w += *y;
  *y = w + 25;
  return 0;
}

int main(void) {
  struct S ai, *b = &ai;
  unsigned char c;

  __builtin_memset(b, 0, sizeof(ai));
  ai.a = &v;
  ai.b = &c;

  struct S **x = &b;
  int returnValue;
  struct S *y = *x;
  unsigned char *a = y->b;

  foo(*a);

  if (__builtin_expect(y->c != 0 || y->a == &v, 0)) {
    returnValue = 1;
    if (returnValue != 1) {
		  abort();
		}
		exit(0);
  }
  if (__builtin_expect(*a == 1, 0)) {
    int a, b = bar(x, &a);

    if (a) {
      returnValue = b;
      if (returnValue != 1) {
				abort();
			}
			exit(0);
    }
  }
  returnValue = 0;

  if (returnValue != 1) {
    abort();
  }
  exit(0);
}
