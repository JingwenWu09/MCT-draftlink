#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void abort(void);

int a$b(void) {
  return 1;
}
int a$b\U0001f600(void) {
  return 2;
}

int main(void) {

  if (a$b() != 1) {
    abort();
  }

  if (a$b\U0001f600() != 2) {
    abort();
  }

  return 0;
}
