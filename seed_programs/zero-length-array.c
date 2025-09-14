#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct {
  int foo;
  int bar[0];
} zla;

struct {
  int foo;
  int bar[];
} fam;

int main() {

  return 0;
}
