#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int main() {
  int t = 1;
  unsigned c = 0, d1 = t ? 1 ^ c ^ 1 >> (-1) : 0;
  return d1;
}
