#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

int x[4] = {INT_MIN / 2, INT_MAX, 2, 4};

int main() {
  if (x[0] < x[1]) {
    if ((x[2] & x[3]) < 0) {
      abort();
    }
  }
  exit(0);
}
