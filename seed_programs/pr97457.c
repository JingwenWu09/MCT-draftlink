#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a;
long c;
signed char d(char e, char f) {
  return e + f;
}
int main(void) {
  for (; a <= 1; a++) {
    c = -8;
    for (; c != 3; c = d(c, 1)) {
      ;
    }
  }
  char b = c;
  if (b != 3) {
    __builtin_abort();
  }
}
