#include <math.h>
#include <stdio.h>
#include <stdlib.h>
static int foo(int a __attribute__((unused))) {
}
int main(void) {
  return foo(0);
}
