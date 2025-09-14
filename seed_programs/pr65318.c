#include <math.h>
#include <stdio.h>
#include <stdlib.h>

static short a = 0;
short b = -1;
static unsigned short c = 0;

int main() {
  if (a <= b) {
    return 1;
  }

  return 0;
}
