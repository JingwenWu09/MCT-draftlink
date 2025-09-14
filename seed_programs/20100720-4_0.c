#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char *a;
int f;

void foo(void) {
  f = (*a != '-');
}
int main() {
  return 0;
}
