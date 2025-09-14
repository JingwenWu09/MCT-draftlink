#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int foo(int i) {
  {
    volatile int j = i + 3;
    return j - 2;
  }
}
int main() {
  volatile int z = foo(-1);
  return z;
}
