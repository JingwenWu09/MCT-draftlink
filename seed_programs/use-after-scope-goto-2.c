#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  int a = 123;

  if (argc == 0) {
    int *ptr;

    ptr = &a;
    *ptr = 1;
    return 0;
  }

  return 0;
}
