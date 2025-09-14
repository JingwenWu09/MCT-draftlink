#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __UINTPTR_TYPE__ uintptr_t;

__attribute__((noinline, noclone)) int sub(int a, int b) {
  return a - b;
}

struct fatp_t {
  uintptr_t pa;
  uintptr_t pb;
}__attribute__((aligned(2 * __alignof__(uintptr_t))));

__attribute__((noinline, noclone)) void foo(struct fatp_t str, int a, int b) {
  int i = sub(a, b);
  if (i == 0) {
    i = sub(b, a);
  }
}

int main(void) {
  struct fatp_t ptr = {31415927, 27182818};
  foo(ptr, 1, 2);
  return 0;
}
