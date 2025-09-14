#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort();

union vec_int4 {
  int i[4];
  __attribute__((altivec(vector__))) int v;
};

int main(void) {
  union vec_int4 i1;

  i1.v = (__attribute__((altivec(vector__))) int){31, 31, 31, 31};

  if (i1.i[0] != 31) {
    abort();
  }

  return 0;
}
