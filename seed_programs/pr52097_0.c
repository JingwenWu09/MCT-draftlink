#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
  unsigned int e0 : 16;
} s1;
typedef struct {
  unsigned int e0 : 16;
} s2;
typedef struct {
  s1 i1;
  s2 i2;
} io;

void f1(void) {
  static io temp;
  static io *i = &temp;
  s1 x0;
  i->i1 = x0;
}

int main() {
  f1();
  return 0;
}
