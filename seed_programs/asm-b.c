#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

unsigned short v = 0x0300;

void foo(unsigned short *p) {
  *p = v;
}

int bar(void) {
  unsigned short x;
  volatile unsigned short *z;
  foo(&x);
  const unsigned int y = x;
  z = &x;
#if defined(__powerpc__) || defined(__PPC__) || defined(__ppc__) || defined(_POWER) || defined(__ppc64__) || defined(__ppc)
  __asm __volatile("sthbrx %1,0,%2" : "=m"(*z) : "r"(y), "r"(z));
#elif defined __i386__ || defined __x86_64__
  __asm __volatile("movb %b1,1(%2)\n\tmovb %h1,(%2)" : "=m"(*z) : "Q"(y), "R"(z));
#endif
  return (x & 1) == 0;
}

int main(void) {
  if (bar()) {
    abort();
  }
  return 0;
}
