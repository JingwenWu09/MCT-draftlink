#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdatomic.h>

extern void abort(void);

_Atomic int v;
int count;

int main() {
  v = 0;
  count = 0;

  atomic_init(&v, count + 1);
  if (v != ++count) {
    abort();
  }

  atomic_store_explicit(&v, count + 1, memory_order_relaxed);
  if (v != ++count) {
    abort();
  }

  atomic_store_explicit(&v, count + 1, memory_order_release);
  if (v != ++count) {
    abort();
  }

  atomic_store_explicit(&v, count + 1, memory_order_seq_cst);
  if (v != ++count) {
    abort();
  }

  count++;

  atomic_store(&v, count);
  if (v != count) {
    abort();
  }

  return 0;
}
