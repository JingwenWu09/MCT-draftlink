#include <math.h>
#include <stdio.h>
#include <stdlib.h>

char a;
void b() {
  char *c[5];
  char *d = &a;
  &d;
  *(c[4] = d);
}
int main() {
  return 0;
}
