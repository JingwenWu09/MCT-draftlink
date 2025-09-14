#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int z[] = {};

int main(void) {
  __builtin_printf("%d\n", *(z + 1));
}
