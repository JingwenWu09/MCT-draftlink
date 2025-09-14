#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

unsigned int n = 1;

void check(unsigned int m) {
  if (m != (unsigned int)-1) {
    abort();
  }
}

int main(void) {
  unsigned int m;
  m = (1 | (2 - n)) | (-n);
  check(m);
  exit(0);
}
