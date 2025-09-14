#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static double a[10][10], b[10][10], c[10][10], d[10][10];
static double e[10];
static int f[10];

void __attribute__((noinline)) fn1(void) {
  asm volatile("" : : : "memory");
}

void __attribute__((noinline)) fn2(int x, ...) {
  asm volatile("" : : "r"(x) : "memory");
}

static void bar(double v, double w, double x, double y, double z) {
  double a;
  if (v / w < 200.0) {
    a = x + (y - x) * __builtin_exp(-v / w);
    fn2(0);
    fn2(1, a * 20.2 / z, z);
    fn1();
  }
}

void __attribute__((noinline)) test(int j, int k, double q) {
  int i = f[k];
  if (a[i][j] > 0.0) {
    bar(q, d[i][j], b[i][j], c[i][j], e[i]);
  }
}

int main(void) {
  d[0][6] = 1.0;
  a[0][6] = 2.0;
  test(6, 7, 400.0);
  return 0;
}
