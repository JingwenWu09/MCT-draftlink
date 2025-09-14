#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char one[50] = "ijk";
int main(void) {
  return __builtin_strlen(one) != 3;
}
