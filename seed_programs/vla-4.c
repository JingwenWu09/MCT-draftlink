#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int i;

int foo(int n) {
  int(*t)[n];
  i = 0;
  int j = 0;
  char b[1][n + 3];
  int d[3][n];
  sizeof(b[i++ + sizeof(j++)]);

  if (i != 1 || j != 0) {
    return 1;
  }
  __typeof__(b[i++]) c1;
  if (i != 2) {
    return 1;
  }
  __typeof__(t + (i++, 0)) c2;
  if (i != 3) {
    return 1;
  }
  __typeof__(i + (i++, 0)) c3;
  if (i != 3) {
    return 1;
  }
  sizeof(d[i++]);
  if (i != 4) {
    return 1;
  }
  __alignof__(__typeof__(t + (i++, 0)));

  if (i != 4) {
    return 1;
  }
  sizeof(__typeof__(t + (i++, 0)));

  if (i != 4) {
    return 1;
  }
  return 0;
}

int foo6(int a, int b[a][a], int (*c)[sizeof(*b)]) {
  return sizeof(*c);
}

int main() {
  int b[10][10];
  int(*c)[sizeof(int) * 10];
  if (foo6(10, b, c) != 10 * sizeof(int) * sizeof(int)) {
    return 1;
  }
  return foo(10);
}
