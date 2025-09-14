#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int a;
int *b = &a, **c = &b;
int main() {
  int **d = &b;
  *d = 0;
}
