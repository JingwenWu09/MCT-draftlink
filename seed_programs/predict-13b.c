#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void exit(int);

int main(int argc, char **argv) {
  switch (argc) {
  case 1:
    return 1;
  case 2:
    return 2;
  case 3:
    exit(1);
  case 4:
    exit(2);
  default:
    return 5;
  }

  return 10;
}
