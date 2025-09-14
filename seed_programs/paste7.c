#include <math.h>
#include <stdio.h>
#include <stdlib.h>

extern void abort(void);
extern void exit(int);

int xx_a, xx_b;

int main(void) {
  xx_a = 1;
  xx_b = 2;
  if (xx_a != 1 || xx_b != 2) {
    abort();
  }
  exit(0);
}
