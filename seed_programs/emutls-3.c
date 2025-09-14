#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int i __attribute__((common));

extern void abort(void);

int test_code(int b) {
  i += b;
  return i;
}

int main(int ac, char *av[]) {
  int a = test_code(test_code(1));

  if ((a != 2) || (i != 2)) {
    abort();
  }

  return 0;
}
