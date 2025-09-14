#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int *p;
  int i;

  p = &i;
  printf("%d\n", p[0]);

  return 0;
}
