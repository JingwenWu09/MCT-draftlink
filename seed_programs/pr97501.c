#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static int c = 0;

int main() {
  int b = 0;
  if (c) {
    for (;; b--) {
      do {
        b++;
      } while (b);
    }
  }
}
