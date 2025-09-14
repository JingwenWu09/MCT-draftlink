#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define N 16

int main() {
  struct {
    char ca[N];
  } s;
  int i;

  for (i = 0; i < N; i++) {
    s.ca[i] = 5;
  }

  for (i = 0; i < N; i++) {
    if (s.ca[i] != 5) {
      abort();
    }
  }

  return 0;
}
