#include <math.h>
#include <stdio.h>
#include <stdlib.h>
extern void abort();

#define _ 2

int main() {
  if (_ != 2) {
    abort();
  }
  return 0;
}
