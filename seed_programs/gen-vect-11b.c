#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

#define N 16

int main() {
  int i;
  char ia[N];
  char ic[N] = {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45};
  char ib[N] = {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45};

  for (i = 0; i < N; i++) {
    ia[i] = ib[i] * ic[i];
  }

  for (i = 0; i < N; i++) {
    if (ia[i] != (char)(ib[i] * ic[i])) {
      abort();
    }
  }

  return 0;
}
