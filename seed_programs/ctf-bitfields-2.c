#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct fields {
  unsigned long long u1 : 15;
  unsigned long long u2 : 33;
  unsigned long long u3 : 16;
  signed long long s1 : 15;
  signed long long s2 : 33;
  signed long long s3 : 16;
} flags;

int i = 33;

int main() {
  return flags.u1 + i;
}
