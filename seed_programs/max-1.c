#include <math.h>
#include <stdio.h>
#include <stdlib.h>
__extension__ typedef __INTPTR_TYPE__ intptr_t;

extern void abort(void);

intptr_t fff[10];

void f(intptr_t a, intptr_t b) {
  intptr_t crcc = b;
  intptr_t d = *((intptr_t *)(a + 1));
  int i;

  a = d >= b ? d : b;

  for (i = 0; i < 10; i++) {
    fff[i] = a;
  }
}

intptr_t a = 10;
int main(void) {
  int i;
  f((intptr_t)(&a) - 1, 0);
  for (i = 0; i < 10; i++) {
    if (fff[i] != 10) {
      abort();
    }
  }
  return 0;
}
