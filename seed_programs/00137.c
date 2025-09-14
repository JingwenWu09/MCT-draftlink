#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define x(y) #y

int main(void) {
  char *p;
  p = x(hello) " is better than bye";

  return (*p == 'h') ? 0 : 1;
}
