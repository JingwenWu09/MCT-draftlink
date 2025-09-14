#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int foo1(unsigned char i) {
  switch (i) {
  case -1:

    return 1;
  case 256:

    return 2;
  default:
    return 3;
  }
}

int foo2(unsigned char i) {
  switch (i) {
  case -1 ... 1:

    return 1;
  case 254 ... 256:

    return 2;
  default:
    return 3;
  }
}

int main(void) {
  if (foo1(10) != 3) {
    abort();
  }
  if (foo2(10) != 3) {
    abort();
  }
  exit(0);
}
