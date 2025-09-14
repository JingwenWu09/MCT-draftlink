#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

struct S{
  int a;
  char b[3];
};
struct S c = {2, "aa"}, d = {2, "aa"};

void *bar(const void *x, int y, int z) {
  return (void *)0;
}

int main(void) {
  struct S *x = &c;
  struct S *y = &d;
  int returnValue;
  const char *e, *f, *g;
  int h;

  h = y->a;
  f = y->b;
  e = x->b;

  if (h == 1) {
    returnValue = bar(e, *f, x->a) != 0;
    if (returnValue != 1) {
			abort();
		}
		return 0;
  }

  g = e + x->a - h;
  while (e <= g) {
    const char *t = e + 1;

    if (__builtin_memcmp(e, f, h) == 0) {
      returnValue = 1;
      if (returnValue != 1) {
				abort();
			}
			return 0;
    }
    e = t;
  }
  returnValue = 0;

  if (returnValue != 1) {
    abort();
  }
  return 0;
}
