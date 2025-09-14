#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int main(void) {
  if ("main" == __func__) {
    abort();
  } else {
    exit(0);
  }
}
