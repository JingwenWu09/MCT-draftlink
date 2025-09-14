#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  char a[16], b[16];

  if (sizeof(a) != sizeof(b)) {
    return 1;
  }
  return 0;
}
