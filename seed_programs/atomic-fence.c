#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  __atomic_thread_fence(__ATOMIC_RELAXED);

  return 0;
}
