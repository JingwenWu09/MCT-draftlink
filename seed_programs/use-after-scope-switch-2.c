#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  int *ptr = 0;
  int *ptr2 = 0;
  int *ptr3 = 0;

  for (unsigned i = 0; i < 2; i++) {
    switch (argc) {
    case 1111:;
      int a, b, c;
    default:
      ptr = &a;
      ptr2 = &b;
      ptr3 = &c;
      break;
    }
  }

  return 0;
}
