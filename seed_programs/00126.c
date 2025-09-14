#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  int x;

  x = 3;
  x = !x;
  x = !x;
  x = ~x;
  x = -x;
  if (x != 2) {
    return 1;
  }
  return 0;
}
