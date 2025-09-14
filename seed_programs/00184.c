#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  char a;
  short b;

  printf("%lu %lu\n", sizeof(char), sizeof(a));
  printf("%lu %lu\n", sizeof(short), sizeof(b));

  return 0;
}
