#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int a[] = {1, 2, 3, 4};

int main() {
  if (sizeof(a) != 4 * sizeof(int)) {
    return 1;
  }

  return 0;
}
