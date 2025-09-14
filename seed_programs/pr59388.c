#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int a;
struct S {
  unsigned int f : 1;
} b;

int main() {
  a = (0 < b.f) | b.f;
  return a;
}
