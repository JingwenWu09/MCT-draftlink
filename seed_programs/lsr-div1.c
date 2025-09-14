#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#define NOINLINE __attribute__((noinline))

static NOINLINE int f1(unsigned int n) {
  return n % 0x33;
}

static NOINLINE int f2(unsigned int n) {
  return n % 0x12;
}

int main() {
  int a = 0xaaaaaaaa;
  int b = 0x55555555;
  int c;
  c = f1(a);
  if (c != 0x11) {
    abort();
  }
  c = f1(b);
  if (c != 0x22) {
    abort();
  }
  c = f2(a);
  if (c != 0xE) {
    abort();
  }
  c = f2(b);
  if (c != 0x7) {
    abort();
  }
  return 0;
}
