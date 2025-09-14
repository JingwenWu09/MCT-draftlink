#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  char a;
  int b;
  double c;

  printf("%lu\n", sizeof(a));
  printf("%lu\n", sizeof(b));
  printf("%lu\n", sizeof(c));

  printf("%lu\n", sizeof(!a));

  return 0;
}
