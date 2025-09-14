#include <math.h>
#include <stdio.h>
#include <stdlib.h>

unsigned long l = (unsigned long)-2;
unsigned short s;

int main() {
  long t = l;
  s = t;
  if (s != (unsigned short)-2) {
    abort();
  }
  exit(0);
}
