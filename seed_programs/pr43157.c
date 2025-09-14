#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define LABEL3(pfx, x) #pfx x
#define LABEL2(pfx, x) LABEL3(pfx, x)
#define LABEL(x) LABEL2(__USER_LABEL_PREFIX__, x)

unsigned int factorial_(unsigned int) __asm__(LABEL("factorial"));

unsigned int factorial(unsigned int i) {
  return i > 1 ? i * factorial_(i - 1) : 1;
}

int main(void) {
  return factorial(5);
}
