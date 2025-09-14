#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);

typedef unsigned long mp_digit;

struct mp_int{
  int used, alloc, sign;
  mp_digit *dp;
};

int main(void) {
  struct mp_int i = {2, 0, -1};
  struct mp_int *a = &i;
  mp_digit b = 0;

  if (a->sign == 1) {
    abort();
  }
  if (a->used > 1) {
    return 0;
  }
  if (a->dp[0] > b) {
    return 0;
  }
  if (a->dp[0] < b) {
    abort();
  }

  return 0;
}
