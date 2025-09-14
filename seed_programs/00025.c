#include <math.h>
#include <stdio.h>
#include <stdlib.h>
int strlen(char *);

int main() {
  char *p;

  p = "hello";
  return strlen(p) - 5;
}
