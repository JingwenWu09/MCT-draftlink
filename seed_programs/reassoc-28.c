#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define LENGTH 4

unsigned char a[] = {1, 1, 1, 1};

void abort(void);
unsigned __attribute__((noinline)) foo(unsigned char *buf, int n) {
  unsigned sum = 0, i = 0;
  do {
    sum += (buf)[n - 1];

    if (n > LENGTH) {
      i++;
    }
    sum += buf[n - 2];
    sum += buf[n - 3];
    sum += buf[n - 4];
    n = n - 4;
  } while (n > 0);

  return sum + i;
}

int main() {
  int sum = foo(a, LENGTH);
  if (sum != 4) {
    abort();
  }
  return 0;
}
