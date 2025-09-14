#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  if ((_Bool)(__INT_MAX__ + 1)) {
    return 1;
  } else {
    return 0;
  }
}
