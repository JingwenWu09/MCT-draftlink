#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a = 0, b = 0, c = 0;
struct S {
  signed m : 7;
  signed e : 2;
};
struct S f[2] = {{0, 0}, {0, 0}};
struct S g = {0, 0};

void __attribute__((noinline)) k() {
  for (; c <= 1; c++) {
    f[b] = g;
    f[b].e ^= 1;
  }
}
int main() {
  k();
  if (f[b].e != 1) {
    __builtin_abort();
  }
}
