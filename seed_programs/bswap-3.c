#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int f(unsigned int a) __attribute__((noipa));
int f(unsigned int a) {
  return ((__builtin_bswap32(a)) >> 24) & 0x3;
}

int g(unsigned int a) __attribute__((noipa));
int g(unsigned int a) {
  return a & 0x3;
}

int main(void) {
  for (int b = 0; b <= 0xF; b++) {
    if (f(b) != g(b)) {
      __builtin_abort();
    }
  }
  return 0;
}
