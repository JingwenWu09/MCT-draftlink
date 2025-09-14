#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int main() {
  struct {
    int x;
  } s = {0};
  return s.x;
}
