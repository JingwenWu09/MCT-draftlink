#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  struct {
    long status;
  } h;

  h.status = 0;
  if (((h.status & 128) == 1) && ((h.status & 32) == 0)) {
    abort();
  }
  exit(0);
}
