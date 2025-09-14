#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <limits.h>

int main() {
  unsigned long count = 8;

  if (count > INT_MAX) {
    abort();
  }

  return (0);
}
