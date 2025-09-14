#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int a, b, c;
int main() {
  int i;
  for (i = 0; i < 1000; i++) {
    if (i % 17) {
      a++;
    } else {
      b++;
    }
    c++;
  }
  return 0;
}
