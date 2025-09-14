#include <math.h>
#include <stdio.h>
#include <stdlib.h>

typedef __INT64_TYPE__ int64_t;
typedef __INT32_TYPE__ int32_t;

__attribute__((noipa, noinline)) int64_t f(int64_t a, int64_t b) {
  return (int64_t)(int32_t)a * (int64_t)(int32_t)b;
}

int main() {
  int64_t a = 0x1145140000000001;
  int64_t b = 0x1919810000000001;
  if (f(a, b) != 1) {
    __builtin_abort();
  }
}
