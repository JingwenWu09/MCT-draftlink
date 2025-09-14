#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <assert.h>
#include <limits.h>

__attribute__((noinline)) int foo(int *a, int n) {
  int min = 999999;
  int bla = 0;
  for (int i = 0; i < n; i++) {
    if (a[i] < min) {
      min = a[i];
      bla = 1;
    }
  }

  if (bla) {
    min += 1;
  }
  return min;
}

int main() {
  int a[] = {2, 1, -13, INT_MAX, INT_MIN, 0};

  int res = foo(a, sizeof(a) / sizeof(a[0]));

  assert(res == (INT_MIN + 1));
}
