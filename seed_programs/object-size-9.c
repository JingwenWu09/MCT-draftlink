#include <math.h>
#include <stdio.h>
#include <stdlib.h>

struct T {
  int c;
  char d[];
};
struct T t __attribute__((aligned(4096))) = {1, "a"};

int baz(int i) {
  return t.d[i];
}

int main(void) {
  baz(2);
  return 0;
}
