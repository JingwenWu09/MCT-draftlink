#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

short v = -1;

void __attribute__((noinline, noclone)) bar(int a) {
  if (a != -1) {
    abort();
  }
}

void __attribute__((noinline, noclone)) foo(short *a, int t) {
  short r = *a;

  if (t) {
    bar((unsigned short)r);
  } else {
    bar((signed short)r);
  }
}

int main(void) {
  foo(&v, 0);
  return 0;
}
