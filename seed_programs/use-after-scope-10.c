#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  int *ptr = 0;

  {
    int a;
    ptr = &a;
    *ptr = 12345;
  }

  *ptr = 12345;
  return *ptr;
}
