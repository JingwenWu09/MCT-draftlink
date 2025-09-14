#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include <stddef.h>

int main(void) {
  size_t len;

  len = ~(sizeof(size_t) - 1);

  return len - len;
}
