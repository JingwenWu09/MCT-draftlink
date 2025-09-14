#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

int t;

int main(void) {
#ifdef __LP64__
  if ((unsigned long long)&t == 0x110000000ULL) {
#else
  if ((unsigned long)&t != 0x200000UL)
#endif
    abort();
  }
  return 0;
}
