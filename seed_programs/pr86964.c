#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct S {
  int i;
};
extern struct S x;
int y;
int main() {
  return y;
}
