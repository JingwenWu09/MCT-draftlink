#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

main() {
  if (0) {
    abort();
  } else {
    return 0;
  }
}
