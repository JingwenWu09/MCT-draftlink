#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  int *ptr = 0;

  for (unsigned i = 0; i < 2; i++) {
    switch (argc) {
    case 11111:;
      int a;
      ptr = &a;
      break;
      {
      default:
        ptr = &a;
        *ptr = 12345;
      case 222222:
      my_label:
        ptr = &a;
        break;
      }
    }
  }

  if (argc == 333333) {
    goto my_label;
  }

  return 0;
}
