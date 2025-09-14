#include <math.h>
#include <stdio.h>
#include <stdlib.h>

enum foo { foo1 = 0, foo2 = 0xffffffffffffffffULL, foo3 = 0xf0fffffffffffffeULL };

int main() {
  if (sizeof(enum foo) != sizeof(unsigned long long)) {
    abort();
  }
  exit(0);
}
