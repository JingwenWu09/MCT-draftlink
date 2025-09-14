#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

#define N 128

#if __LONG_MAX__ == 2147483647
typedef short half_word;
#else
typedef int half_word;
#endif

int main_1(int n, int *p) {
  int i;
  half_word ib[N];
  half_word ia[N];
  int k;

  for (i = 0; i < N; i++) {
    ia[i] = n;
  }

  for (i = 0; i < N; i++) {
    if (ia[i] != n) {
      abort();
    }
  }

  k = *p;
  for (i = 0; i < N; i++) {
    ib[i] = k;
  }

  for (i = 0; i < N; i++) {
    if (ib[i] != k) {
      abort();
    }
  }

  return 0;
}

static volatile int n = 1;

int main(void) {
  return main_1(n + 2, (int *)&n);
}
