#include <math.h>
#include <stdio.h>
#include <stdlib.h>
void abort();

long long a = 0x1234567800000000LL;

void f(long long a) {
  if ((a & 0xffffffffLL) != 0) {
    abort();
  }
}

int main() {
  f(a);
  return 0;
}
