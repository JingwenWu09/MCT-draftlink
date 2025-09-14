#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  if (0 ? 1 : 0) {
    return 1;
  }
  if (1 ? 0 : 1) {
    return 2;
  }
  return 0;
}
