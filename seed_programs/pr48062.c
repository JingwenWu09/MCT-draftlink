#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main(void) {
  int i;
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wshadow"
  { int i; }
#pragma GCC diagnostic pop
}
