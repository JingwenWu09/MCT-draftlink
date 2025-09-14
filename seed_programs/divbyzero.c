#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define ZERO (4 - 6 + 2)

int main(int argc, char *argv[]) {
  int w = argc % ZERO;
  int x = argc / 0;
  int y = argc / ZERO;

  double z = 0.0 / 0.0;
  w = (ZERO ? y / ZERO : x);
  x = (ZERO ? argc % ZERO : x);

  return 0;
}
