#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int b : 3;
  int c : 28;
  int d : 1;
};

int main() {
  struct S e = {};
  e.c = -1;
  if (e.d) {
    __builtin_abort();
  }
  return 0;
}
