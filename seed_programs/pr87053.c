#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const union {
  struct {
    char x[4];
    char y[4];
  };
  struct {
    char z[8];
  };
} u = {{"1234", "567"}};

int main() {
  if (__builtin_strlen(u.z) != 7) {
    __builtin_abort();
  }
}
