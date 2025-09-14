#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static __thread int vara;

int foo(int b) {
  return vara + b;
}

int main(void) {
  return foo(0);
}
