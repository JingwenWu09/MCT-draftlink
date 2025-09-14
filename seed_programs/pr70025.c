#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef char (*F)(unsigned long, void *);
union B{
  struct A {
    char a1, a2, a3, a4;
    unsigned long a5;
    F a6;
    void *a7;
  } b;
  char c[1];
};
struct C {
  const char *c1;
  unsigned long c2;
};
struct D {
  unsigned long d1;
  int d2;
  const char *d3;
  unsigned long d4, d5;
  struct C d6[49];
  char d7[8];
} ;

__attribute__((noinline, noclone)) void foo(struct D p[1]) {
  asm volatile("" : : "r"(p) : "memory");
}

char baz(unsigned long i) {
  return (char)i;
}

int main() {
  struct D p;
  struct A f;
  __builtin_memset(&f, 0, sizeof(f));
  f.a2 = 4;
  f.a5 = 13;
  f.a6 = baz;
  __builtin_memset(&p, 0, sizeof(p));
  p.d6[0].c1 = (const char *)&f;

  unsigned long k = p.d1 + 1;
  struct C *l = &p.d6[p.d2];
  const char *m = l->c1;
  p.d1 = k;
  if (*m == '\0') {
    struct A *f = &((union B *)m)->b;
    unsigned long n = l->c2;
    unsigned long o = n + f->a5;
    if (k < o) {
      unsigned long i;
      unsigned long q = k + 8;
      F a6 = f->a6;
      void *a7 = f->a7;
      if (q > o) {
        q = o;
      }
      for (i = k; i < q; i++) {
        p.d7[i - k] = (*a6)(i - n, a7);
      }
      p.d4 = k;
      p.d3 = p.d7;
      p.d5 = q;
      if (p.d4 != 1 || p.d5 != 9 || p.d3 != p.d7) {
				__builtin_abort();
			}
    }
  }
  while (p.d2 > 0 && l[0].c2 != l[-1].c2) {
    p.d2--;
    l--;
  }
  if (p.d2 == 0) {
    p.d2 = 0x55555555;
    if (p.d4 != 1 || p.d5 != 9 || p.d3 != p.d7) {
			__builtin_abort();
		}
  }
  p.d2--;
  foo(&p);

  if (p.d4 != 1 || p.d5 != 9 || p.d3 != p.d7) {
    __builtin_abort();
  }
  return 0;
}
