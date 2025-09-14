#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noinline, constructor(200))) int foo() {
  return 123;
}

__attribute__((noinline, constructor(400))) int bar() {
  return 123;
}

int main() {
  foo() + bar();

  return 0;
}
