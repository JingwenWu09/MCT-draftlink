#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

#define MIN(x, y) ((x) < (y) ? (x) : (y))
#define MAX(x, y) ((x) > (y) ? (x) : (y))

int f1(int a, int b) {
  return MIN(MAX(a, b), b);
}

int f2(int a, int b) {
  return MAX(MIN(a, b), b);
}

int f3(int a, int b) {
  return MIN(MAX(b, a), b);
}

int f4(int a, int b) {
  return MAX(MIN(b, a), b);
}

int g1(int a, int b) {
  return MIN(a, MAX(a, b));
}

int g2(int a, int b) {
  return MAX(a, MIN(a, b));
}

int g3(int a, int b) {
  return MIN(a, MAX(b, a));
}

int g4(int a, int b) {
  return MAX(a, MIN(b, a));
}

int main(void) {
  if (f1(1, 2) != 2) {
    abort();
  }

  if (f2(1, 2) != 2) {
    abort();
  }

  if (f3(1, 2) != 2) {
    abort();
  }

  if (f4(1, 2) != 2) {
    abort();
  }

  if (g1(1, 2) != 1) {
    abort();
  }

  if (g2(1, 2) != 1) {
    abort();
  }

  if (g3(1, 2) != 1) {
    abort();
  }

  if (g4(1, 2) != 1) {
    abort();
  }

  return 0;
}
