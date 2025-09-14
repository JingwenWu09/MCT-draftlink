#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

char c = 0xff;

int main() {
  int i = (unsigned)c;
  if (i >= 0) {
    abort();
  }
  return 0;
}
