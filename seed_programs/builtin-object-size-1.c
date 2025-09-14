#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __SIZE_TYPE__ size_t;
extern void abort(void);
extern void exit(int);
extern void *malloc(size_t);
extern void *calloc(size_t, size_t);
extern void *alloca(size_t);
extern void *memcpy(void *, const void *, size_t);
extern void *memset(void *, int, size_t);
extern char *strcpy(char *, const char *);

struct A {
  char a[10];
  int b;
  char c[10];
} y, w[4];

char exta[50];
char extb[30];
struct A zerol[0];

void __attribute__((noinline)) test1(void *q, int x) {
  struct A a;
  void *p = &a.a[3], *r;
  char var[x + 10];
  if (x < 0) {
    r = &a.a[9];
  } else {
    r = &a.c[1];
  }
  if (__builtin_object_size(p, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 3) {
    ;
  }
  if (__builtin_object_size(&a.c[9], 0) != sizeof(a) - __builtin_offsetof(struct A, c) - 9) {
    ;
  }
  if (__builtin_object_size(q, 0) != (size_t)-1) {
    ;
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 0 ? sizeof(a) - __builtin_offsetof(struct A, a) - 9 : sizeof(a) - __builtin_offsetof(struct A, c) - 1))
    ;
#else
  if (__builtin_object_size(r, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 9) {
    ;
  }
#endif
  if (x < 6) {
    r = &w[2].a[1];
  } else {
    r = &a.a[6];
  }
  if (__builtin_object_size(&y, 0) != sizeof(y)) {
    ;
  }
  if (__builtin_object_size(w, 0) != sizeof(w)) {
    ;
  }
  if (__builtin_object_size(&y.b, 0) != sizeof(a) - __builtin_offsetof(struct A, b)) {
    ;
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 6 ? 2 * sizeof(w[0]) - __builtin_offsetof(struct A, a) - 1 : sizeof(a) - __builtin_offsetof(struct A, a) - 6))
    ;
#else
  if (__builtin_object_size(r, 0) != 2 * sizeof(w[0]) - __builtin_offsetof(struct A, a) - 1) {
    ;
  }
#endif
  if (x < 20) {
    r = malloc(30);
  } else {
    r = calloc(2, 16);
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 20 ? 30 : 2 * 16))
    ;
#else

  if (__builtin_object_size(r, 0) != 2 * 16 && __builtin_object_size(r, 0) != 30) {
    ;
  }
#endif
  if (x < 20) {
    r = malloc(30);
  } else {
    r = calloc(2, 14);
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 20 ? 30 : 2 * 14))
    ;
#else
  if (__builtin_object_size(r, 0) != 30) {
    ;
  }
#endif
  if (x < 30) {
    r = malloc(sizeof(a));
  } else {
    r = &a.a[3];
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 30 ? sizeof(a) : sizeof(a) - 3))
    ;
#else
  if (__builtin_object_size(r, 0) != sizeof(a)) {
    ;
  }
#endif
  r = memcpy(r, "a", 2);
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 30 ? sizeof(a) : sizeof(a) - 3))
    ;
#else
  if (__builtin_object_size(r, 0) != sizeof(a)) {
    ;
  }
#endif
  r = memcpy(r + 2, "b", 2) + 2;
#ifdef __builtin_object_size
  if (__builtin_object_size(r, 0) != (x < 30 ? sizeof(a) - 4 : sizeof(a) - 7))
    ;
#else
  if (__builtin_object_size(r, 0) != sizeof(a) - 4) {
    ;
  }
