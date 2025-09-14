#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  int x;
  int *p;

  x = 1;
  p = &x;
  p[0] = 0;
  return x;
}
