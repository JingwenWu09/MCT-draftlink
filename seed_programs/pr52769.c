#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct foo_t {
  int should_be_zero;
  char s[6];
  int x;
};

int main(void) {
  volatile struct foo_t foo = {.s = "123456", .x = 2};

  if (foo.should_be_zero != 0) {
    __builtin_abort();
  }

  return 0;
}
