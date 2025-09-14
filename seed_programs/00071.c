#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define X 1
#undef X

#ifdef X
FAIL
#endif

    int
    main() {
  return 0;
}
