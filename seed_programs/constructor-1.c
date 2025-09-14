#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int x, y;

void __attribute__((noinline)) bar(void) {
  y++;
}

void __attribute__((constructor)) foo(void) {
  if (!x) {
    bar();
    y++;
  }
}

int main() {
  x = 1;
  foo();
  foo();
  if (y != 2) {
    abort();
  }
  exit(0);
}
