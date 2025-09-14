#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

int a$b(void) {
  return 1;
}

int main(void) {

  if (a$b() != 1) {
    abort();
  }

  return 0;
}
