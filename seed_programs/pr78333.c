#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void __attribute__((no_instrument_function)) __cyg_profile_func_enter(void *this_fn, void *call_site) {
}

void __attribute__((no_instrument_function)) __cyg_profile_func_exit(void *this_fn, void *call_site) {
}

extern inline __attribute__((gnu_inline, always_inline)) int foo() {
}
int main() {
  foo();
  return 0;
}
