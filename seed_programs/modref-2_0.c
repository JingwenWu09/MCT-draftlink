#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noinline)) void test(char *a) {
  __builtin_memset(a, 0, 321);
}
__attribute__((noinline)) void test2(double *x, double *y) {
  __builtin_modf(*x, y);
}
int main(void) {
  char array[321];
  double x = 1, y = 2;
  char arrayz[321];
  arrayz[0] = 1;
  test(array);
  test2(&x, &y);
  if (__builtin_constant_p(x == 2) && __builtin_constant_p(arrayz[0] == 1)) {
    __builtin_abort();
  }
  return 0;
}
