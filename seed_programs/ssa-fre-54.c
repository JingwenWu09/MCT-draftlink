#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

union U {
  int i;
  char c[4];
  short s[2];
};

char __attribute__((noinline, noclone)) foo(int i) {
  union U u;
  u.i = i;

#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
  return u.c[0];
#elif __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__
  return u.c[3];
#else
  return 0x04;
#endif
}

short __attribute__((noinline, noclone)) baz(int i) {
  union U u;
  u.i = i;

#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
  return u.s[0];
#elif __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__
  return u.s[1];
#else
  return 0x0304;
#endif
}

char __attribute__((noinline, noclone)) bar(int j) {
  union U u;
  u.i = j;

  return u.c[2];
}

int main() {
  if (foo(0x01020304) != 0x04) {
    abort();
  }
  if (baz(0x01020304) != 0x0304) {
    abort();
  }
  return 0;
}
