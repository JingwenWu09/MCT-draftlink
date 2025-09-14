#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct f {
  signed t : 1;
};
int g(struct f *a, int t) __attribute__((noipa));
int g(struct f *a, int t) {
  if (t) {
    a->t = -1;
  } else {
    a->t = 0;
  }
  int t1 = a->t;
  if (t1) {
    return 1;
  }
  return t1;
}

int main(void) {
  struct f a;
  if (!g(&a, 1)) {
    __builtin_abort();
  }
  return 0;
}
