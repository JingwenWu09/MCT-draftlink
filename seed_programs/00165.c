#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define FRED 12
#define BLOGGS(x) (12 * (x))

int main() {
  printf("%d\n", FRED);
  printf("%d, %d, %d\n", BLOGGS(1), BLOGGS(2), BLOGGS(3));

  return 0;
}
