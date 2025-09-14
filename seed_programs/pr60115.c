#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a, b[2];

int main() {
lbl:
  for (; a; a--) {
    if (b[10000]) {
      goto lbl;
    }
  }

  return 0;
}
