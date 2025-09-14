#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define TEST_WORSE(args...) (5, ##args)
#define TEST_BAD(foo, args...) (2, (foo), ##args)

extern void abort();

static int add(int a, int b) {
  return a + b;
}

int main() {

  if (TEST_WORSE() != 5) {
    abort();
  }

  if (add TEST_BAD(5) != 7) {
    abort();
  }
  return 0;
}
