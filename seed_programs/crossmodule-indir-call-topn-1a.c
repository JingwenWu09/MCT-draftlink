#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#ifdef DOJOB
int one(int a) {
  return 1;
}

int two(int a) {
  return 0;
}
#else
int main() {
  return 0;
}
#endif
