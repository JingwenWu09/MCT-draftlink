#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

#define N 128
#define OFF 3

int main_1(int off) {
  int i;
  char ia[N + OFF];

  for (i = 0; i < N; i++) {
    ia[i + off] = 5;
  }

  for (i = 0; i < N; i++) {
    if (ia[i + off] != 5) {
      abort();
    }
  }

  return 0;
}

static volatile int off = 1;

int main(void) {
  return main_1(off);
}
