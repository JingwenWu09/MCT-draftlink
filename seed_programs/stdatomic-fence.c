#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdatomic.h>

int main() {
  atomic_thread_fence(memory_order_relaxed);
  atomic_thread_fence(memory_order_consume);
  atomic_thread_fence(memory_order_acquire);
  atomic_thread_fence(memory_order_release);
  atomic_thread_fence(memory_order_acq_rel);
  atomic_thread_fence(memory_order_seq_cst);

  atomic_signal_fence(memory_order_relaxed);
  atomic_signal_fence(memory_order_consume);
  atomic_signal_fence(memory_order_acquire);
  atomic_signal_fence(memory_order_release);
  atomic_signal_fence(memory_order_acq_rel);
  atomic_signal_fence(memory_order_seq_cst);

  return 0;
}
