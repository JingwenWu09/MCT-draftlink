#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct s {
  int a;
};

int main(void) {
  struct s x = {0};
  asm volatile("" : : "r"(&x) : "memory");
  return 0;
}
