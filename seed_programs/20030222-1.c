#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

void ll_to_int(long long x, volatile int *p) {
  int i;
  asm("" : "=r"(i) : "0"(x));
  *p = i;
}

int val = INT_MIN + 1;

int main() {
  volatile int i;

  ll_to_int((long long)val, &i);
  if (i != val) {
    abort();
  }

  exit(0);
}
