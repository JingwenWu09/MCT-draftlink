#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdatomic.h>

extern void abort(void);

_Atomic int a = ATOMIC_VAR_INIT(1), b;

int main() {
  b = kill_dependency(a);
  if (b != 1) {
    abort();
  }

  return 0;
}
