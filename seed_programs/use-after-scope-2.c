#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int *bar(int *x, int *y) {
  return y;
}

int foo(void) {
  char *p;
  char a = 0;
  p = &a;

  if (*p) {
    return 1;
  } else {
    return 0;
  }
}

int main(void) {
  char *ptr;
  char my_char[9];
  ptr = &my_char[0];

  int a[16];
  int *p, *q = a;
  int b[16];
  p = bar(a, b);
  bar(a, q);
  int c[16];
  q = bar(a, c);
  int v = *bar(a, q);
  return v;
}
