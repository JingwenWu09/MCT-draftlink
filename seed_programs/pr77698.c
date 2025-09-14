#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile long int g;
volatile long int j = 0;

void foo(long int *a, long int *b, long int n) {
  long int i;

  for (i = 0; i < n; i++) {
    a[j] = *b;
  }
}

long int a, b;
int main() {
  a = 1;
  b = 2;
  foo(&a, &b, 1000000);
  g = a + b;
  return 0;
}
