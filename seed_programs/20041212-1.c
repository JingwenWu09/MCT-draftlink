#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void *f(void) __attribute__((__noinline__));
void *f(void) {
  return f;
}
int main(void) {
  if (f() != f) {
    abort();
  }
  exit(0);
}
