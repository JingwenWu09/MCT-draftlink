#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  long q;
} * h;
long a, b, g, j, k, *c, *d, *e, *f, *i;
long *baz(void) {
  asm volatile("" : : : "memory");
  return e;
}

void bar(int x) {
  int y;
  for (y = 0; y < x; y++) {
    switch (b) {
    case 0:
    case 2:
      a++;
      break;
    case 3:
      a++;
      break;
    case 1:
      a++;
    }
    if (d) {
      f = baz();
      g = k++;
      if (&h->q) {
        j = *f;
        h->q = *f;
      } else {
        i = (long *)(h->q = *f);
      }
      *c++ = (long)f;
      e += 6;
    } else {
      f = baz();
      g = k++;
      if (&h->q) {
        j = *f;
        h->q = *f;
      } else {
        i = (long *)(h->q = *f);
      }
      *c++ = (long)f;
      e += 6;
    }
  }
}

int main() {
  return 0;
}
