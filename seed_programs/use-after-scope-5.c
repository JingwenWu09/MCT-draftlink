#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int *ptr;

__attribute__((always_inline)) inline static void foo(int v) {
  int values[10];
  for (unsigned i = 0; i < 10; i++) {
    values[i] = v;
  }

  ptr = &values[3];
}

int main(int argc, char **argv) {
  foo(argc);

  return *ptr;
}
