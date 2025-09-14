#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int v;

__attribute__((noipa)) void bar(const char *x, const char *y, int z) {
  if (!v) {
    __builtin_abort();
  }
  asm volatile("" : "+g"(x));
  asm volatile("" : "+g"(y));
  asm volatile("" : "+g"(z));
}

typedef struct {
  unsigned M1;
  unsigned M2 : 1;
  int : 0;
  unsigned M3 : 1;
} S;

S foo() {
  S result = {0, 0, 1};
  return result;
}

int main() {
  S ret = foo();
  (ret.M2 == 0) ? (void)0 : bar("ret.M2 == 0", __FILE__, __LINE__);
  (ret.M3 == 1) ? (void)0 : bar("ret.M3 == 1", __FILE__, __LINE__);
  return 0;
}
