#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define N 8

int main(int argc, char **argv) {
  int a[N];
  for (int i = 0; i < N; i++) {
    a[i] = 2 * i + 1;
  }
  int *p = &a[0];
  __builtin_printf("%d\n", a[argc]);
  return *(++p);
}
