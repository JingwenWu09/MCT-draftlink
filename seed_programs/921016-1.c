#include <math.h>
#include <stdio.h>
#include <stdlib.h>
main() {
  int j = 1081;
  struct {
    signed int m : 11;
  } l;
  if ((l.m = j) == j) {
    abort();
  }
  exit(0);
}
