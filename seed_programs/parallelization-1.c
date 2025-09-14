#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

void parloop(int N) {
  int i;
  int x[1000099];

  for (i = 0; i < N; i++) {
    x[i] = i + 3;
  }

  for (i = 0; i < N; i++) {
    if (x[i] != i + 3) {
      abort();
    }
  }
}

int main(void) {
  parloop(10000);

  return 0;
}
