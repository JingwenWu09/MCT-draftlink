#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct be {
  unsigned short pad[1];
  unsigned char a;
  unsigned char b;
} __attribute__((scalar_storage_order("big-endian")));

struct le {
  unsigned short pad[3];
  unsigned char a;
  unsigned char b;
};

int a_or_b_different(struct be *x, struct le *y) {
  return (x->a != y->a) || (x->b != y->b);
}

int main(void) {
  struct be x = {.a = 1, .b = 2};
  struct le y = {.a = 1, .b = 2};

  if (a_or_b_different(&x, &y)) {
    __builtin_abort();
  }

  return 0;
}
