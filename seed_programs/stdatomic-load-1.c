#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdatomic.h>

extern void abort(void);

_Atomic char v;
char count;

int main() {
  v = 0;
  count = 0;

  if (atomic_load_explicit(&v, memory_order_relaxed) != count++) {
    abort();
  } else {
    v++;
  }

  if (atomic_load_explicit(&v, memory_order_acquire) != count++) {
    abort();
  } else {
    v++;
  }

  if (atomic_load_explicit(&v, memory_order_consume) != count++) {
    abort();
  } else {
    v++;
  }

  if (atomic_load_explicit(&v, memory_order_seq_cst) != count++) {
    abort();
  } else {
    v++;
  }

  if (atomic_load(&v) != count) {
    abort();
  }

  return 0;
}