#endif
  r = &a.a[4];
  r = memset(r, 'a', 2);
  if (__builtin_object_size(r, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 4) {
    ;
  }
  r = memset(r + 2, 'b', 2) + 2;
  if (__builtin_object_size(r, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 8) {
    ;
  }
  r = &a.a[1];
  r = strcpy(r, "ab");
  if (__builtin_object_size(r, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 1) {
    ;
  }
  r = strcpy(r + 2, "cd") + 2;
  if (__builtin_object_size(r, 0) != sizeof(a) - __builtin_offsetof(struct A, a) - 5) {
    ;
  }
  if (__builtin_object_size(exta, 0) != (size_t)-1) {
    ;
  }
  if (__builtin_object_size(exta + 10, 0) != (size_t)-1) {
    ;
  }
  if (__builtin_object_size(&exta[5], 0) != (size_t)-1) {
    ;
  }
  if (__builtin_object_size(extb, 0) != sizeof(extb)) {
    ;
  }
  if (__builtin_object_size(extb + 10, 0) != sizeof(extb) - 10) {
    ;
  }
  if (__builtin_object_size(&extb[5], 0) != sizeof(extb) - 5) {
    ;
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(var, 0) != x + 10)
    ;
  if (__builtin_object_size(var + 10, 0) != x)
    ;
  if (__builtin_object_size(&var[5], 0) != x + 5)
    ;
#else
  if (__builtin_object_size(var, 0) != (size_t)-1) {
    ;
  }
  if (__builtin_object_size(var + 10, 0) != (size_t)-1) {
    ;
  }
  if (__builtin_object_size(&var[5], 0) != (size_t)-1) {
    ;
  }
#endif
  if (__builtin_object_size(zerol, 0) != 0) {
    ;
  }
  if (__builtin_object_size(&zerol, 0) != 0) {
    ;
  }
  if (__builtin_object_size(&zerol[0], 0) != 0) {
    ;
  }
  if (__builtin_object_size(zerol[0].a, 0) != 0) {
    ;
  }
  if (__builtin_object_size(&zerol[0].a[0], 0) != 0) {
    ;
  }
  if (__builtin_object_size(&zerol[0].b, 0) != 0) {
    ;
  }
  if (__builtin_object_size("abcdefg", 0) != sizeof("abcdefg")) {
    ;
  }
  if (__builtin_object_size("abcd\0efg", 0) != sizeof("abcd\0efg")) {
    ;
  }
  if (__builtin_object_size(&"abcd\0efg", 0) != sizeof("abcd\0efg")) {
    ;
  }
  if (__builtin_object_size(&"abcd\0efg"[0], 0) != sizeof("abcd\0efg")) {
    ;
  }
  if (__builtin_object_size(&"abcd\0efg"[4], 0) != sizeof("abcd\0efg") - 4) {
    ;
  }
  if (__builtin_object_size(&"abcd\0efg"[5], 0) != sizeof("abcd\0efg") - 5) {
    ;
  }
  if (__builtin_object_size(L"abcdefg", 0) != sizeof(L"abcdefg")) {
    ;
  }
  r = (char *)L"abcd\0efg";
  if (__builtin_object_size(r + 2, 0) != sizeof(L"abcd\0efg") - 2) {
    ;
  }
}

size_t l1 = 1;

void __attribute__((noinline)) test2(void) {
  struct B {
    char buf1[10];
    char buf2[10];
  } a;
  char *r, buf3[20];
  int i;
  size_t res;

  if (sizeof(a) != 20) {
    return;
  }

  r = buf3;
  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1) {
      r = &a.buf1[1];
    } else if (i == l1) {
      r = &a.buf2[7];
    } else if (i == l1 + 1) {
      r = &buf3[5];
    } else if (i == l1 + 2) {
      r = &a.buf1[9];
    }
  }
#ifdef __builtin_object_size
  res = sizeof(buf3);

  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 1;
    else if (i == l1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf2) - 7;
    else if (i == l1 + 1)
      res = sizeof(buf3) - 5;
    else if (i == l1 + 2)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 9;
  }
#else
  res = 20;
#endif
  if (__builtin_object_size(r, 0) != res) {
    ;
  }
  r = &buf3[20];
  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1) {
      r = &a.buf1[7];
    } else if (i == l1) {
      r = &a.buf2[7];
    } else if (i == l1 + 1) {
      r = &buf3[5];
    } else if (i == l1 + 2) {
      r = &a.buf1[9];
    }
  }
