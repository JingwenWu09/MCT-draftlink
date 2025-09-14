#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  void *p;
  int x;

  x = 2;
  p = &x;

  if (*((int *)p) != 2) {
    return 1;
  }
  return 0;
}
