#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  int x;
  int *p;

  x = 0;
  p = &x;
  return p[0];
}