#ifdef __builtin_object_size
  res = sizeof(buf3) - 20;

  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 7;
    else if (i == l1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf2) - 7;
    else if (i == l1 + 1)
      res = sizeof(buf3) - 5;
    else if (i == l1 + 2)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 9;
  }
  if (__builtin_object_size(r, 0) != res)
    ;
#else
  res = 15;
#endif
  if (__builtin_object_size(r, 0) != res) {
    ;
  }
  r += 8;
#ifdef __builtin_object_size
  res -= 8;
  if (__builtin_object_size(r, 0) != res)
    ;
  if (res >= 6) {
    if (__builtin_object_size(r + 6, 0) != res - 6)
      ;
  } else if (__builtin_object_size(r + 6, 0) != 0)
    ;
#else
  if (__builtin_object_size(r, 0) != 7) {
    ;
  }
  if (__builtin_object_size(r + 6, 0) != 1) {
    ;
  }
#endif
  r = &buf3[18];
  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1) {
      r = &a.buf1[9];
    } else if (i == l1) {
      r = &a.buf2[9];
    } else if (i == l1 + 1) {
      r = &buf3[5];
    } else if (i == l1 + 2) {
      r = &a.buf1[4];
    }
  }
#ifdef __builtin_object_size
  res = sizeof(buf3) - 18;

  for (i = 0; i < 4; ++i) {
    if (i == l1 - 1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 9;
    else if (i == l1)
      res = sizeof(a) - __builtin_offsetof(struct B, buf2) - 9;
    else if (i == l1 + 1)
      res = sizeof(buf3) - 5;
    else if (i == l1 + 2)
      res = sizeof(a) - __builtin_offsetof(struct B, buf1) - 4;
  }
  if (res >= 12) {
    if (__builtin_object_size(r + 12, 0) != res - 12)
      ;
  } else if (__builtin_object_size(r + 12, 0) != 0)
    ;
#else
  if (__builtin_object_size(r + 12, 0) != 4) {
    ;
  }
#endif
}

