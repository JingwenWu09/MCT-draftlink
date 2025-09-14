#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stdlib.h>

int __attribute__((noinline, noclone)) foo(int i, int j) {
  j = i;
  i = 2;
  if (j) {
    j++;
  }
  j += i + 1;
  return j;
}

int __attribute__((noinline, noclone)) bar(int j, int i) {
  j = i;
  i = 2;
  if (j) {
    j++;
  }
  j += i + 1;
  return j;
}

int main(void) {
  if (foo(0, 1) != 3) {
    abort();
  }
  if (bar(1, 0) != 3) {
    abort();
  }
  return 0;
}
