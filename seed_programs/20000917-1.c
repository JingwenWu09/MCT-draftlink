#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct int3 {
  int a, b, c;
};

struct int3 one(void) {
  return (struct int3){1, 1, 1};
}

struct int3 zero(void) {
  return (struct int3){0, 0, 0};
}

int main(void) {
  struct int3 a;
  *({
    ({
      one();
      &a;
    });
  }) = zero();
  if (a.a && a.b && a.c) {
    abort();
  }
  exit(0);
}
