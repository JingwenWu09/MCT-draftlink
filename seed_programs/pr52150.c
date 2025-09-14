#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void foo() __asm__("bar");

void foo() {
}

int main() {
  foo();
  return 0;
}
