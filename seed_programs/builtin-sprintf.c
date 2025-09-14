#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define nan(x) __builtin_nan(x)

volatile double nan_0 = nan("0");
volatile double nan_x = nan("0xdeadbeef");

int main(void) {
  char buf[80];

  int cst_n_0 = __builtin_sprintf(buf, "%g", nan("0"));
  int cst_n_x = __builtin_sprintf(buf, "%g", nan("0xdeadbeef"));

  int var_n_0 = __builtin_sprintf(buf, "%g", nan_0);
  int var_n_x = __builtin_sprintf(buf, "%g", nan_x);

  if (cst_n_0 != var_n_0){
    __builtin_abort();
  }

  if (cst_n_x != var_n_x){
    __builtin_abort();
  }

  return 0;
}
