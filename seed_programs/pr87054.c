#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#ifndef T
#if __SIZEOF_INT128__ && defined __SSE__
#define T __int128
#else
#define T long
#endif
#endif
#ifndef R
#ifdef __SSE__
#define R "x"
#else
#define R "r"
#endif
#endif

typedef T A;
struct B {
  char d;
  A c;
} __attribute__((packed));
struct B b[50];

int main() {
  int i;
  for (i = 0; i < sizeof(b) / sizeof(*b); i++) {
    asm("" : "+" R(b[i].c));
  }
}
