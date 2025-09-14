#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static const char *const multilib_exclusions_raw[] = {0};

void __attribute__((noinline)) f(char *const *p) {
  __asm__("" : : "g"(p) : "memory");
}

void g(char **o) {
  const char *const *q = multilib_exclusions_raw;

  f(o);
  while (*q++) {
    f(o);
  }
}

int main() {
  return 0;
}