void __attribute__((noinline)) test3(void) {
  char buf4[10];
  struct B {
    struct A a[2];
    struct A b;
    char c[4];
    char d;
    double e;
    _Complex double f;
  } x;
  double y;
  _Complex double z;
  double *dp;

  if (__builtin_object_size(buf4, 0) != sizeof(buf4)) {
    ;
  }
  if (__builtin_object_size(&buf4, 0) != sizeof(buf4)) {
    ;
  }
  if (__builtin_object_size(&buf4[0], 0) != sizeof(buf4)) {
    ;
  }
  if (__builtin_object_size(&buf4[1], 0) != sizeof(buf4) - 1) {
    ;
  }
  if (__builtin_object_size(&x, 0) != sizeof(x)) {
    ;
  }
  if (__builtin_object_size(&x.a, 0) != sizeof(x)) {
    ;
  }
  if (__builtin_object_size(&x.a[0], 0) != sizeof(x)) {
    ;
  }
  if (__builtin_object_size(&x.a[0].a, 0) != sizeof(x)) {
    ;
  }
  if (__builtin_object_size(&x.a[0].a[0], 0) != sizeof(x)) {
    ;
  }
  if (__builtin_object_size(&x.a[0].a[3], 0) != sizeof(x) - 3) {
    ;
  }
  if (__builtin_object_size(&x.a[0].b, 0) != sizeof(x) - __builtin_offsetof(struct A, b)) {
    ;
  }
  if (__builtin_object_size(&x.a[1].c, 0) != sizeof(x) - sizeof(struct A) - __builtin_offsetof(struct A, c)) {
    ;
  }
  if (__builtin_object_size(&x.a[1].c[0], 0) != sizeof(x) - sizeof(struct A) - __builtin_offsetof(struct A, c)) {
    ;
  }
  if (__builtin_object_size(&x.a[1].c[3], 0) != sizeof(x) - sizeof(struct A) - __builtin_offsetof(struct A, c) - 3) {
    ;
  }
  if (__builtin_object_size(&x.b, 0) != sizeof(x) - __builtin_offsetof(struct B, b)) {
    ;
  }
  if (__builtin_object_size(&x.b.a, 0) != sizeof(x) - __builtin_offsetof(struct B, b)) {
    ;
  }
  if (__builtin_object_size(&x.b.a[0], 0) != sizeof(x) - __builtin_offsetof(struct B, b)) {
    ;
  }
  if (__builtin_object_size(&x.b.a[3], 0) != sizeof(x) - __builtin_offsetof(struct B, b) - 3) {
    ;
  }
  if (__builtin_object_size(&x.b.b, 0) != sizeof(x) - __builtin_offsetof(struct B, b) - __builtin_offsetof(struct A, b)) {
    ;
  }
  if (__builtin_object_size(&x.b.c, 0) != sizeof(x) - __builtin_offsetof(struct B, b) - __builtin_offsetof(struct A, c)) {
    ;
  }
  if (__builtin_object_size(&x.b.c[0], 0) != sizeof(x) - __builtin_offsetof(struct B, b) - __builtin_offsetof(struct A, c)) {
    ;
  }
  if (__builtin_object_size(&x.b.c[3], 0) != sizeof(x) - __builtin_offsetof(struct B, b) - __builtin_offsetof(struct A, c) - 3) {
    ;
  }
  if (__builtin_object_size(&x.c, 0) != sizeof(x) - __builtin_offsetof(struct B, c)) {
    ;
  }
  if (__builtin_object_size(&x.c[0], 0) != sizeof(x) - __builtin_offsetof(struct B, c)) {
    ;
  }
  if (__builtin_object_size(&x.c[1], 0) != sizeof(x) - __builtin_offsetof(struct B, c) - 1) {
    ;
  }
  if (__builtin_object_size(&x.d, 0) != sizeof(x) - __builtin_offsetof(struct B, d)) {
    ;
  }
  if (__builtin_object_size(&x.e, 0) != sizeof(x) - __builtin_offsetof(struct B, e)) {
    ;
  }
  if (__builtin_object_size(&x.f, 0) != sizeof(x) - __builtin_offsetof(struct B, f)) {
    ;
  }
  dp = &__real__ x.f;
  if (__builtin_object_size(dp, 0) != sizeof(x) - __builtin_offsetof(struct B, f)) {
    ;
  }
  dp = &__imag__ x.f;
  if (__builtin_object_size(dp, 0) != sizeof(x) - __builtin_offsetof(struct B, f) - sizeof(x.f) / 2) {
    ;
  }
  dp = &y;
  if (__builtin_object_size(dp, 0) != sizeof(y)) {
    ;
  }
  if (__builtin_object_size(&z, 0) != sizeof(z)) {
    ;
  }
  dp = &__real__ z;
  if (__builtin_object_size(dp, 0) != sizeof(z)) {
    ;
  }
  dp = &__imag__ z;
  if (__builtin_object_size(dp, 0) != sizeof(z) / 2) {
    ;
  }
}

struct S {
  unsigned int a;
};

void __attribute__((noinline)) test5(size_t x) {
  char buf[64];
  char *p = &buf[8];
  size_t i;

  for (i = 0; i < x; ++i) {
    p = p + 4;
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(p, 0) != sizeof(buf) - 8 - 4 * x)
    ;
#else

  if (__builtin_object_size(p, 0) != sizeof(buf) - 8) {
    ;
  }
#endif
  memset(p, ' ', sizeof(buf) - 8 - 4 * 4);
}

