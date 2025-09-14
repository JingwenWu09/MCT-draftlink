#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct {
  int a : 1;
} b;
int *c = (int *)&b, d;
int main() {
  d = c && (b.a = (d < 0) ^ 3);
  if (d != 1) {
    __builtin_abort();
  }
  return 0;
}
