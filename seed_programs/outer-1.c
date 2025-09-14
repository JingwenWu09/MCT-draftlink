#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

void parloop(int N) {
  int i, j;
  int x[1000][1000];

  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
      x[i][j] = i + j + 3;
    }
  }

  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
      if (x[i][j] != i + j + 3) {
        abort();
      }
    }
  }
}

int main(void) {
  parloop(1000);

  return 0;
}
