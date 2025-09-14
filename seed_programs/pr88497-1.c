#include <math.h>
#include <stdio.h>
#include <stdlib.h>
typedef double v2df __attribute__((vector_size(16)));
__attribute__((noinline)) double test(double accumulator, v2df arg1[], v2df arg2[]) {
  v2df temp;
  temp = arg1[0] * arg2[0];
  accumulator += temp[0] + temp[1];
  temp = arg1[1] * arg2[1];
  accumulator += temp[0] + temp[1];
  temp = arg1[2] * arg2[2];
  accumulator += temp[0] + temp[1];
  temp = arg1[3] * arg2[3];
  accumulator += temp[0] + temp[1];
  return accumulator;
}

extern void abort(void);

int main() {
  v2df v2[4] = {{1.0, 2.0}, {4.0, 8.0}, {1.0, 3.0}, {9.0, 27.0}};
  v2df v3[4] = {{1.0, 4.0}, {16.0, 64.0}, {1.0, 2.0}, {3.0, 4.0}};
  double acc = 100.0;
  double res = test(acc, v2, v3);
  if (res != 827.0) {
    abort();
  }
  return 0;
}
