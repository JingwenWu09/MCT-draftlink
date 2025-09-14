#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static unsigned long long q = 0;

__attribute__((noipa)) static void foo(void) {
  q = (q & ~0x1ffffffffULL) | 0x100000000ULL;
}

int main() {
  __asm volatile("" : "+m"(q));
  foo();
  if (q != 0x100000000ULL) {
    __builtin_abort();
  }
  return 0;
}
