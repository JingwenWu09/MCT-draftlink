#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define M(x) x
#define A(aa, bb) aa(bb)

int main(void) {
  char *a = A(M, "hi");

  return (a[1] == 'i') ? 0 : 1;
}
