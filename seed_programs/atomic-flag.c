#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
unsigned char a;

int main() {
  int b;

  __atomic_clear(&a, __ATOMIC_RELAXED);
  if (a != 0) {
    abort();
  }

  b = __atomic_test_and_set(&a, __ATOMIC_SEQ_CST);
  if (a != __GCC_ATOMIC_TEST_AND_SET_TRUEVAL || b != 0) {
    abort();
  }

  b = __atomic_test_and_set(&a, __ATOMIC_ACQ_REL);
  if (a != __GCC_ATOMIC_TEST_AND_SET_TRUEVAL || b != 1) {
    abort();
  }

  __atomic_clear(&a, __ATOMIC_SEQ_CST);
  if (a != 0) {
    abort();
  }

  return 0;
}
