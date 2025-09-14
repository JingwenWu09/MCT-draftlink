#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>
volatile int a, b;
int c, d, e, f;

static int fn1() {
  if (b) {
    d++;
    e = c || f;
  }
  return 0;
}

int main() {
  for (; a < 1; a++) {
    fn1();
    continue;
  }
  if (d != 0) {
    abort();
  }
  return 0;
}
