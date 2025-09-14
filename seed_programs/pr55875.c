#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int a[251];
__attribute__((noinline)) t(int i) {
  if (i == 0) {
    exit(0);
  }
  if (i > 255) {
    abort();
  }
}
main() {
  unsigned int i;
  for (i = 0;; i++) {
    a[i] = t((unsigned char)(i + 5));
  }
}