void __attribute__((noinline)) test6(size_t x) {
  struct T {
    char buf[64];
    char buf2[64];
  } t;
  char *p = &t.buf[8];
  size_t i;

  for (i = 0; i < x; ++i) {
    p = p + 4;
  }
#ifdef __builtin_object_size
  if (__builtin_object_size(p, 0) != sizeof(t) - 8 - 4 * x)
    ;
#else
  if (__builtin_object_size(p, 0) != sizeof(t) - 8) {
    ;
  }
#endif
  memset(p, ' ', sizeof(t) - 8 - 4 * 4);
}

void __attribute__((noinline)) test7(void) {
  char buf[64];
  struct T {
    char buf[64];
    char buf2[64];
  } t;
  char *p = &buf[64], *q = &t.buf[64];

  if (__builtin_object_size(p + 64, 0) != 0) {
    ;
  }
  if (__builtin_object_size(q + 63, 0) != sizeof(t) - 64 - 63) {
    ;
  }
  if (__builtin_object_size(q + 64, 0) != sizeof(t) - 64 - 64) {
    ;
  }
  if (__builtin_object_size(q + 256, 0) != 0) {
    ;
  }
}

void __attribute__((noinline)) test8(void) {
  struct T {
    char buf[10];
    char buf2[10];
  } t;
  char *p = &t.buf2[-4];
  char *q = &t.buf2[0];
  if (__builtin_object_size(p, 0) != sizeof(t) - 10 + 4) {
    ;
  }
  if (__builtin_object_size(q, 0) != sizeof(t) - 10) {
    ;
  }

  q = q - 8;
  if (__builtin_object_size(q, 0) != (size_t)-1 && __builtin_object_size(q, 0) != sizeof(t) - 10 + 8) {
    ;
  }
  p = &t.buf[-4];
  if (__builtin_object_size(p, 0) != 0) {
    ;
  }
}

void __attribute__((noinline)) test9(unsigned cond) {
  char *buf2 = malloc(10);
  char *p;

  if (cond) {
    p = &buf2[8];
  } else {
    p = &buf2[4];
  }

#ifdef __builtin_object_size
  if (__builtin_object_size(&p[-4], 0) != (cond ? 6 : 10))
    ;
#else
  if (__builtin_object_size(&p[-4], 0) != 10) {
    ;
  }
#endif

  for (unsigned i = cond; i > 0; i--) {
    p--;
  }

#ifdef __builtin_object_size
  if (__builtin_object_size(p, 0) != ((cond ? 2 : 6) + cond))
    ;
#else
  if (__builtin_object_size(p, 0) != 10) {
    ;
  }
#endif

  p = &y.c[8];
  for (unsigned i = cond; i > 0; i--) {
    p--;
  }

#ifdef __builtin_object_size
  if (__builtin_object_size(p, 0) != sizeof(y) - __builtin_offsetof(struct A, c) - 8 + cond)
    ;
#else
  if (__builtin_object_size(p, 0) != sizeof(y)) {
    ;
  }
#endif
}

void __attribute__((noinline)) test10(void) {
  static char buf[255];
  unsigned int i, len = sizeof(buf);
  char *p = buf;

  for (i = 0; i < sizeof(buf); i++) {
    if (len < 2) {
#ifdef __builtin_object_size
      if (__builtin_object_size(p - 3, 0) != sizeof(buf) - i + 3)
        ;
#else
      if (__builtin_object_size(p - 3, 0) != sizeof(buf)) {
        ;
      }
#endif
      break;
    }
    p++;
    len--;
  }
}

int main(void) {
  struct S s[10];
  __asm("" : "=r"(l1) : "0"(l1));
  test1(main, 6);
  test2();
  test3();

  char *x = (char *)s;
  int y = 10;
  register int i;
  struct A *p;

  for (i = 0; i < y; i++) {
    p = (struct A *)x;
    x = (char *)&p[1];
    if (__builtin_object_size(p, 0) != (size_t)-1) {
      ;
    }
  }

  test5(4);
  test6(4);
  test7();
  test8();
  test9(1);
  test10();
  exit(0);
}
