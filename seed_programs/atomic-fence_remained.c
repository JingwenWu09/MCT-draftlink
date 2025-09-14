#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  __atomic_thread_fence(__ATOMIC_RELAXED);
  __atomic_thread_fence(__ATOMIC_CONSUME);
  __atomic_thread_fence(__ATOMIC_ACQUIRE);
  __atomic_thread_fence(__ATOMIC_RELEASE);
  __atomic_thread_fence(__ATOMIC_ACQ_REL);
  __atomic_thread_fence(__ATOMIC_SEQ_CST);

  __atomic_signal_fence(__ATOMIC_RELAXED);
  __atomic_signal_fence(__ATOMIC_CONSUME);
  __atomic_signal_fence(__ATOMIC_ACQUIRE);
  __atomic_signal_fence(__ATOMIC_RELEASE);
  __atomic_signal_fence(__ATOMIC_ACQ_REL);
  __atomic_signal_fence(__ATOMIC_SEQ_CST);

  return 0;
}
