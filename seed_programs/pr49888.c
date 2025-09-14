#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int v __attribute__((used));

static void __attribute__((noipa)) f(int *p) {
  int c = *p;
  v = c;
  *p = 1;

  v = 0;
}
int main() {
  int a = 0;
  f(&a);
  return 0;
}
