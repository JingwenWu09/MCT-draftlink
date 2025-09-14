#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdatomic.h>

extern void abort(void);

_Atomic char v;
char count, ret;

int main() {
  v = 0;
  count = 0;

  if (atomic_exchange_explicit(&v, count + 1, memory_order_relaxed) != count) {
    abort();
  }
  count++;

  if (atomic_exchange_explicit(&v, count + 1, memory_order_acquire) != count) {
    abort();
  }
  count++;

  if (atomic_exchange_explicit(&v, count + 1, memory_order_release) != count) {
    abort();
  }
  count++;

  if (atomic_exchange_explicit(&v, count + 1, memory_order_acq_rel) != count) {
    abort();
  }
  count++;

  if (atomic_exchange_explicit(&v, count + 1, memory_order_seq_cst) != count) {
    abort();
  }
  count++;

  count++;

  ret = atomic_exchange(&v, count);
  if (ret != count - 1 || v != count) {
    abort();
  }

  return 0;
}
