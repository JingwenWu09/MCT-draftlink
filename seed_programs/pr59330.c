#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void free(void *ptr) {
}

void *foo(void) {
  return 0;
}

int main(void) {
  void *p = foo();
  free(p);
  return 0;
}
