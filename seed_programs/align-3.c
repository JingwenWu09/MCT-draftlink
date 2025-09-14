#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void func(void) __attribute__((aligned(256)));

void func(void) {
}

int main() {
  if (__alignof__(func) != 256) {
    abort();
  }
  return 0;
}
