#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct a {
  int i;
  char c;
};
struct b {
  float f;
  float g;
};
int main(void) {
  static struct b b;
  return ((struct a *)&b)->i;
}
