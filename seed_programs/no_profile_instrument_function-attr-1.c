#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((no_profile_instrument_function)) int foo() {
  return 0;
}

__attribute__((no_profile_instrument_function)) int bar() {
  return 1;
}

int main() {
  return foo();
}
