#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((noipa)) float foo(float x, float y) {
  return x * __builtin_copysignf(1.0f, y) + y;
}

int main() {
  if (foo(1.25f, 7.25f) != 1.25f + 7.25f || foo(1.75f, -3.25f) != -1.75f + -3.25f || foo(-2.25f, 7.5f) != -2.25f + 7.5f || foo(-3.0f, -4.0f) != 3.0f + -4.0f) {
    __builtin_abort();
  }
  return 0;
}
