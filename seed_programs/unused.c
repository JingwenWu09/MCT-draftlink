#include <math.h>
#include <stdio.h>
#include <stdlib.h>

__attribute__((transaction_safe)) static int unused_func() {
  return 12345;
}

int main() {
  return 0;
}
