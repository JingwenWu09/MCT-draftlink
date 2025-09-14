#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __UINTPTR_TYPE__ uintptr_t;

struct fatp_t {
  uintptr_t pa;
  uintptr_t pb;
} __attribute__((aligned(2 * __alignof__(uintptr_t))));

__attribute__((noinline, noclone)) void clear_stack(void) {
  char a[128 * 1024 + 128];

  __builtin_memset(a + 128 * 1024, 0, 128);
}

__attribute__((noinline, noclone)) void foo(struct fatp_t str, int count) {
  char a[128 * 1024];

  if (count > 0) {
    foo(str, count - 1);
  }
  clear_stack();
  count--;
}

int main(void) {
  struct fatp_t ptr = {31415927, 27182818};
  foo(ptr, 1);
  return 0;
}
