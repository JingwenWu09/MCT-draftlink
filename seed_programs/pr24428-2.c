#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__thread double thrtest[81];
int main() {
  double *p, *e;
  e = &thrtest[81];
  for (p = &thrtest[0]; p < e; ++p) {
    *p = 1.0;
  }
  return 0;
}
