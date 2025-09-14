#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  unsigned int s1;
  unsigned int s2;
};
struct T {
  unsigned int t1;
  struct S t2;
};
struct U {
  unsigned short u1;
  unsigned short u2;
};
struct V {
  struct U v1;
  struct T v2;
};
struct S a;
char *b;
union {
  char b[64];
  struct V v;
} u;
volatile int v;
extern void abort(void);

__attribute__((noinline, noclone)) void bar(struct S x) {
  v = x.s1;
  v = x.s2;
}

__attribute__((noinline, noclone)) int baz(struct S *x) {
  v = x->s1;
  v = x->s2;
  v = 0;
  return v + 1;
}

int main() {
  struct S *ci = 0;
  asm("" : "+r"(ci) : "r"(&a));
  u.v.v2.t2.s1 = 8192;
  b = u.b;

  struct S *c = ci;
  struct T *d;
  struct S e = a;
  unsigned int f, g;
  if (c == 0) {
    c = &e;
  } else {
    if (c->s2 % 8192 <= 15 || (8192 - c->s2 % 8192) <= 31) {
      int x = 1;
      void *y = 0;
      unsigned int z = c->s1;
      unsigned int w = c->s2;
      if (x != 4 || y != (void *)&u.v.v2) {
        abort();
      }
      v = z + w;
      v = 16384;
    }
  }
  if (!baz(c)) {
    if (v != 16384) {
		  abort();
		}
		return 0;
  }
  g = (((struct U *)b)->u2 & 2) ? 32 : __builtin_offsetof(struct V, v2);
  f = c->s2 % 8192;
  if (f == 0) {
    e.s2 += g;
    f = g;
  } else if (f < g) {
    int x = 2;
    void *y = 0;
    unsigned int z = c->s1;
    unsigned int w = c->s2;
    if (x != 4 || y != (void *)&u.v.v2) {
      abort();
    }
    v = z + w;
    v = 16384;
    if (v != 16384) {
		  abort();
		}
		return 0;
  }
  if ((((struct U *)b)->u2 & 1) && f == g) {
    bar(*c);
    int x = 3;
    void *y = 0;
    unsigned int z = c->s1;
    unsigned int w = c->s2;
    if (x != 4 || y != (void *)&u.v.v2) {
      abort();
    }
    v = z + w;
    v = 16384;
    if (v != 16384) {
			abort();
		}
		return 0;
  }
  d = (struct T *)(b + c->s2 % 8192);
  if (d->t2.s1 >= c->s1 && (d->t2.s1 != c->s1 || d->t2.s2 >= c->s2)) {
    int x = 4;
    void *y = d;
    unsigned int z = c->s1;
    unsigned int w = c->s2;
    if (x != 4 || y != (void *)&u.v.v2) {
      abort();
    }
    v = z + w;
    v = 16384;
  }

  if (v != 16384) {
    abort();
  }
  return 0;
}
