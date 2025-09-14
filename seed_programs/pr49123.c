#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
static struct S { int f : 1; } s;
static int v = -1;

int main() {
  s.f = v < 0;
  if ((unsigned int)s.f != -1U) {
    abort();
  }
  return 0;
}
