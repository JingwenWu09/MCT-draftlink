#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);
#if __INT_MAX__ >= 0x7fffffff
volatile unsigned u = 0x80000081;
#else
volatile unsigned long u = 0x80000081;
#endif
volatile float f1, f2;
int main(void) {
  f1 = (float)u;
  f2 = (float)0x80000081;
  if (f1 != f2) {
    abort();
  }
  exit(0);
}
