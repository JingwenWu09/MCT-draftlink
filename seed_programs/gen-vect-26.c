#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

#define N 128

int main() {
  int i;
  char ia[N + 1];

  for (i = 1; i <= N; i++) {
    ia[i] = 5;
  }

  for (i = 1; i <= N; i++) {
    if (ia[i] != 5) {
      abort();
    }
  }

  return 0;
}
