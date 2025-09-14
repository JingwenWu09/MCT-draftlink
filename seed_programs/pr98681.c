#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noipa)) int foo(int x) {
  if (x > 32) {
    return (x << -64) & 255;
  } else {
    return x;
  }
}

int main() {
  if (foo(32) != 32 || foo(-150) != -150) {
    __builtin_abort();
  }
  return 0;
}
