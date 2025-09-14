#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct A {
  int i;
} __attribute__((aligned(sizeof(long int))));

extern void foo(struct A *);
extern void foo(struct A *);

int main() {
  return 0;
}
