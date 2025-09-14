#include <math.h>
#include <stdio.h>
#include <stdlib.h>
__attribute__((noinline)) int mulv(int a, int b) {
  return a * b;
}

int main() {
  mulv(0, 0);
  mulv(0, -1);
  mulv(-1, 0);
  mulv(-1, -1);
  return 0;
}
