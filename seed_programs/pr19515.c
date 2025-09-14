#include <math.h>
#include <stdio.h>
#include <stdlib.h>

union aun {
  char a2[8];
};

void abort(void);

int main(void) {
  union aun a = {{0}};

  if (a.a2[2] != 0) {
    abort();
  }
  return 0;
}
