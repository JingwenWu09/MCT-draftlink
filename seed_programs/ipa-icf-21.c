#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <xmmintrin.h>

__attribute__((noinline)) void foo() {
  float x = 1.2345f;
  __m128 v = _mm_load1_ps(&x);
}

__attribute__((noinline)) void bar() {
  float x = 1.2345f;
  __m128 v = _mm_load1_ps(&x);
}

int main() {
  return 2;
}
