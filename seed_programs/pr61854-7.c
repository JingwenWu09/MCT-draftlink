#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int i = 0
#if 0

#else

          + 1
#endif
      ;
  if (i == 0) {
    __builtin_abort();
  }
  return 0;
}
