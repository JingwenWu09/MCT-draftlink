#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort(void);

long long v, count;

int main() {
  v = 0;
  count = 0;

  __atomic_store_n(&v, count + 1, __ATOMIC_RELAXED);
  if (v != ++count) {
    abort();
  }

  __atomic_store_n(&v, count + 1, __ATOMIC_RELEASE);
  if (v != ++count) {
    abort();
  }

  __atomic_store_n(&v, count + 1, __ATOMIC_SEQ_CST);
  if (v != ++count) {
    abort();
  }

  count++;

  __atomic_store(&v, &count, __ATOMIC_RELAXED);
  if (v != count++) {
    abort();
  }

  __atomic_store(&v, &count, __ATOMIC_RELEASE);
  if (v != count++) {
    abort();
  }

  __atomic_store(&v, &count, __ATOMIC_SEQ_CST);
  if (v != count) {
    abort();
  }

  return 0;
}
