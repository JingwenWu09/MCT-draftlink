#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void (*volatile fn)(void);
static void foo(void) {
}

int main(void) {
  fn = foo;
  return 0;
}
