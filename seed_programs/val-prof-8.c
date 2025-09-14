#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  unsigned u = (argc - 1);
  int counter = 0;

  for (unsigned i = 0; i < 100; i++) {
    unsigned x = i < 10 ? 16 : 15;
    counter += u % x;
  }

  return counter;
}
