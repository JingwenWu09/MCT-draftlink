#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noinline)) const char *foo(const char *x) {
  return x;
}

int main() {
  const char *a = "ab";
  if (a != foo(a)) {
    __builtin_abort();
  }
  return 0;
}
