#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a = 0;

int main() {
  for (;; a++) {
    int c[1];
    if (a) {
      break;
      a;
      continue;
    }
    continue;
  }

  return 0;
}
