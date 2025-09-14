#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);
int main() {
  int a[2];
  int *p = a;
  int i;
  a[0] = 1;
  a[1] = 2;
  for (i = 0; i < 2; ++i) {
    if (p[i] != i + 1) {
      abort();
    }
  }
  return 0;
}
