#include <limits.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

int test(int x) {
  return (2 * x) / 2;
}

int main() {
  int x = INT_MAX;

  if (test(x) != x) {
    abort();
  }
  return 0;
}
