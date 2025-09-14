#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char *argv[]) {
  static int a[] = {__builtin_constant_p(argc) ? 1 : 0};
  return a[0];
}
