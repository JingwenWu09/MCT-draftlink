#include <math.h>
#include <stdio.h>
#include <stdlib.h>
struct bitt {
  int a;
  unsigned int bitfield_a : 10;
  unsigned int bitfield_b : 7;
  unsigned int bitfield_c : 19;
} bitty;

struct no_bitt {
  int a;
  int b;
} no_bitty;

int main() {
  return bitty.bitfield_b + bitty.a;
}
