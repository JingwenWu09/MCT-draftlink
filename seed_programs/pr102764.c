#include <math.h>
#include <stdio.h>
#include <stdlib.h>

volatile int a;

int main(void) {
  for (int i = 0; i < 1000; i++) {
    if (i % 17) {
      a++;
    }
  }
}
