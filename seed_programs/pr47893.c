#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

struct S {
  unsigned s1 : 4, s2 : 2, s3 : 2, s4 : 2, s5 : 2, s6 : 1, s7 : 1, s8 : 1, s9 : 1, s10 : 1;
  int s11 : 16;
  unsigned s12 : 4;
  int s13 : 16;
  unsigned s14 : 2;
  int s15 : 16;
  unsigned s16 : 4;
  int s17 : 16;
  unsigned s18 : 2;
};

struct T {
  unsigned t[3];
};

struct U {
  unsigned u1, u2;
};

struct V;

struct W {
  char w1[24];
  struct V *w2;
  unsigned w3;
  char w4[28912];
  unsigned int w5;
  char w6[60];
};

struct X {
  unsigned int x[2];
};

struct V {
  int v1;
  struct X v2[3];
  char v3[28];
};

struct Y {
  void *y1;
  char y2[3076];
  struct T y3[32];
  char y4[1052];
};

volatile struct S v1 = {.s15 = -1, .s16 = 15, .s17 = -1, .s18 = 3};
volatile int cnt;
volatile struct U v2;
volatile struct T v3;
volatile int ii;
const char *volatile p = "";

__attribute__((noinline, noclone)) int fn1(int x) {
  int r;
  __asm__ volatile("" : "=r"(r) : "0"(1), "r"(x) : "memory");
  return r;
}


__attribute__((noinline, noclone))
#ifdef __i386__
__attribute__((regparm(2)))
#endif
struct S
fn2(struct Y *x, const struct X *y) {
  if (++cnt > 1) {
    abort();
  }
  __asm__ volatile("" : : "r"(x), "r"(y) : "memory");
  return v1;
}

__attribute__((noinline, noclone)) void fn3(void *x, unsigned y, const struct S *z, unsigned w) {
  __asm__ volatile("" : : "r"(x), "r"(y), "r"(z), "r"(w) : "memory");
}

__attribute__((noinline, noclone)) struct U fn4(void *x, unsigned y) {
  __asm__ volatile("" : : "r"(x), "r"(y) : "memory");
  return v2;
}

__attribute__((noinline, noclone)) struct S fn5(void *x) {
  __asm__ volatile("" : : "r"(x) : "memory");
  return v1;
}

__attribute__((noinline, noclone)) struct T fn6(void *x) {
  __asm__ volatile("" : : "r"(x) : "memory");
  return v3;
}

__attribute__((noinline, noclone)) struct T fn7(void *x, unsigned y, unsigned z) {
  __asm__ volatile("" : : "r"(x), "r"(y), "r"(z) : "memory");
  return v3;
}

static inline void fn9(void *x, struct S y __attribute__((unused))) {
  fn4(x, 8);
}

static void fn10(struct Y *x) {
  void *a = x->y1;
  struct T b __attribute__((unused)) = fn6(a);
  fn9(a, fn5(a));
}

int main() {
  struct V vi = {.v1 = 0};
  struct W wi = {.w5 = 1, .w2 = &vi};
  unsigned int x = ii + 1;
  void *y = (void *)p;
  const struct W *z = &wi;
  unsigned int w = ii;
  const char *v = (const char *)p;
  const char *u = (const char *)p;
  struct Y a, *t;
  unsigned i;
  t = &a;
  __builtin_memset(t, 0, sizeof *t);
  t->y1 = y;
  if (x == 0) {
    if (z->w3 & 1) {
      fn10(t);
    }
    for (i = 0; i < w; i++) {
      if (v[i] == 0) {
        t->y3[i] = fn7(y, 0, u[i]);
      } else {
        return 0;
      }
    }
  } else {
    for (i = 0; i < w; i++) {
      t->y3[i] = fn7(y, v[i], u[i]);
    }
  }
  for (i = 0; i < z->w5; i++) {
    struct Y *xs = t;
    const struct V *ys = &z->w2[i];
    void *as = xs->y1;
    struct S bs[4];
    unsigned is, cs;
    cs = fn1(ys->v1);
    for (is = 0; is < cs; is++) {
      bs[is] = fn2(xs, &ys->v2[is]);
    }
    fn3(as, ys->v1, bs, cs);
  }

  if (cnt != 1) {
    abort();
  }
  return 0;
}
