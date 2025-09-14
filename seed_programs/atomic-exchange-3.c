#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

int v, count, ret;

int main() {
  v = 0;
  count = 0;

  if (__atomic_exchange_n(&v, count + 1, __ATOMIC_RELAXED) != count) {
    abort();
  }
  count++;

  if (__atomic_exchange_n(&v, count + 1, __ATOMIC_ACQUIRE) != count) {
    abort();
  }
  count++;

  if (__atomic_exchange_n(&v, count + 1, __ATOMIC_RELEASE) != count) {
    abort();
  }
  count++;

  if (__atomic_exchange_n(&v, count + 1, __ATOMIC_ACQ_REL) != count) {
    abort();
  }
  count++;

  if (__atomic_exchange_n(&v, count + 1, __ATOMIC_SEQ_CST) != count) {
    abort();
  }
  count++;

  count++;

  __atomic_exchange(&v, &count, &ret, __ATOMIC_RELAXED);
  if (ret != count - 1 || v != count) {
    abort();
  }
  count++;

  __atomic_exchange(&v, &count, &ret, __ATOMIC_ACQUIRE);
  if (ret != count - 1 || v != count) {
    abort();
  }
  count++;

  __atomic_exchange(&v, &count, &ret, __ATOMIC_RELEASE);
  if (ret != count - 1 || v != count) {
    abort();
  }
  count++;

  __atomic_exchange(&v, &count, &ret, __ATOMIC_ACQ_REL);
  if (ret != count - 1 || v != count) {
    abort();
  }
  count++;

  __atomic_exchange(&v, &count, &ret, __ATOMIC_SEQ_CST);
  if (ret != count - 1 || v != count) {
    abort();
  }
  count++;

  return 0;
}
