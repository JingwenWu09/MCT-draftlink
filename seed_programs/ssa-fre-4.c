#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int x;
int main() {
  x = 0;
  if (x) {
    for (int i = 0; i < 10; ++i) {
      x = i;
    }
  }
  return x;
}
