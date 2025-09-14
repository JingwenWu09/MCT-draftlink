#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  __complex double dc;
  double *dp = &(__real dc);
  *dp = 3.14;
  if ((__real dc) != 3.14) {
    abort();
  }
  exit(0);
}
