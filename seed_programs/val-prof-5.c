#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a[1000];
int b = 997;
int main() {
  int i;
  for (i = 0; i < 1000; i++) {
    if (a[i] != 1) {
      a[i] /= b;
    } else {
      a[i] /= b;
    }
  }
  return 0;
}
