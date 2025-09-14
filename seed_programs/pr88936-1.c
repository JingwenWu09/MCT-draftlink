#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static long bug(long depth, long *v) {
  if (depth == 0) {
    *v = 0;
    return 1;
  }

  long r = 1;
  long val = bug(depth - 1, &r);
  return 2 * r + val;
}

static long ff(long depth) {
  return bug(depth, (long *)0);
}

int main() {
  if (ff(1) != 1) {
    __builtin_abort();
  }
  return 0;
}
