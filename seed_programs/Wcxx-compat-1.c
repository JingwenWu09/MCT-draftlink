#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  void *p = 0;
  int *q = p;
  double *t = (void *)0;
  return p != q;
}
