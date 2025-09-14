#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

int main(void) {
#ifdef FOO
  abort();
#else
  return 0;
#endif
}
