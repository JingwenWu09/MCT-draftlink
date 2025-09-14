#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

int foo(void) __attribute__((weak));

int foo(void) {
  return 1;
}

int main(void) {

  if (foo()) {
    exit(0);
  } else {
    abort();
  }
}
