#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);
int __attribute__((weak, visibility("hidden"))) foo(void) {
  return 1;
}
int main(void) {
  if (!foo()) {
    abort();
  }
  return 0;
}
