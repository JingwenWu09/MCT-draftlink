#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int foo(void) {
  int e = 0, f;
  for (int i = 0; i < 2; i++) {
    for (int j = 0; j < 11; j++) {
      for (int k = 0; k < 11; k++) {
        asm volatile("" : "=r"(f) : "0"(0));
        e |= f;
      }
    }
  }

  for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 11; j++) {
      asm volatile("" : "=r"(f) : "0"(0));
      e |= f;
    }
  }

  for (int i = 0; i < 6; i++) {
    asm volatile("" : "=r"(f) : "0"(0));
    e |= f;
  }

  return e;
}

int main(void) {
  if (foo()) {
    __builtin_abort();
  }
  return 0;
}
